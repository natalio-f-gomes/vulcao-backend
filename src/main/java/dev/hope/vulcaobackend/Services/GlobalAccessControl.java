package dev.hope.vulcaobackend.Services;


import dev.hope.vulcaobackend.Models.Offerings.ProductModel;
import dev.hope.vulcaobackend.Repositories.AuthRepository;
import dev.hope.vulcaobackend.Repositories.Cart.CartRepository;
import dev.hope.vulcaobackend.Repositories.EmployerRepository;
import dev.hope.vulcaobackend.Repositories.StaffRepository;
import dev.hope.vulcaobackend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

import static java.rmi.server.LogStream.log;

@Service
public class GlobalAccessControl {

    @Autowired
    private AuthRepository authRepository;
    @Autowired
    StaffRepository staffRepository;
    @Autowired
    EmployerRepository employerRepository;
    @Autowired
    AuthService authService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;


    public static final CharSequence[] SYMBOLS = {
            "[", "]", "{", "}", ",", "=", "...",
            "@", "#", "$", "%", "^", "&", "*", "(", ")", "!",
            "/", "\\", "|", ":", ";", "?", "<", ">",
            "~", "`", "'", "\"", "_",
            "->", "=>", "::", "!=", "==", "<=", ">=", "&&", "||",
            "++", "--", "+=", "-=", "*=", "/=", "%=",
            "/*", "*/", "//", "/**"
    };
    public boolean isFieldValid(String field){
        String VALID_PATTERN = "^[\\p{L}0-9 +_.,:;&%/()-]*$";
        if(!field.matches(VALID_PATTERN)){throw new RuntimeException("Field:{ " + field + " } is not valid. ");}
        return true;
    }

    public boolean isProductValid(ProductModel product){
        if(product.getName().isBlank() || product.getName().length() > 200){throw new RuntimeException("Product name cannot be over 50 chars long nor be empty");}
        if(product.getDescription().isBlank() || product.getDescription().length() > 2500){throw new RuntimeException("Product description cannot be over 20000 chars long nor be empty");}
        if(product.getPricePerUnit() == null || product.getPricePerUnit() < 0){throw new RuntimeException("Product Price cannot be null");}
        if(product.getUnitType() == null || product.getCurrency() == null || product.getStock() == null ||product.getStock() <0 )
        {throw new RuntimeException("Product Unit type, currency, and stock cannot be null");}
        if(product.getExpirationDate() == null) {throw new RuntimeException("Product Unit type, currency, and stock cannot be null");}
        if(product.getProductType().isPerishable() && product.getExpirationDate().isBefore(LocalDate.now())){throw new RuntimeException("The product cannot be expired");}
        if(product.getProductType().isFrozen() && product.getExpirationDate().isBefore(LocalDate.now())
                || product.getExpirationDate().getYear() > LocalDate.now().getYear() + 20){throw new RuntimeException("The product cannot be expired");}
        if(product.getCompanyName().isBlank() || product.getCompanyId().isBlank()){throw new RuntimeException("The product MUST BE ASSOCIATED WITHA  COMPANY");}
        isFieldValid(product.getName());
        isFieldValid(product.getDescription());

        log("PRODUCT " + product.getName() + " is Valid");
        return true;

    }

    public boolean checkIfEntityExistsBeforeRegister(String username, String email) {
        System.out.println("Username " + username + " email: " + email);
        boolean userExists = userRepository.findByUsername(username).isPresent() ||
                userRepository.findByEmail(email).isPresent();
        boolean employerExist = employerRepository.findByUsername(username).isPresent() ||
                employerRepository.findByEmail(email).isPresent();
        boolean staffExists = staffRepository.findByUsername(username).isPresent() ||
                staffRepository.findByEmail(email).isPresent();

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
    public String generateNewIdForCart() {
        boolean doesCartIdExists = true;
        String randomId = UUID.randomUUID().toString();
        while (doesCartIdExists) {
            doesCartIdExists = cartRepository.findById(randomId).isPresent();
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





    public boolean isDataValid(String... text) {
        for (String t : text) {
            if (t == null || !t.matches("^[\\p{L}\\p{M}\\p{N}._\\- ]+$")) {
                return false;
            }
        }
        return true;
    }

    public boolean isRequestValid(String username, String email, String password, String firstName, String lastName) {
        System.out.println("Username " + username + " email: " + email + " password " + password);
        if (username.isBlank() || email.isBlank() || password.isBlank() || !email.contains("@") || firstName.length() > 100 || lastName.length() > 100 ||
                (username.length() < 3) || username.length() > 20 || password.length() < 8 || password.length() > 50 || (password.strip().length() != password.length() )) return false;
        CharSequence[] symbols = {
                "[", "]", "{", "}", ",", "=", " ",
                "...",
                "@", "#", "$", "%", "^", "&", "*", "(", ")", "!",
                "+", "-", "/", "\\", "|", ":", ";", "?", "<", ">",
                "~", "`", "'", "\"", "\n",
                "->", "=>", "::", "!=", "==", "<=", ">=", "&&", "||",
                "++", "--", "+=", "-=", "*=", "/=", "%=",
                "/*", "*/", "//", "/**"
        };
        System.out.println("Received username: [" + username + "]");
        System.out.println("Length: " + username.length());
        System.out.println("Bytes (UTF-8): " + Arrays.toString(username.getBytes(StandardCharsets.UTF_8)));

        for (CharSequence symbol : symbols) {
            if (username.contains(symbol)) {
                System.out.println("Rejected by symbol: [" + symbol + "]");
                return false;
            }
        }
        return true;
    }
    public String determineRole(String username) {
        if (employerRepository.findByUsername(username).isPresent()) return "EMPLOYER";
        if (staffRepository.findByUsername(username).isPresent())  return "STAFF";
        return "USER";
    }

}
