package dev.hope.vulcaobackend.RestControllers.PrivateEndpoints;


import dev.hope.vulcaobackend.Authentication.CookieUtil;
import dev.hope.vulcaobackend.Authentication.JWTService;
import dev.hope.vulcaobackend.DataTransferObjects.AccountDTOs.EmployerDTO;
import dev.hope.vulcaobackend.DataTransferObjects.CompanyDTO;
import dev.hope.vulcaobackend.DataTransferObjects.UserContactData;
import dev.hope.vulcaobackend.DataTransferObjects.UserCredentials;
import dev.hope.vulcaobackend.DataTransferObjects.UserFullName;
import dev.hope.vulcaobackend.Models.Common.Address;
import dev.hope.vulcaobackend.Repositories.EmployerRepository;
import dev.hope.vulcaobackend.Repositories.UserRepository;
import dev.hope.vulcaobackend.Services.EmployerService;
import dev.hope.vulcaobackend.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/employer")
public class EmployerController {

    @Autowired
    private UserService userService;
    @Autowired private EmployerService employerService;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JWTService jwtService;
    @Autowired private CookieUtil cookieUtil;
    @Autowired private EmployerRepository employerRepository;
    @Autowired private UserRepository userRepository;

    @GetMapping("/company/get/all")
    public List<CompanyDTO> getAllEmployerCompanies(){
        String executiveUsername = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        return employerService.listMyCompanies(executiveUsername);
    }

    @GetMapping("/get/all")
    public List<EmployerDTO> getAllEmployers(){
        String executiveUsername = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        return employerService.findAllEmployers();
    }

    //Get Executive profile
    @GetMapping("/get/my-profile")
    public EmployerDTO getCurrentEmployerProfile() {
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        return employerService.findEmployerProfile(sessionUser);
    }


    //Update User profile
    @PutMapping("/update/{username}/profile")
    public ResponseEntity<?> updatePersonalInfo(@PathVariable String username, @RequestBody UserFullName updatedUser)  {
        try{
            employerService.updateEmployerProfile(username, updatedUser);
            return ResponseEntity.status(HttpStatus.OK).build();
        }catch (AccessDeniedException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }

    @PutMapping("/update/{username}/address")
    public ResponseEntity<?> updateUserAddress(@PathVariable String username, @RequestBody Address newAddress) throws AccessDeniedException {
        try{
            employerService.updateEmployerAddress(username, newAddress);
            return ResponseEntity.status(HttpStatus.OK).build();
        }catch (AccessDeniedException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("/update/{username}/credentials")
    public ResponseEntity<?>  updateUserCredentials(@PathVariable String username, UserCredentials newCredentials ) throws AccessDeniedException {
        try{
            employerService.updateEmployerCredentials(username, newCredentials);
            return ResponseEntity.status(HttpStatus.OK).build();
        }catch (AccessDeniedException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("/update/{username}/contact")
    public ResponseEntity<?>  updateUserContact(@PathVariable String username, @RequestBody UserContactData updatedUser) throws AccessDeniedException {
        try{
            employerService.updateEmployerContact(username, updatedUser);
            return ResponseEntity.status(HttpStatus.OK).build();
        }catch (AccessDeniedException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

}
