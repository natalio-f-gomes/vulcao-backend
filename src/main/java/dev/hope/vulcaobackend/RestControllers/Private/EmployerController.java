package dev.hope.vulcaobackend.RestControllers.Private;

import dev.hope.vulcaobackend.Authentication.CookieUtil;
import dev.hope.vulcaobackend.Authentication.JWTService;
import dev.hope.vulcaobackend.DTOs.Contact;
import dev.hope.vulcaobackend.DTOs.Credentials;
import dev.hope.vulcaobackend.DTOs.FullName;
import dev.hope.vulcaobackend.Properties.Address;
import dev.hope.vulcaobackend.Properties.UserType;
import dev.hope.vulcaobackend.Services.Account.EmployerService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/api/v2/employer")
public class EmployerController {

    @Autowired
    EmployerService employerService;
    @Autowired
    private CookieUtil cookieUtil;
    @Autowired
    private JWTService jWTService;

    @GetMapping("get/profile")
    public ResponseEntity<?> getProfile(){
        return ResponseEntity.ok(employerService.getProfile());
    }

    @GetMapping("get/businesses")
    public ResponseEntity<?> getBusinesses(){
        return ResponseEntity.ok(employerService.getBusinesses());
    }


    @PutMapping("update/name")
    public ResponseEntity<?> updateEmployerName(@RequestBody FullName fullName){
        employerService.updateName(fullName);
        return ResponseEntity.ok().build();
    }

    @PutMapping("update/address")
    public ResponseEntity<?> updateEmployerAddress(@RequestBody Address address){
        employerService.updateAddress(address);
        return ResponseEntity.ok().build();
    }

    @PutMapping("update/contact")
    public ResponseEntity<?> updateEmployerContact(@RequestBody Contact contact){
        employerService.updateContact(contact);
        return ResponseEntity.ok().build();
    }

    @PutMapping("update/credentials")
    public ResponseEntity<?> updateEmployerCredentials(@RequestBody Credentials credentials,HttpServletResponse response){
        String username = employerService.updateCredentials(credentials);

        String token = jWTService.generateJWTToken(username, UserType.EMPLOYER.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, cookieUtil.createCookie(token).toString());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> EmployerLogout(HttpServletResponse response, Principal principal){

        if (principal == null) {
            // still clear the cookie so a half-logged-out client gets cleaned up
            response.addHeader(HttpHeaders.SET_COOKIE, cookieUtil.clearCookie().toString());
            return ResponseEntity.ok("ALREADY LOGGED OUT");
        }
        log.debug("EmployerLogout Endpoint reached:");

        response.addHeader(HttpHeaders.SET_COOKIE,cookieUtil.clearCookie().toString());
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Sessão encerrada com sucesso para o usuário: " + principal.getName());
    }



}
