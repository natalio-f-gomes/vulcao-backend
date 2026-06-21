package dev.hope.vulcaobackend.RestControllers.PublicEndpoints;



import dev.hope.vulcaobackend.Authentication.CookieUtil;
import dev.hope.vulcaobackend.Authentication.IPService;
import dev.hope.vulcaobackend.Authentication.JWTService;
import dev.hope.vulcaobackend.Authentication.LoginRequest;
import dev.hope.vulcaobackend.Configuration.AWSSESConfig;
import dev.hope.vulcaobackend.DataTransferObjects.ResetPasswordRequest;
import dev.hope.vulcaobackend.Models.AccountEntities.EmployerModel;
import dev.hope.vulcaobackend.Models.AccountEntities.StaffModel;
import dev.hope.vulcaobackend.Models.AccountEntities.UserModel;
import dev.hope.vulcaobackend.Repositories.EmployerRepository;
import dev.hope.vulcaobackend.Repositories.StaffRepository;
import dev.hope.vulcaobackend.Repositories.UserRepository;
import dev.hope.vulcaobackend.Services.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Map;

@Slf4j
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

    @Autowired
    private CompanyService companyService;


    public AuthController(AWSSESConfig sesService) {
        this.sesService = sesService;
    }


    @PostMapping("/user/password/reset/request/{email}")
    public ResponseEntity<?> UserResetPasswordRequest(@PathVariable String email) {

        authService.validateEmail(email);

        boolean usernameExists = userRepository.findByEmail(email).isPresent();
        if(!usernameExists){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Este e-mail não está associado a nenhum cliente.");
        }

        String randomCode = mfaService.save(email);
        sesService.sendCode(email, randomCode);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Enviámos um código de verificação para o seu e-mail.");
    }

    @PostMapping("/user/password/reset/confirm/{email}/{mfaCode}")
    public ResponseEntity<?> UserResetPasswordConfirm(@PathVariable String email, @PathVariable String mfaCode, @RequestBody ResetPasswordRequest passwordRequest) {
        System.out.println("Employer reset password confirm endpoint reached");
        ResponseEntity<?> emailResponse = authService.validateEmail(email);
        if(!emailResponse.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return emailResponse;
        }

        UserModel user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Este e-mail não está associado a nenhum cliente."));
        ResponseEntity<?> mfaResponse = mfaService.confirmMFA(email, mfaCode);
        if(!mfaResponse.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return mfaResponse;
        }

        userService.resetUserPassword(user, passwordRequest.getNewPassword());
        return ResponseEntity.ok().body("Palavra-passe reposta com sucesso.\nSinta-se à vontade para iniciar sessão, estimado cliente.");

    }



    @PostMapping("/user/register/request/{userEmail}/{username}")
    public ResponseEntity<?> registerUserRequest(@PathVariable String userEmail, @PathVariable String username) {

        ResponseEntity<?> usernameResponse = authService.validateUsername(username);
        if(!usernameResponse.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return usernameResponse;
        }

        ResponseEntity<?> emailResponse =  authService.validateEmail(userEmail);
        if(!emailResponse.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return emailResponse;
        }

        boolean usernameExists = userRepository.findByUsername(username).isPresent();
        if(usernameExists){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Este nome de usuário já está em uso.Por favor escolhe um nome the usuário differente");
        }

        String randomCode = mfaService.save(userEmail);
        sesService.sendCode(userEmail, randomCode);
        return ResponseEntity.accepted().build(); //202
    }


    @PostMapping("/user/register/confirm/{mfaCode}")
    public ResponseEntity<?> userRegisterConfirm(@PathVariable String mfaCode, @RequestBody UserModel newUser){
        // Validate MFA
        ResponseEntity<?> mfaResponse = mfaService.confirmMFA(newUser.getEmail(), mfaCode);
        if(!mfaResponse.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){

            return mfaResponse;
        }

        // Validate name
        ResponseEntity<?> nameResponse = authService.validateName(newUser.getFirstName(), newUser.getLastName());
        if(!nameResponse.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return nameResponse;
        }

        // Validate email
        ResponseEntity<?> emailResponse = authService.validateEmail(newUser.getEmail());
        if(!emailResponse.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return emailResponse;
        }

        // Validate username
        ResponseEntity<?> usernameResponse = authService.validateUsername(newUser.getUsername());
        if(!usernameResponse.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return usernameResponse;
        }

        ResponseEntity<?> passwordResponse =authService.validatePassword(newUser.getPassword());
        if(!passwordResponse.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return passwordResponse;
        }

        // Check if user already exists
        boolean entityExists = globalAccessControl.checkIfEntityExistsBeforeRegister(newUser.getUsername());
        System.out.println("User EXISTS: " + entityExists);
        if (entityExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Este nome de utilizador já está em uso. Por favor, escolhe outro nome de utilizador"));
        }
        // Save the user
        try {
            String userId = globalAccessControl.generateNewIdForUser();
            newUser.setId(userId);
            userService.save(newUser);
            mfaService.deleteMFA(newUser.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Erro ao criar utilizador. Por favor, tente novamente."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erro inesperado. Por favor, tente novamente."));
        }
    }


    @PostMapping("/user/login/request/{username}")
    public ResponseEntity<?> userLoginRequest( @PathVariable String username) {
        authService.validateUsername(username);
        boolean usernameExists = userRepository.findByUsername(username).isPresent();
        if(!usernameExists){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não existe cliente com esse nome de usuario");
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
        if(!responseMFA.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return responseMFA;
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
        }
        String role = authService.determineRole(loginRequest.username());
        String token = jwtService.generateJWTToken(loginRequest.username(), role);
        response.addHeader(HttpHeaders.SET_COOKIE, cookieUtil.createCookie(token).toString());
        mfaService.deleteMFA(user.getEmail());
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
    public ResponseEntity<?> registerEmployerConfirm(@PathVariable String mfaCode, @RequestBody EmployerModel newEmployer){
        ResponseEntity<?> mfaResponse = mfaService.confirmMFA(newEmployer.getEmail(), mfaCode);
        if(!mfaResponse.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return mfaResponse;
        }
        ResponseEntity<?> nameResponse = authService.validateName(newEmployer.getFirstName(), newEmployer.getLastName());
        ResponseEntity<?> emailResponse =   authService.validateEmail(newEmployer.getEmail());
        ResponseEntity<?> usernameResponse =authService.validateUsername(newEmployer.getUsername());
        ResponseEntity<?> passwordResponse =authService.validatePassword(newEmployer.getPassword());
        if(!passwordResponse.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return passwordResponse;
        }

        if(!nameResponse.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return nameResponse;
        }
        if(!emailResponse.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return emailResponse;
        }
        if(!usernameResponse.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return usernameResponse;
        }


        boolean entityExists = globalAccessControl.checkIfEntityExistsBeforeRegister(
                newEmployer.getUsername()
        );

        if(entityExists){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Este nome de utilizador já está em uso. Por favor, escolhe outro nome de utilizador");
        }

        try {
            String employerId = globalAccessControl.generateNewIdForEmployer();
            newEmployer.setId(employerId);
            newEmployer.onCreate();
            employerService.registerEmployer(newEmployer);

            // Only delete MFA after successful registration
            mfaService.deleteMFA(newEmployer.getEmail());

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/employer/request/register/{email}/{username}")
    public ResponseEntity<?> registerEmployerRequest(@PathVariable String email, @PathVariable String username) {
        ResponseEntity<?> usernameResponse = authService.validateUsername(username);
        ResponseEntity<?> emailResponse = authService.validateEmail(email);
        if(!usernameResponse.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return usernameResponse;
        }
        if(!emailResponse.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return emailResponse;
        }
        boolean usernameExists = employerRepository.findByUsername(username).isPresent();
        if(usernameExists){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Este nome de utilizador já está em uso. Por favor, escolhe outro nome de utilizador");
        }
        authService.validateEmail(email);
        String randomCode = mfaService.save(email);
        sesService.sendCode(email, randomCode);
        return ResponseEntity.accepted().build(); //202
    }

    @PostMapping("/employer/request/login/{username}")
    public ResponseEntity<?> employerLoginRequest( @PathVariable String username) {
        ResponseEntity<?> usernameResponse = authService.validateUsername(username);
        if(!usernameResponse.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return usernameResponse;
        }
        boolean employerExists = employerRepository.findByUsername(username).isPresent();
        if(!employerExists){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresário(a) com este nome the usuário não foin encontrado: '" + username + "'");
        }
        EmployerModel employer = employerRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("Employer not found" + username));
        String randomCode = mfaService.save(employer.getEmail());
        sesService.sendCode(employer.getEmail(), randomCode);
        return ResponseEntity.accepted().build(); //202
    }


    @PostMapping("/employer/confirm/login/{mfaCode}")
    public ResponseEntity<?> loginEmployerConfirm(@PathVariable String mfaCode, @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        authService.validateUsername(loginRequest.username());
        EmployerModel employer = employerRepository.findByUsername(loginRequest.username()).orElseThrow(()->
                new UsernameNotFoundException("Empresário(a) com este nome the usuário não foin encontrado: '" + loginRequest.username() + "'"));

        ResponseEntity<?> responseMFA =  mfaService.confirmMFA(employer.getEmail(), mfaCode);
        if(!responseMFA.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return responseMFA;
        }
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
        }
        String role = authService.determineRole(loginRequest.username());
        System.out.println("ROLE: "+ role);
        String token = jwtService.generateJWTToken(loginRequest.username(), role);
        response.addHeader(HttpHeaders.SET_COOKIE, cookieUtil.createCookie(token).toString());
        mfaService.deleteMFA(employer.getEmail());
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



    @PostMapping("/employer/password/reset/request/{email}")
    public ResponseEntity<?> EmployerResetPasswordRequest(@PathVariable String email) {
        System.out.println("Employer reset password confirm endpoint reached");
        ResponseEntity<?> emailResponse = authService.validateEmail(email);
        if(!emailResponse.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return emailResponse;
        }

        boolean emailExists = employerRepository.findByEmail(email).isPresent();
        if(!emailExists){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Este e-mail não está associado a nenhum empresário(a).");
        }

        String randomCode = mfaService.save(email);
        sesService.sendCode(email, randomCode);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Enviámos um código de verificação para o seu e-mail.");
    }

    @PostMapping("/employer/password/reset/confirm/{email}/{mfaCode}")
    public ResponseEntity<?> EmployerResetPasswordConfirm(@PathVariable String email, @PathVariable String mfaCode, @RequestBody ResetPasswordRequest passwordRequest) {
        System.out.println("Employer reset password confirm endpoint reached");
        ResponseEntity<?> emailResponse = authService.validateEmail(email);
        if(!emailResponse.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return emailResponse;
        }

        EmployerModel employer = employerRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Este e-mail não está associado a nenhum empresário(a)."));
        ResponseEntity<?> mfaResponse = mfaService.confirmMFA(email, mfaCode);
        if(!mfaResponse.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return mfaResponse;
        }

        employerService.resetEmployerPassword(employer, passwordRequest.getNewPassword());
        return ResponseEntity.ok().body("Palavra-passe reposta com sucesso.\nSinta-se à vontade para iniciar sessão, estimado Empresário(a).");

    }


    @PostMapping("/staff/register")
    public ResponseEntity<?> registerStaff(@RequestBody StaffModel newStaff){

        if(newStaff.getCompanyId().isBlank()){
            return ResponseEntity.badRequest().body("Company ID is required");
        }


        if(globalAccessControl.checkIfEntityExistsBeforeRegister(newStaff.getUsername())){
            return ResponseEntity.badRequest().body("There is a user with this username already");
        }
        try {
            String staffId = globalAccessControl.generateNewIdForStaff();
            newStaff.setId(staffId);
            ResponseEntity<?> response =  staffService.registerStaff(newStaff);
            if(!response.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
                return  response;
            }
            return ResponseEntity.status(HttpStatus.CREATED).build();

                    } catch (NullPointerException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @PostMapping("/staff/request/login/{username}")
    public ResponseEntity<?> staffLoginRequest( @PathVariable String username) {
        ResponseEntity<?> usernameResponse =authService.validateUsername(username);
        if(!usernameResponse.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return usernameResponse;
        }
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
        if(!responseMFA.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
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
        String role = authService.determineRole(loginRequest.username());
        System.out.println("ROLE: "+ role);
        String token = jwtService.generateJWTToken(loginRequest.username(), role);
        response.addHeader(HttpHeaders.SET_COOKIE, cookieUtil.createCookie(token).toString());
        mfaService.deleteMFA(staff.getEmail());
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






