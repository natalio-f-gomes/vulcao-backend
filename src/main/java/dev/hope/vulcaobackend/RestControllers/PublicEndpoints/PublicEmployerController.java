package dev.hope.vulcaobackend.RestControllers.PublicEndpoints;


import dev.hope.vulcaobackend.Authentication.CookieUtil;
import dev.hope.vulcaobackend.Authentication.JWTService;
import dev.hope.vulcaobackend.DataTransferObjects.AccountDTOs.EmployerDTO;
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
@RequestMapping("/api/v1/public/employer")
public class PublicEmployerController {

    @Autowired
    private UserService userService;
    @Autowired private EmployerService employerService;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JWTService jwtService;
    @Autowired private CookieUtil cookieUtil;
    @Autowired private EmployerRepository employerRepository;
    @Autowired private UserRepository userRepository;



    @GetMapping("/get/all")
    public List<EmployerDTO> getAllEmployers(){
        return employerService.findAllEmployers();
    }

    @GetMapping("/get/byId/{id}")
    public EmployerDTO getEmployerById(@PathVariable String id){
        return employerService.findEmployerById(id);
    }

    @GetMapping("/get/byEmail/{email}")
    public EmployerDTO getEmployerByEmail(@PathVariable String email){
        return employerService.findEmployerByEmail(email);
    }

    @GetMapping("/get/byUsername/{username}")
    public EmployerDTO getEmployerByUsername(@PathVariable String username){
        return employerService.findEmployerByUsername (username);
    }





}
