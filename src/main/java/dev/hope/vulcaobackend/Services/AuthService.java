package dev.hope.vulcaobackend.Services;


import dev.hope.vulcaobackend.Models.AccountEntities.AuthModel;
import dev.hope.vulcaobackend.Models.AccountEntities.EmployerModel;
import dev.hope.vulcaobackend.Models.AccountEntities.StaffModel;
import dev.hope.vulcaobackend.Models.AccountEntities.UserModel;
import dev.hope.vulcaobackend.Repositories.AuthRepository;
import dev.hope.vulcaobackend.Repositories.EmployerRepository;
import dev.hope.vulcaobackend.Repositories.StaffRepository;
import dev.hope.vulcaobackend.Repositories.UserRepository;
import jakarta.servlet.http.Cookie;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.IllegalFormatPrecisionException;
import java.util.List;

@Service
public class AuthService {

    @Autowired
    AuthRepository authRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmployerRepository employerRepository;
    @Autowired
    private StaffRepository staffRepository;

    public void loginUser(UserModel user, String userIpAddress, String accessToken){
        AuthModel userLoginData = new AuthModel();
        userLoginData.setAccessToken(accessToken);
        userLoginData.setIpAddress(userIpAddress);
        userLoginData.setEmail(user.getEmail());
        userLoginData.setUsername(user.getUsername());
        userLoginData.setIssuedAt(LocalDateTime.now());
        authRepository.save(userLoginData);
    }

    public void logoutUser(String username, String userIpAddress) throws AccessDeniedException {
        UserModel userLoggedIn = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Username "+ username + " not found"));
        List<AuthModel> isUserLoggedInByUsername = authRepository.findByUsername(username);
        List<AuthModel> userIpAddresses = authRepository.findByUserIpAddress(userIpAddress);
        if(isUserLoggedInByUsername.isEmpty() || userIpAddresses.isEmpty() ){throw new AccessDeniedException("You do not appear to be logged in");}
        boolean valid = doesTheIpAddressMatch(userIpAddresses, userIpAddress);
        if(!valid){throw new AccessDeniedException("You do not appear to be logged in");}
        authRepository.deleteUserLoginData(username);
    }
    public boolean canUserPerformAction(UserModel userLoggedIn, String ipAddress, Cookie[] cookies) throws AccessDeniedException {
        System.out.println("DEBUG: Username: " + userLoggedIn.getUsername());
        System.out.println("DEBUG: IP Address: " + ipAddress);
        System.out.println("DEBUG: Access token: " + cookies[0].getValue());
        List<AuthModel> loginData =  authRepository.findByUsername(userLoggedIn.getUsername());
        System.out.println("Is login data empty: "+loginData.isEmpty());
        for(AuthModel login: loginData){
            System.out.println("AUTH MODEL: IpAddress "+ login.getIpAddress() + " Token "+  cookies[0].getValue());
            if(login.getIpAddress().equals(ipAddress) && login.getAccessToken().equals(cookies[0].getValue())){
                return true;
            }
        }
        throw new AccessDeniedException("YOU DO NOT HAVE PERMISSIONS TO DO THIS ACTION");
    }

    public boolean canEmployerPerformAction(UserModel userLoggedIn, String ipAddress, Cookie[] cookies) throws AccessDeniedException {
        System.out.println("DEBUG: Username: " + userLoggedIn.getUsername());
        System.out.println("DEBUG: IP Address: " + ipAddress);
        System.out.println("DEBUG: Access token: " + cookies[0].getValue());
        List<AuthModel> loginData =  authRepository.findByUsername(userLoggedIn.getUsername());
        System.out.println("Is login data empty: "+loginData.isEmpty());
        for(AuthModel login: loginData){
            System.out.println("AUTH MODEL: IpAddress "+ login.getIpAddress() + " Token "+  cookies[0].getValue());
            if(login.getIpAddress().equals(ipAddress) && login.getAccessToken().equals(cookies[0].getValue())){
                return true;
            }
        }
        throw new AccessDeniedException("YOU DO NOT HAVE PERMISSIONS TO DO THIS ACTION");
    }


