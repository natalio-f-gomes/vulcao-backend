package dev.hope.vulcaobackend.Services;


import dev.hope.vulcaobackend.Models.AccountEntities.AuthModel;
import dev.hope.vulcaobackend.Models.AccountEntities.EmployerModel;
import dev.hope.vulcaobackend.Models.AccountEntities.StaffModel;
import dev.hope.vulcaobackend.Repositories.EmployerRepository;
import dev.hope.vulcaobackend.Repositories.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuthService {


    @Autowired
    private EmployerRepository employerRepository;
    @Autowired
    private StaffRepository staffRepository;





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




    public void validateUsername(String username) {
        System.out.println("Username validation check: "+ username);
        System.out.println("Length: " + username.length());
        if (username == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }
        if(username.length() < 8 || username.length() > 50){
            throw new RuntimeException("Username must be between 8 and 50 characters long");
        }

        // Normalize first so equivalent forms compare equal
        String normalized = java.text.Normalizer.normalize(username, java.text.Normalizer.Form.NFKC);
        String NAME_PATTERN = "^[a-zA-Z0-9._-]+$";
        if (!normalized.matches(NAME_PATTERN)) {
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
