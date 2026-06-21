package dev.hope.vulcaobackend.Services;


import dev.hope.vulcaobackend.Repositories.EmployerRepository;
import dev.hope.vulcaobackend.Repositories.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {


    @Autowired
    private EmployerRepository employerRepository;
    @Autowired
    private StaffRepository staffRepository;



    public static final CharSequence[] SYMBOLS = {
            "[", "]", "{", "}", ",", "=", "...",
            "@", "#", "$", "%", "^", "&", "*", "(", ")", "!",
            "/", "\\", "|", ":", ";", "?", "<", ">",
            "~", "`", "'", "\"",
            "->", "=>", "::", "!=", "==", "<=", ">=", "&&", "||",
            "++", "--", "+=", "-=", "*=", "/=", "%=",
            "/*", "*/", "//", "/**"

    };

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
    public static ResponseEntity<?> validateEmail(String email){
        if(email == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("O email não pode estar vazio");
        }
        if(email.length() < 5 || email.length() > 50) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("O email deve ter entre 5 e 50 caracteres");
        }
        for (int i = 0; i < email.length(); i++) {
            int cp = email.codePointAt(i);
            if (Character.isWhitespace(cp) || Character.isSpaceChar(cp)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("O email não pode ter espaços");
            }
        }
        if(email.contains("script") || email.contains("alert")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Email inválido");
        }

        String[] emailParts = email.split("@");
        if(emailParts.length != 2) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("O email deve ter um @");
        }

        String username = emailParts[0];
        String tld = emailParts[1];
        System.out.println("NOME DE UTILIZADOR: " + username);
        System.out.println("DOMÍNIO: " + tld);

        if(tld.isEmpty() || username.isEmpty() ||
                tld.charAt(0)=='.' || tld.charAt(0)=='-' || tld.charAt(0)=='=' ||
                !tld.contains(".") ||
                username.charAt(0)=='.' || username.charAt(username.length()-1)=='.'){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Email inválido. Verifique se o formato está correto");
        }

        String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if(!email.matches(EMAIL_PATTERN)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Email inválido. Use apenas letras, números, ponto e traço");
        }

        for(int i=0; i < username.length()-1; i++){
            if(username.charAt(i)=='.' && username.charAt(i+1)=='.'){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("O email não pode ter dois pontos seguidos (..)");
            }
        }

        // Simplified emoji check
        String emojiRegex = "\\p{IsEmoji}";
        String extendedRegex = "\\p{IsExtended_Pictographic}";
        if(username.contains(emojiRegex) || username.contains(extendedRegex) ||
                tld.contains(emojiRegex) || tld.contains(extendedRegex)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("O email não pode ter emojis");
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    public ResponseEntity<?> validatePassword(String password) {
        if(password.length() < 8){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A palavra-passe tem que ter pelo menos 8 caracteres");
        }
        return ResponseEntity.accepted().build();

    }


    public ResponseEntity<?> validateUsername(String username) {
        System.out.println("Username validation check: "+ username);
        System.out.println("Length: " + username.length());

        if (username == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("O nome de utilizador não pode estar vazio");
        }
        if(username.length() < 8 || username.length() > 50){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("O nome de utilizador deve ter entre 8 e 50 caracteres");
        }

        String normalized = java.text.Normalizer.normalize(username, java.text.Normalizer.Form.NFKC);
        String NAME_PATTERN = "^[a-zA-Z0-9._-]+$";
        if (!normalized.matches(NAME_PATTERN)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("O nome de utilizador só pode ter letras, números, ponto, traço e underscore (_)");
        }

        int codepointCount = normalized.codePointCount(0, normalized.length());
        if (codepointCount < 5 || codepointCount > 200) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("O nome de utilizador deve ter entre 5 e 200 caracteres");
        }

        for (int i = 0; i < normalized.length(); ) {
            int cp = normalized.codePointAt(i);

            if (cp == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("O nome de utilizador não pode ter caracteres especiais");
            }
            if (Character.isWhitespace(cp) || Character.isSpaceChar(cp)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("O nome de utilizador não pode ter espaços");
            }

            int type = Character.getType(cp);
            if (type == Character.FORMAT || type == Character.CONTROL ||
                    type == Character.SURROGATE || type == Character.UNASSIGNED) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("O nome de utilizador tem caracteres inválidos");
            }
            if (cp < 128 && INVALID_CHARS.contains((char) cp)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("O nome de utilizador tem caracteres inválidos");
            }

            i += Character.charCount(cp);
        }

        if (RESERVED_NAMES.contains(normalized.toLowerCase(java.util.Locale.ROOT))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Este nome de utilizador não está disponível");
        }

        if (normalized.contains("..")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("O nome de utilizador não pode ter dois pontos seguidos");
        }

        if (EMOJI_PATTERN.matcher(normalized).find()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("O nome de utilizador não pode ter emojis");
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
    public ResponseEntity<?> validateName(String firstName, String lastName){
        if (firstName.isBlank() || firstName.length() > 100){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "O primeiro nome deve ter no máximo 100 caracteres"));
        }
        for (CharSequence symbol : SYMBOLS) {
            if (firstName.contains(symbol)) {
                System.out.println("Rejected by symbol: [" + symbol + "]");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "O primeiro nome só pode ter letras, números e underscore (_)"));
            }
        }

        if (lastName.isBlank() || lastName.length() > 100){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "O último nome deve ter no máximo 100 caracteres"));
        }
        for (CharSequence symbol : SYMBOLS) {
            if (lastName.contains(symbol)) {
                System.out.println("Rejected by symbol: [" + symbol + "]");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "O último nome só pode ter letras, números e underscore (_)"));
            }
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }


    public ResponseEntity<?> isDataValid(String field) {
        // Check if only European characters are allowed
        if (field == null || !field.matches("^[\\p{L}\\p{M}\\p{N}._\\- ]+$")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Use apenas letras, números, ponto, traço, underscore e espaços");
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    public String determineRole(String username) {
        if (employerRepository.findByUsername(username).isPresent()) return "EMPLOYER";
        if (staffRepository.findByUsername(username).isPresent())  return "STAFF";
        return "USER";
    }

}
