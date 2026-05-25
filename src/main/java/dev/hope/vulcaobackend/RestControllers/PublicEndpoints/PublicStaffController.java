package dev.hope.vulcaobackend.RestControllers.PublicEndpoints;


import dev.hope.vulcaobackend.Authentication.CookieUtil;
import dev.hope.vulcaobackend.Authentication.JWTService;
import dev.hope.vulcaobackend.DataTransferObjects.AccountDTOs.StaffDTO;
import dev.hope.vulcaobackend.Repositories.EmployerRepository;
import dev.hope.vulcaobackend.Repositories.UserRepository;
import dev.hope.vulcaobackend.Services.StaffService;
import dev.hope.vulcaobackend.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/public/staff")
public class PublicStaffController {

    @Autowired
    private UserService userService;
    @Autowired private StaffService staffService;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JWTService jwtService;
    @Autowired private CookieUtil cookieUtil;
    @Autowired private EmployerRepository employerRepository;
    @Autowired private UserRepository userRepository;


    @GetMapping("/get/all/{companyId}")
    public List<StaffDTO> getAllStaff(@PathVariable String companyId){
        return staffService.findAllStaffs(companyId);
    }

    @GetMapping("/get/byId/{id}")
    public StaffDTO getStaffById(@PathVariable String id){
        return staffService.findStaffById(id);
    }

    @GetMapping("/get/byEmail/{email}")
    public StaffDTO getStaffByEmail(@PathVariable String email){
        return staffService.findStaffByEmail(email);
    }

    @GetMapping("/get/byUsername/{username}")
    public StaffDTO getStaffByUsername(@PathVariable String username){
        return staffService.findStaffByUsername(username);
    }





}
