package dev.hope.vulcaobackend.Authentication;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class CookieUtil {
    //This is for all accounts
    public ResponseCookie createCookie(String token){
        return ResponseCookie.from("AUTH_TOKEN", token)
                .httpOnly(true)
                .secure(false) // https if true, needs to be changed when deployed
                .path("/")
                .maxAge(Duration.ofDays(1))
                .sameSite("Strict")
                .build();
    }

    public ResponseCookie clearCookie(){
        return ResponseCookie.from("AUTH_TOKEN", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();
    }





}
