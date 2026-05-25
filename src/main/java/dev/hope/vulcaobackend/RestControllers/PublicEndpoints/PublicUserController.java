package dev.hope.vulcaobackend.RestControllers.PublicEndpoints;


import dev.hope.vulcaobackend.DataTransferObjects.AccountDTOs.UserDTO;
import dev.hope.vulcaobackend.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/public/user")
public class PublicUserController {

    @Autowired
    UserService userService;



    //Get Users
    @GetMapping("/get/all")
    public List<UserDTO> getAllUsers(){
        return userService.findAllUsers();
    }



    @GetMapping("/get/{username}")
    public UserDTO getByUsername(@PathVariable String username) {
        return userService.findUserByUsername(username);
    }

    @GetMapping("/get/{id}")
    public UserDTO getById(@PathVariable String id) {
       return userService.findUserById(id);
    }

    @GetMapping("/get/{email}")
    public UserDTO getByEmail(@PathVariable String email) {
        return userService.findUserByEmail(email);
    }


   
}
