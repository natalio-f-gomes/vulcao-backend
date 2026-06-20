package dev.hope.vulcaobackend.Services;


import dev.hope.vulcaobackend.Models.Offerings.ProductModel;
import dev.hope.vulcaobackend.Models.Offerings.WearableModel;
import dev.hope.vulcaobackend.Repositories.*;
import dev.hope.vulcaobackend.Repositories.Cart.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.rmi.server.LogStream.log;

@Service
public class GlobalAccessControl {


    @Autowired
    StaffRepository staffRepository;
    @Autowired
    EmployerRepository employerRepository;
    @Autowired
    AuthService authService;
    @Autowired
    UserRepository userRepository;
 


    public static final CharSequence[] SYMBOLS = {
            "[", "]", "{", "}", ",", "=", "...",
            "@", "#", "$", "%", "^", "&", "*", "(", ")", "!",
            "/", "\\", "|", ":", ";", "?", "<", ">",
            "~", "`", "'", "\"",
            "->", "=>", "::", "!=", "==", "<=", ">=", "&&", "||",
            "++", "--", "+=", "-=", "*=", "/=", "%=",
            "/*", "*/", "//", "/**"

    };
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private CompanyService companyService;

    /** allowed: Unicode letters, digits, space, plus, underscore, period, comma, colon, semicolon, ampersand, percent, slash, parentheses, hyphen*/
    public boolean isFieldValid(String field){
        String VALID_PATTERN = "^[\\p{L}0-9 +_.,:;&%/()-]*$";
        if(!field.matches(VALID_PATTERN)){throw new RuntimeException("Field:{ " + field + " } is not valid. ");}
        return true;
    }


    public boolean isWearableValid(WearableModel item){
        if(item.getName().isBlank() || item.getName().length() > 200){throw new RuntimeException("Product name cannot be over 50 chars long nor be empty");}
        if(item.getDescription().isBlank() || item.getDescription().length() > 2500){throw new RuntimeException("Product description cannot be over 20000 chars long nor be empty");}
        if(item.getPrice() == null || item.getPrice() < 0){throw new RuntimeException("Product Price cannot be null");}

        if(item.getCompanyId().isBlank() ){throw new RuntimeException("The product MUST BE ASSOCIATED WITHA  COMPANY");}
        isFieldValid(item.getName());
        isFieldValid(item.getDescription());
        log("PRODUCT " + item.getName() + " is Valid");
        return true;
    }





    public boolean checkIfEntityExistsBeforeRegister(String username) {
        System.out.println("Username " + username);
        boolean userExists = userRepository.findByUsername(username).isPresent();
        boolean employerExist = employerRepository.findByUsername(username).isPresent();
        boolean staffExists = staffRepository.findByUsername(username).isPresent();

        return userExists || employerExist || staffExists;
    }

    public String generateNewIdForUser() {
        boolean doesUserIdExists = true;
        String randomId = UUID.randomUUID().toString();
        while (doesUserIdExists) {
            doesUserIdExists = userRepository.findById(randomId).isPresent();
            randomId = UUID.randomUUID().toString();
        }
        return randomId;
    }

    public String generateNewIdForTask() {
        boolean doesTaskIdExists = true;
        String randomId = UUID.randomUUID().toString();
        while (doesTaskIdExists) {
            doesTaskIdExists = taskRepository.findById(randomId).isPresent();
            randomId = UUID.randomUUID().toString();
        }
        return randomId;
    }

    public String generateNewIdForCartItem() {
        boolean doesCartIdExists = true;
        String randomId = UUID.randomUUID().toString();
        while (doesCartIdExists) {
            doesCartIdExists = cartItemRepository.findByItemId(randomId).isPresent();
            randomId = UUID.randomUUID().toString();
        }
        return randomId;
    }

    public String generateNewIdForEmployer() {
        boolean doesIdExists = true;
        String randomId = UUID.randomUUID().toString();
        while (doesIdExists) {
            doesIdExists = employerRepository.findById(randomId).isPresent();
            randomId = UUID.randomUUID().toString();
        }
        return randomId;
    }

    public String generateNewIdForStaff() {
        boolean doesIdExists = true;
        String randomId = UUID.randomUUID().toString();
        while (doesIdExists) {
            doesIdExists = staffRepository.findById(randomId).isPresent();
            randomId = UUID.randomUUID().toString();
        }
        return randomId;
    }





}
