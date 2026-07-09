package dev.hope.vulcaobackend.RestControllers.Public;

import dev.hope.vulcaobackend.Authentication.CookieUtil;
import dev.hope.vulcaobackend.Authentication.JWTService;
import dev.hope.vulcaobackend.Authentication.LoginRequest;
import dev.hope.vulcaobackend.Configuration.AWSSESConfig;
import dev.hope.vulcaobackend.DTOs.ResetPasswordRequest;
import dev.hope.vulcaobackend.DTOs.UserDTO;
import dev.hope.vulcaobackend.Models.UserModel;
import dev.hope.vulcaobackend.Properties.UserType;
import dev.hope.vulcaobackend.Repositories.UserRepository;
import dev.hope.vulcaobackend.Services.Account.EmployerService;
import dev.hope.vulcaobackend.Services.ExceptionHandler.ApiException;
import dev.hope.vulcaobackend.Services.GlobalServices;
import dev.hope.vulcaobackend.Services.MFAService;
import dev.hope.vulcaobackend.Services.Validators;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v2/auth/employer")
public class EmployerAuthController {
    @Autowired
    EmployerService employerService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MFAService mfaService;
    AWSSESConfig sesService;
    @Autowired GlobalServices globalServices;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTService jWTService;
    @Autowired
    private CookieUtil cookieUtil;

    public EmployerAuthController(AWSSESConfig sesService) {
        this.sesService = sesService;
    }

    @PostMapping("/register/request/{username}/{email}")
    public ResponseEntity<?> RegisterRequest(@PathVariable String username, @PathVariable String email){
        Validators.validateUsername(username);
        Validators.validateEmail(email);
        globalServices.isUsernameTaken(username);
        String randomCode = mfaService.save(email);
        sesService.sendCode(email, randomCode);
        return ResponseEntity.accepted().build(); //202
    }


    @PostMapping("/register/confirm/{mfaCode}")
    public ResponseEntity<?> RegisterConfirm(@PathVariable String mfaCode, @RequestBody UserDTO newUser){
        mfaService.confirmMFA(newUser.getEmail(), mfaCode);
        mfaService.deleteMFA(newUser.getEmail());
        employerService.save(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }


    @PostMapping("/login/request/{username}")
    public ResponseEntity<?> LoginRequest( @PathVariable String username) {
        Validators.validateUsername(username);
        Optional<UserModel> usernameExists = userRepository.findByUsername(username);
        if(usernameExists.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não existe empresário(a) com esse nome de usuario");
        }
        UserModel user = usernameExists.get();
        if(!user.getUserType().equals(UserType.EMPLOYER.toString())){
            throw new ApiException(HttpStatus.FORBIDDEN, "Não existe nenhum empresário(a) com este nome de usuário.");
        }
        String randomCode = mfaService.save(user.getEmail());
        sesService.sendCode(user.getEmail(), randomCode);
        return ResponseEntity.accepted().build(); //202
    }

    @PostMapping("/login/confirm/{mfaCode}")
    public ResponseEntity<?> LoginConfirm(@PathVariable String mfaCode, @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        String username = loginRequest.username();
        Validators.validateUsername(username);

        Optional<UserModel> usernameExists = userRepository.findByUsername(username);
        if(usernameExists.isEmpty()){
            throw new ApiException(HttpStatus.FORBIDDEN, "Não existe nenhum empresário(a) com este nome de usuário.");
        }
        UserModel user = usernameExists.get();
        if(!user.getUserType().equals(UserType.EMPLOYER.toString())){
            throw new ApiException(HttpStatus.FORBIDDEN, "Não existe nenhum empresário(a) com este nome de usuário.");
        }
        mfaService.confirmMFA(user.getEmail(), mfaCode);


        try {
             authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, loginRequest.password())
            );
        } catch (AuthenticationException e) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Usuário ou senha incorretos.");
        }
        String token = jWTService.generateJWTToken(username, UserType.EMPLOYER.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, cookieUtil.createCookie(token).toString());
        mfaService.deleteMFA(user.getEmail());
        return ResponseEntity.ok("Logged In");
    }

    @PutMapping("/reset/password/request/{email}")
    public ResponseEntity<?> ResetPasswordRequest(@PathVariable String email) {
        Validators.validateEmail(email);
        globalServices.getEmployerByEmail(email);
        String randomCode = mfaService.save(email);
        sesService.sendCode(email, randomCode);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Enviámos um código de verificação para o seu e-mail.");
    }

    @PutMapping("/reset/password/confirm/{email}/{mfaCode}")
    public ResponseEntity<?> ResetPasswordConfirm(@PathVariable String email, @PathVariable String mfaCode, @RequestBody ResetPasswordRequest passwordRequest) {
        System.out.println("Employer reset password confirm endpoint reached");
        Validators.validateEmail(email);
        mfaService.confirmMFA(email, mfaCode);
        employerService.resetPassword(email, passwordRequest);
        return ResponseEntity.ok().body("Palavra-passe reposta com sucesso.\nSinta-se à vontade para iniciar sessão, estimado empresário(a).");

    }





}
