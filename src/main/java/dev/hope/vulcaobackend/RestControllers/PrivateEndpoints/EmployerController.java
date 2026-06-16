package dev.hope.vulcaobackend.RestControllers.PrivateEndpoints;


import dev.hope.vulcaobackend.DataTransferObjects.UserContactData;
import dev.hope.vulcaobackend.DataTransferObjects.UserCredentials;
import dev.hope.vulcaobackend.DataTransferObjects.UserFullName;
import dev.hope.vulcaobackend.Models.Common.Address;
import dev.hope.vulcaobackend.Services.EmployerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.nio.file.AccessDeniedException;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/employer")
public class EmployerController {

    @Autowired private EmployerService employerService;

    @GetMapping("/company/get/all")
    public ResponseEntity<?> getAllEmployerCompanies(){
        String executiveUsername = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        return ResponseEntity.ok(employerService.listMyCompanies(executiveUsername));
    }

    //Get Executive profile
    @GetMapping("/get/my-profile")
    public ResponseEntity<?> getCurrentEmployerProfile() {
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        return ResponseEntity.ok( employerService.findEmployerProfile(sessionUser));
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

    @PutMapping("/update/{employerId}/credentials")
    public ResponseEntity<?>  updateUserCredentials(@PathVariable String employerId, @RequestBody UserCredentials newCredentials ) throws AccessDeniedException {
        try{
            System.out.println("New username: " + newCredentials.getUsername());
            employerService.updateEmployerCredentials(employerId, newCredentials);
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
