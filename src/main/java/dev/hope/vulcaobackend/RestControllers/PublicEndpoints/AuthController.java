package dev.hope.vulcaobackend.RestControllers.PublicEndpoints;



import dev.hope.vulcaobackend.Authentication.CookieUtil;
import dev.hope.vulcaobackend.Authentication.IPService;
import dev.hope.vulcaobackend.Authentication.JWTService;
import dev.hope.vulcaobackend.Authentication.LoginRequest;
import dev.hope.vulcaobackend.Configuration.AWSSESConfig;
import dev.hope.vulcaobackend.Models.AccountEntities.EmployerModel;
import dev.hope.vulcaobackend.Models.AccountEntities.StaffModel;
import dev.hope.vulcaobackend.Models.AccountEntities.UserModel;
import dev.hope.vulcaobackend.Repositories.EmployerRepository;
import dev.hope.vulcaobackend.Repositories.StaffRepository;
import dev.hope.vulcaobackend.Repositories.UserRepository;
import dev.hope.vulcaobackend.RestControllers.APIResponse;
import dev.hope.vulcaobackend.Services.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JWTService jwtService;
    @Autowired private CookieUtil cookieUtil;
    @Autowired
    private AuthService authService;
    @Autowired
    private EmployerService employerService;
    @Autowired
    private EmployerRepository employerRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private StaffService staffService;
    @Autowired
    GlobalAccessControl globalAccessControl;

    @Autowired private MFAService mfaService;

    AWSSESConfig sesService;


    public AuthController(AWSSESConfig sesService) {
        this.sesService = sesService;
    }


    @PostMapping("/user/register/request/{userEmail}/{username}")
    public ResponseEntity<?> registerUserRequest(@PathVariable String userEmail, @PathVariable String username) {
        authService.validateUsername(username);
        authService.validateEmail(userEmail);

        boolean usernameExists = userRepository.findByUsername(username).isPresent();
        if(usernameExists){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Both the email and the username already taken");
        }

        String randomCode =mfaService.save(userEmail);
        sesService.sendCode(userEmail, randomCode);
        return ResponseEntity.accepted().build(); //202
    }

    @PostMapping("/user/register/confirm/{mfaCode}")
    public ResponseEntity<?> confirmMFA(@PathVariable String mfaCode, @RequestBody UserModel newUser){
        ResponseEntity<?> response =  mfaService.confirmMFA(newUser.getEmail(), mfaCode);
        if(!response.getStatusCode().isSameCodeAs(HttpStatus.OK)){
            return response;
        }

        boolean isRequestValid = globalAccessControl.isRequestValid(newUser.getUsername(), newUser.getEmail(), newUser.getPassword(), newUser.getFirstName(), newUser.getLastName());
        boolean isUserDataValid = globalAccessControl.isDataValid(newUser.getUsername(), newUser.getFirstName(), newUser.getLastName(), newUser.getPassword());
        authService.validateEmail(newUser.getEmail());
        authService.validateUsername(newUser.getUsername());
        System.out.println("THE USER DATA IS VALID? : " + isUserDataValid);
        if (!isRequestValid || !isUserDataValid) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        boolean entityExists = globalAccessControl.checkIfEntityExistsBeforeRegister(newUser.getUsername());
        System.out.println("User EXISTS Response: "+ response);
        if (entityExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        try {
            String userId = globalAccessControl.generateNewIdForUser();
            newUser.setId(userId);
            userService.save(newUser);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }





    @PostMapping("/user/login/request/{username}")
    public ResponseEntity<?> userLoginRequest( @PathVariable String username) {
        authService.validateUsername(username);
        boolean usernameExists = userRepository.findByUsername(username).isPresent();
        if(!usernameExists){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This user does not exist!");
        }
        UserModel user = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("This user does not exist"));
        String randomCode = mfaService.save(user.getEmail());
        sesService.sendCode(user.getEmail(), randomCode);
        return ResponseEntity.accepted().build(); //202
    }



    @PostMapping("/user/login/confirm/{mfaCode}")
    public ResponseEntity<?> loginUserConfirm(@PathVariable String mfaCode, @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        authService.validateUsername(loginRequest.username());
        UserModel user = userRepository.findByUsername(loginRequest.username()).orElseThrow(()-> new UsernameNotFoundException("USER WITH THIS USERNAME WAS NOT FOUND "+ loginRequest.username()));

        ResponseEntity<?> responseMFA =  mfaService.confirmMFA(user.getEmail(), mfaCode);
        if(!responseMFA.getStatusCode().isSameCodeAs(HttpStatus.OK)){
            return responseMFA;
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
        }
        String role = globalAccessControl.determineRole(loginRequest.username());
        String token = jwtService.generateJWTToken(loginRequest.username(), role);
        response.addHeader(HttpHeaders.SET_COOKIE, cookieUtil.createCookie(token).toString());
        return ResponseEntity.ok("Logged In");
    }

    @PostMapping("/user/logout")
    public ResponseEntity<?> logoutUser(HttpServletResponse response, HttpServletRequest request, Principal principal){
        if (principal == null) {
            // still clear the cookie so a half-logged-out client gets cleaned up
            response.addHeader(HttpHeaders.SET_COOKIE, cookieUtil.clearCookie().toString());
            return ResponseEntity.ok("ALREADY LOGGED OUT");
        }
        String ip = IPService.getClientIpAddress(request);
        System.out.println("User IP Address "+ ip);

        response.addHeader(HttpHeaders.SET_COOKIE,cookieUtil.clearCookie().toString());
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("LOGGED OUT SUCCESSFULLY");
    }

    @PostMapping("/employer/confirm/register/{mfaCode}")
    public ResponseEntity<?> registerEmployerConfirm(@PathVariable String mfaCode,
                                                     @RequestBody EmployerModel newEmployer) {
        // normalize first — mobile keyboards add spaces, casing varies
        newEmployer.setUsername(newEmployer.getUsername().trim());
        newEmployer.setEmail(newEmployer.getEmail().trim().toLowerCase());
        newEmployer.setFirstName(newEmployer.getFirstName().trim());
        newEmployer.setLastName(newEmployer.getLastName().trim());

        // validate BEFORE the code is consumed
        boolean valid = globalAccessControl.isRequestValid(newEmployer.getUsername(), newEmployer.getEmail(),
                newEmployer.getPassword(), newEmployer.getFirstName(), newEmployer.getLastName())
                && globalAccessControl.isDataValid(newEmployer.getUsername(), newEmployer.getFirstName(),
                newEmployer.getLastName(), newEmployer.getPassword());
        authService.validateEmail(newEmployer.getEmail());
        authService.validateUsername(newEmployer.getUsername());
        if (!valid) return ResponseEntity.badRequest().build();

        if (globalAccessControl.checkIfEntityExistsBeforeRegister(newEmployer.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // only now verify + consume the code
        ResponseEntity<?> response = mfaService.confirmMFA(newEmployer.getEmail(), mfaCode);
        if (!response.getStatusCode().isSameCodeAs(HttpStatus.OK)) return response;

        try {
            newEmployer.setId(globalAccessControl.generateNewIdForEmployer());
            newEmployer.onCreate();
            employerService.registerEmployer(newEmployer);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // don't hide as 400
        }
    }

    @PostMapping("/employer/request/register/{email}/{username}")
    public ResponseEntity<?> registerEmployerRequest(@PathVariable String email, @PathVariable String username) {
        boolean usernameExists = employerRepository.findByUsername(username).isPresent();
        if(usernameExists){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Both the email and the username already taken");
        }
        authService.validateEmail(email);
        String randomCode = mfaService.save(email);
        sesService.sendCode(email, randomCode);
        return ResponseEntity.accepted().build(); //202
    }

    @PostMapping("/employer/request/login/{username}")
    public ResponseEntity<?> employerLoginRequest( @PathVariable String username) {
        authService.validateUsername(username);
        boolean usernameExists = employerRepository.findByUsername(username).isPresent();
        if(!usernameExists){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This user does not exist!");
        }
        EmployerModel employer = employerRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("This user does not exist"));
        String randomCode = mfaService.save(employer.getEmail());
        sesService.sendCode(employer.getEmail(), randomCode);
        return ResponseEntity.accepted().build(); //202
    }


    @PostMapping("/employer/confirm/login/{mfaCode}")
    public ResponseEntity<?> loginEmployerConfirm(@PathVariable String mfaCode, @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        authService.validateUsername(loginRequest.username());
        EmployerModel employer = employerRepository.findByUsername(loginRequest.username()).orElseThrow(()-> new UsernameNotFoundException("EMPLOYER WITH THIS USERNAME WAS NOT FOUND "+ loginRequest.username()));

        ResponseEntity<?> responseMFA =  mfaService.confirmMFA(employer.getEmail(), mfaCode);
        if(!responseMFA.getStatusCode().isSameCodeAs(HttpStatus.OK)){
            return responseMFA;
        }
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
        }
        String role = globalAccessControl.determineRole(loginRequest.username());
        System.out.println("ROLE: "+ role);
        String token = jwtService.generateJWTToken(loginRequest.username(), role);
        response.addHeader(HttpHeaders.SET_COOKIE, cookieUtil.createCookie(token).toString());
        return ResponseEntity.ok("Employer Logged In");
    }

    @PostMapping("/employer/logout")
    public ResponseEntity<?> employerLogout(HttpServletResponse response,HttpServletRequest request, Principal principal){
        String ip = IPService.getClientIpAddress(request);
        System.out.println("User IP Address "+ ip);
        response.addHeader(HttpHeaders.SET_COOKIE,cookieUtil.clearCookie().toString());
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("LOGGED OUT SUCCESSFULLY");
    }

    @PostMapping("/staff/register")
    public ResponseEntity<?> registerStaff(@RequestBody StaffModel newStaff, HttpServletRequest request){

        if(newStaff.getCompanyId().isBlank()){
            return ResponseEntity.badRequest()
                    .body(new APIResponse(null, "Company ID is required",400, LocalDateTime.now()));
        }
        boolean isRequestValid = globalAccessControl.isRequestValid(newStaff.getUsername(), newStaff.getEmail(), newStaff.getPassword(), newStaff.getFirstName(), newStaff.getLastName());
        boolean isUserDataValid = globalAccessControl.isDataValid(newStaff.getUsername(), newStaff.getFirstName(), newStaff.getLastName(), newStaff.getPassword());
            authService.validateEmail(newStaff.getEmail().toString());
            authService.validateUsername(newStaff.getUsername().toString());
            System.out.println("THE USER DATA IS VALID? : " + isUserDataValid);
           if (!isRequestValid || !isUserDataValid) {
              return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
          }

        if(!globalAccessControl.isRequestValid(newStaff.getUsername(), newStaff.getEmail(), newStaff.getPassword(), newStaff.getFirstName(), newStaff.getLastName())){
            return ResponseEntity.badRequest()
                    .body(new APIResponse(null, "Invalid input",400, LocalDateTime.now()));
        }

        if(globalAccessControl.checkIfEntityExistsBeforeRegister(newStaff.getUsername())){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new APIResponse(null, "User already exists", 409, LocalDateTime.now()));
        }
        try {
            String staffId = globalAccessControl.generateNewIdForStaff();
            newStaff.setId(staffId);
            staffService.registerStaff(newStaff, request);
                        //cartService.createCartOnce(newUser);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new APIResponse("Staff registered successfully", null, 201, LocalDateTime.now()));
                    } catch (NullPointerException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @PostMapping("/staff/request/login/{username}")
    public ResponseEntity<?> staffLoginRequest( @PathVariable String username) {
        authService.validateUsername(username);
        boolean usernameExists = staffRepository.findByUsername(username).isPresent();
        if(!usernameExists){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This user does not exist!");
        }
        StaffModel staff = staffRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("This user does not exist"));
        String randomCode = mfaService.save(staff.getEmail());
        sesService.sendCode(staff.getEmail(), randomCode);
        return ResponseEntity.accepted().build(); //202
    }


    @PostMapping("/staff/confirm/login/{mfaCode}")
    public ResponseEntity<?> ConfirmStaffLogin(@PathVariable String mfaCode, @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        authService.validateUsername(loginRequest.username());
        StaffModel staff = staffRepository.findByUsername(loginRequest.username()).orElseThrow(()-> new UsernameNotFoundException("THIS Staff DOES NOT EXIST"));

        ResponseEntity<?> responseMFA =  mfaService.confirmMFA(staff.getEmail(), mfaCode);
        if(!responseMFA.getStatusCode().isSameCodeAs(HttpStatus.OK)){
            return responseMFA;
        }
        authService.validateUsername(loginRequest.username());

        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
        }
        String role = globalAccessControl.determineRole(loginRequest.username());
        System.out.println("ROLE: "+ role);
        String token = jwtService.generateJWTToken(loginRequest.username(), role);
        response.addHeader(HttpHeaders.SET_COOKIE, cookieUtil.createCookie(token).toString());
        return ResponseEntity.ok("Staff Logged In");
    }


    @PostMapping("/staff/logout")
    public ResponseEntity<?> logoutStaff(HttpServletResponse response,HttpServletRequest request, Principal principal) throws AccessDeniedException {
        String ip = IPService.getClientIpAddress(request);
        System.out.println("User IP Address "+ ip);
        response.addHeader(HttpHeaders.SET_COOKIE,cookieUtil.clearCookie().toString());
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("LOGGED OUT SUCCESSFULLY");

    }

}






