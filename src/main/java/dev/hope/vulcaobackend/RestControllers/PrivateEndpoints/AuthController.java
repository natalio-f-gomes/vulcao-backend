package dev.hope.vulcaobackend.RestControllers.PrivateEndpoints;



import dev.hope.vulcaobackend.Authentication.CookieUtil;
import dev.hope.vulcaobackend.Authentication.IPService;
import dev.hope.vulcaobackend.Authentication.JWTService;
import dev.hope.vulcaobackend.Authentication.LoginRequest;
import dev.hope.vulcaobackend.Models.AccountEntities.EmployerModel;
import dev.hope.vulcaobackend.Models.AccountEntities.StaffModel;
import dev.hope.vulcaobackend.Models.AccountEntities.UserModel;
import dev.hope.vulcaobackend.Repositories.EmployerRepository;
import dev.hope.vulcaobackend.Repositories.StaffRepository;
import dev.hope.vulcaobackend.Repositories.UserRepository;
import dev.hope.vulcaobackend.RestControllers.APIResponse;
import dev.hope.vulcaobackend.Services.AuthService;
import dev.hope.vulcaobackend.Services.EmployerService;
import dev.hope.vulcaobackend.Services.GlobalAccessControl;
import dev.hope.vulcaobackend.Services.StaffService;
import dev.hope.vulcaobackend.Services.UserService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.nio.file.AccessDeniedException;
import java.security.Principal;

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



    @PostMapping("/user/register")
    public ResponseEntity<?> registerUser(@RequestBody UserModel newUser) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.valueToTree(newUser);
        System.out.println("JSON NODE USERNAME: " + jsonNode.get("username"));

        boolean isRequestValid = globalAccessControl.isRequestValid(newUser.getUsername(), newUser.getEmail(), newUser.getPassword(), newUser.getFirstName(), newUser.getLastName());
        boolean isUserDataValid = globalAccessControl.isDataValid(newUser.getUsername(), newUser.getFirstName(), newUser.getLastName(), newUser.getPassword());
        authService.validateEmail(newUser.getEmail().toString());
        authService.validateUsername(newUser.getUsername().toString());
        System.out.println("THE USER DATA IS VALID? : " + isUserDataValid);
        if (!isRequestValid || !isUserDataValid) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        boolean response = globalAccessControl.checkIfEntityExistsBeforeRegister(newUser.getUsername(), newUser.getEmail());
        System.out.println("User EXISTS Response: "+ response);
        if (response) {
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


    @PostMapping("/user/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest, HttpServletResponse response, HttpServletRequest request) {
        authService.validateUsername(loginRequest.username());
        UserModel user = userRepository.findByUsername(loginRequest.username()).orElseThrow(()-> new UsernameNotFoundException("USER WITH THIS USERNAME WAS NOT FOUND "+ loginRequest.username()));
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
        String ip = IPService.getClientIpAddress(request);
        System.out.println("User IP Address "+ ip);
        try{
            authService.logoutUser(principal.getName(), ip);
        }catch (AccessDeniedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        response.addHeader(HttpHeaders.SET_COOKIE,cookieUtil.clearCookie().toString());
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("LOGGED OUT SUCCESSFULLY");
    }

    @PostMapping("/employer/register")
    public ResponseEntity<?> registerEmployer(@RequestBody EmployerModel newEmployer){
        boolean isRequestValid = globalAccessControl.isRequestValid(newEmployer.getUsername(), newEmployer.getEmail(), newEmployer.getPassword(), newEmployer.getFirstName(), newEmployer.getLastName());
        boolean isUserDataValid = globalAccessControl.isDataValid(newEmployer.getUsername(), newEmployer.getFirstName(), newEmployer.getLastName(), newEmployer.getPassword());
        authService.validateEmail(newEmployer.getEmail());
        authService.validateUsername(newEmployer.getUsername());
        System.out.println("THE USER DATA IS VALID? : " + isUserDataValid);
        if (!isRequestValid || !isUserDataValid) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        System.out.println("Does the entity Exists: " + globalAccessControl.checkIfEntityExistsBeforeRegister(newEmployer.getUsername(), newEmployer.getEmail()));
        if(globalAccessControl.checkIfEntityExistsBeforeRegister(newEmployer.getUsername(), newEmployer.getEmail())){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }try{
            String employerId = globalAccessControl.generateNewIdForEmployer();
            newEmployer.setId(employerId);
            newEmployer.onCreate();
            employerService.registerEmployer(newEmployer);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/employer/login")
    public ResponseEntity<?> loginEmployer(@RequestBody LoginRequest loginRequest, HttpServletResponse response, HttpServletRequest request) {
        authService.validateUsername(loginRequest.username());
        EmployerModel employer = employerRepository.findByUsername(loginRequest.username()).orElseThrow(()-> new UsernameNotFoundException("THIS EMPLOYER DOES NOT EXIST"));

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
        authService.logoutEmployer(principal.getName(), ip);
        response.addHeader(HttpHeaders.SET_COOKIE,cookieUtil.clearCookie().toString());
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("LOGGED OUT SUCCESSFULLY");
    }

    @PostMapping("/staff/register")
    public ResponseEntity<?> registerStaff(@RequestBody StaffModel newStaff, HttpServletRequest request){

        if(newStaff.getCompanyId().isBlank()){
            return ResponseEntity.badRequest()
                    .body(new APIResponse(null, "Company ID is required"));
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
                    .body(new APIResponse(null, "Invalid input"));
        }

        if(globalAccessControl.checkIfEntityExistsBeforeRegister(newStaff.getUsername(), newStaff.getEmail())){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new APIResponse(null, "User already exists"));
        }
        try {
            String staffId = globalAccessControl.generateNewIdForStaff();
            newStaff.setId(staffId);
            staffService.registerStaff(newStaff, request);
                        //cartService.createCartOnce(newUser);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new APIResponse("Staff registered successfully", null));
                    } catch (NullPointerException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @PostMapping("/staff/login")
    public ResponseEntity<?> loginStaff(@RequestBody LoginRequest loginRequest, HttpServletResponse response, HttpServletRequest request) {
        authService.validateUsername(loginRequest.username());
        StaffModel staff = staffRepository.findByUsername(loginRequest.username()).orElseThrow(()-> new UsernameNotFoundException("THIS Staff DOES NOT EXIST"));
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
        return ResponseEntity.ok("Employer Logged In");
    }

    @PostMapping("/staff/logout")
    public ResponseEntity<?> logoutStaff(HttpServletResponse response,HttpServletRequest request, Principal principal) throws AccessDeniedException {
        String ip = IPService.getClientIpAddress(request);
        System.out.println("User IP Address "+ ip);
        authService.logoutStaff(principal.getName(), ip);
        response.addHeader(HttpHeaders.SET_COOKIE,cookieUtil.clearCookie().toString());
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("LOGGED OUT SUCCESSFULLY");

    }

}