    public boolean doesTheIpAddressMatch(List<AuthModel> entityLoggedInIpAddresses, String userIpAddress){
        boolean valid = false;
        for(AuthModel auth: entityLoggedInIpAddresses){
            if(auth.getIpAddress().equals(userIpAddress))
            {   valid=true;
                break;
            }
        }
        return valid;
    }

    public void loginEmployer(EmployerModel employerLoggedIn, String userIpAddress, String accessToken){
        AuthModel userLoginData = new AuthModel();
        userLoginData.setAccessToken(accessToken);
        userLoginData.setIpAddress(userIpAddress);
        userLoginData.setUsername(employerLoggedIn.getUsername());
        userLoginData.setEmail(employerLoggedIn.getEmail());
        userLoginData.setIssuedAt(LocalDateTime.now());
        authRepository.save(userLoginData);
    }

    public void logoutEmployer(String username, String userIpAddress){
        EmployerModel employerLoggedIn = employerRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Username "+ username + " not found"));
        List<AuthModel> isUserLoggedInByUsername = authRepository.findByUsername(username);
        List<AuthModel> employerIpAddresses = authRepository.findByUserIpAddress(userIpAddress);
        if(isUserLoggedInByUsername.isEmpty() ){throw new org.springframework.security.access.AccessDeniedException("NOT AUTHORIZED TO LOGOUT GIVEN THAT YOU ARE NOT LOGGED IN");}
        boolean valid = doesTheIpAddressMatch(employerIpAddresses, userIpAddress);
        if(!valid){
            {throw new org.springframework.security.access.AccessDeniedException("YOUR IP ADDRESS SHOWS THAT YOU ARE NOT LLOGED IN");}
        }
        authRepository.deleteUserLoginData(username);
    }

    public void loginStaff(StaffModel staffLoggedIn, String userIpAddress, String accessToken){
        AuthModel userLoginData = new AuthModel();
        userLoginData.setAccessToken(accessToken);
        userLoginData.setIpAddress(userIpAddress);
        userLoginData.setUsername(staffLoggedIn.getUsername());
        userLoginData.setEmail(staffLoggedIn.getEmail());
        userLoginData.setIssuedAt(LocalDateTime.now());
        authRepository.save(userLoginData);
    }

    public void logoutStaff(String username, String userIpAddress) throws AccessDeniedException {
        StaffModel staff = staffRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Username "+ username + " not found"));
        List<AuthModel> isStaffLoggedInByUsername = authRepository.findByUsername(username);
        List<AuthModel> staffIpAddress = authRepository.findByUserIpAddress(userIpAddress);
        if(isStaffLoggedInByUsername.isEmpty() ){throw new AccessDeniedException("");}
        boolean valid = doesTheIpAddressMatch(staffIpAddress, userIpAddress);
        if(!valid){
            {throw new AccessDeniedException("");}
        }
        authRepository.deleteUserLoginData(username);
    }

    private static final java.util.regex.Pattern EMOJI_PATTERN =
            java.util.regex.Pattern.compile("\\p{IsExtended_Pictographic}");

    private static final java.util.Set<Character> INVALID_CHARS = new java.util.HashSet<>(java.util.Arrays.asList(
            '#', '-', '\'', ';', '%', '@', '&', '^', '*', '`', '~', '\\', '/', ':',
            '<', '>', '(', ')', '=', '+', '!', '?', '"', ',', '{', '}', '[', ']', '|', '$'
    ));

    private static final java.util.Set<String> RESERVED_NAMES = new java.util.HashSet<>(java.util.Arrays.asList(
            "admin", "administrator", "root", "system", "support", "anonymous",
            "null", "undefined", "api", "postgres", "superuser", "guest"
    ));

