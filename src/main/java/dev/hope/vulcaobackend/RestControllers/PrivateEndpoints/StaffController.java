package dev.hope.vulcaobackend.RestControllers.PrivateEndpoints;

import dev.hope.vulcaobackend.Authentication.CookieUtil;
import dev.hope.vulcaobackend.Authentication.JWTService;
import dev.hope.vulcaobackend.DataTransferObjects.AccountDTOs.StaffDTO;
import dev.hope.vulcaobackend.DataTransferObjects.UserContactData;
import dev.hope.vulcaobackend.DataTransferObjects.UserCredentials;
import dev.hope.vulcaobackend.DataTransferObjects.UserFullName;
import dev.hope.vulcaobackend.Models.AccountEntities.StaffModel;
import dev.hope.vulcaobackend.Models.Common.Address;
import dev.hope.vulcaobackend.Models.Properties.Permissions;
import dev.hope.vulcaobackend.Repositories.EmployerRepository;
import dev.hope.vulcaobackend.Repositories.UserRepository;
import dev.hope.vulcaobackend.Services.StaffService;
import dev.hope.vulcaobackend.Services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/staff")
public class StaffController {

    @Autowired
    private UserService userService;
    @Autowired private StaffService staffService;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JWTService jwtService;
    @Autowired private CookieUtil cookieUtil;
    @Autowired private EmployerRepository employerRepository;
    @Autowired private UserRepository userRepository;


    @GetMapping("/get/all/{companyId}")
    public ResponseEntity<?> getAllStaff(@PathVariable String companyId){
        return ResponseEntity.ok(staffService.findAllStaffs(companyId));
    }

    //Get staff profile
    @GetMapping("/get/my-profile")
    public ResponseEntity<?> getCurrentStaffProfile() {
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        return ResponseEntity.ok(staffService.findStaffProfile(sessionUser));
    }


    //Update Staff account
    @PutMapping("/update/{staffId}")
    public ResponseEntity<?> employerUpdateStaff(@PathVariable String staffId, @RequestBody StaffModel updatedUser, HttpServletRequest request)  {
        try{
            staffService.employerUpdateStaff(staffId, updatedUser, request);
            return ResponseEntity.status(HttpStatus.OK).build();
        }catch (AccessDeniedException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    //Update Staff Address
    @PutMapping("/update/{staffId}/address")
    public ResponseEntity<?> updateStaffAddress(@PathVariable String staffId, @RequestBody Address newAddress, HttpServletRequest request)  {
        try{
            staffService.updateStaffAddress(staffId, newAddress);
            return ResponseEntity.status(HttpStatus.OK).build();
        }catch (AccessDeniedException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }



    @PutMapping("/update/{username}/credentials")
    public ResponseEntity<?>  updateStaffCredentials(@PathVariable String username, UserCredentials newCredentials, HttpServletRequest request )  {
        try{
            staffService.editStaffCredentials(username, newCredentials, request);
            return ResponseEntity.status(HttpStatus.OK).build();
        }catch (AccessDeniedException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("/update/{staffId}/contact")
    public ResponseEntity<?>  updateStaffContact(@PathVariable String staffId, @RequestBody UserContactData updatedUser, HttpServletRequest request)  {
        try{
            staffService.editStaffContact(staffId, updatedUser, request);
            return ResponseEntity.status(HttpStatus.OK).build();
        }catch (AccessDeniedException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("/update/permissions/{StaffId}")
    public ResponseEntity<?>  employerAssignsPermissionToStaff(@PathVariable String StaffId, @RequestBody Set<Permissions> permissions, HttpServletRequest request)  {
        try{
            staffService.employerAssignPermissionsToStaff(StaffId, permissions, request);
            return ResponseEntity.status(HttpStatus.OK).build();
        }catch (AccessDeniedException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("/update/{staffId}/profile")
    public ResponseEntity<?>  updateStaffProfile(@PathVariable String staffId, @RequestBody UserFullName profile, HttpServletRequest request) {
        try{
            staffService.editStaffProfile(staffId, profile, request);
            return ResponseEntity.status(HttpStatus.OK).build();
        }catch (AccessDeniedException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }






}