    public void validateEmail(String email){
        if(email == null) { throw  new RuntimeException("Email cannot be null");}
        if(email.length()< 5 || email.length() > 50) {throw new IllegalArgumentException("Email must be over 5 characters long");}
        for (int i = 0; i < email.length(); i++) {
            int cp = email.codePointAt(i);
            if (Character.isWhitespace(cp) || Character.isSpaceChar(cp)) {
                throw new RuntimeException("Email cannot contain any whitespace");
            }
        }
        if(email.contains("script") || email.contains("alert")){
            throw new RuntimeException("Invalid Email");
        }
        String[] emailParts = email.split("@");
        String username = emailParts[0];
        String tld = emailParts[1];
        System.out.println("USERNAME: " + username);
        System.out.println("TLD: " + tld);
        if(tld.isEmpty() || username.isEmpty() || tld.charAt(0)=='.' || tld.charAt(0)=='-' || tld.charAt(0)=='=' || !tld.contains(".") || username.charAt(0)=='.' || username.charAt(username.length()-1)=='.'){
            throw new RuntimeException("Both TLD and username must be specified and they cannot start with a dot(.). The Username cannot end with a .");
        }

        String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        if(!email.matches(EMAIL_PATTERN)){ throw new RuntimeException("Invalid Email pattern");}


        for(int i=0; i < username.length()-1; i++){

            if(username.charAt(i)=='.' && username.charAt(i+1)=='.'){
                throw new RuntimeException("Email cannot contain 2 consecutive ..");
            }
        }
        // Matches individual emoji characters and complex sequences
        String emojiRegex = "\\p{IsEmoji}";
        // OR for broader coverage including newer pictographs:
        String extendedRegex = "\\p{IsExtended_Pictographic}";
        if(username.contains(emojiRegex) || username.contains(extendedRegex) || tld.contains(emojiRegex) || tld.contains(extendedRegex)){
            throw new RuntimeException("Email cannot contain emoji");
        }
    }


    public boolean isValidUsername(String s) {
        if (s == null || s.length() < 8 || s.length() > 50) return false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            boolean ok = (c >= 'a' && c <= 'z')
                    || (c >= 'A' && c <= 'Z')
                    || (c >= '0' && c <= '9')
                    || c == '_' || c == '.' || c == '-';
            if (!ok) return false;
        }
        return true;
    }

    public void validateUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }
        if(!isValidUsername(username)){
            throw new RuntimeException("Username must be between 8 and 50 characters long");
        }

        // Normalize first so equivalent forms compare equal
        String normalized = java.text.Normalizer.normalize(username, java.text.Normalizer.Form.NFKC);

        if (!normalized.matches("[A-Za-z0-9_.-]+")) {
            throw new IllegalArgumentException("Username may only contain letters, digits, underscore, dot, and hyphen");
        }

        int codepointCount = normalized.codePointCount(0, normalized.length());
        if (codepointCount < 5 || codepointCount > 200) {
            throw new IllegalArgumentException("Username must be between 5 and 200 characters");
        }

        // Single pass over codepoints
        for (int i = 0; i < normalized.length(); ) {
            int cp = normalized.codePointAt(i);

            if (cp == 0) {
                throw new IllegalArgumentException("Username cannot contain null bytes");
            }
            if (Character.isWhitespace(cp) || Character.isSpaceChar(cp)) {
                throw new IllegalArgumentException("Username cannot contain whitespace");
            }
            // Zero-width / format / control characters (BOM, ZWSP, ZWJ, RTL override, etc.)
            int type = Character.getType(cp);
            if (type == Character.FORMAT || type == Character.CONTROL ||
                    type == Character.SURROGATE || type == Character.UNASSIGNED) {
                throw new IllegalArgumentException("Username contains a disallowed control or formatting character");
            }
            if (cp < 128 && INVALID_CHARS.contains((char) cp)) {
                throw new IllegalArgumentException("Username cannot contain '" + (char) cp + "'");
            }

            i += Character.charCount(cp);
        }

        if (RESERVED_NAMES.contains(normalized.toLowerCase(java.util.Locale.ROOT))) {
            throw new IllegalArgumentException("Username is reserved");
        }

        if (normalized.contains("..")) {
            throw new IllegalArgumentException("Username cannot contain consecutive dots");
        }

        if (EMOJI_PATTERN.matcher(normalized).find()) {
            throw new IllegalArgumentException("Username cannot contain emoji");
        }
    }
}
