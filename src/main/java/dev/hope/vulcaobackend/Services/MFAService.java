package dev.hope.vulcaobackend.Services;

import dev.hope.vulcaobackend.Models.MFAModel;
import dev.hope.vulcaobackend.Repositories.MfaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class MFAService {

    @Autowired private MfaRepository mfaRepository;

    private final SecureRandom secureRandom = new SecureRandom();

    public String generateRandomCode(){
        int code = secureRandom.nextInt(100_000) + 900_000;
        System.out.println("Random Code: "+ code);
        return String.valueOf(code);
    }

    public String save(String email){
        String randomCode = generateRandomCode();
        MFAModel mfaData = new MFAModel();
        mfaData.setAuthCode(randomCode);
        mfaData.setUserEmail(email);
        mfaData.setSendTime(LocalDateTime.now());
        mfaData.setExpirationTime(LocalDateTime.now().plusMinutes(15));
        mfaRepository.save(mfaData);
        return randomCode;
    }


    public ResponseEntity<?> confirmMFA(String email, String code){
        MFAModel mfa = mfaRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("User email not found: " + email));

        if(!mfa.getAuthCode().equals(code)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Wrong MFA CODE.");
        }

        LocalDateTime sentTime = mfa.getSendTime();
        LocalDateTime receivedTime = LocalDateTime.now();
        Duration duration = Duration.between(sentTime, receivedTime);
        if(duration.toMinutes() > 15){
            mfaRepository.deleteMFAData(mfa);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("THIS CODE IS EXPIRED. PLEASE REQUEST A NEW ONE");
        }

        return ResponseEntity.ok("Authorized");
    }

    // Add a method to delete after successful registration
    public void deleteMFA(String email) {
        System.out.println("MFA DELETE POINT REACHED");
        MFAModel mfaData = mfaRepository.findByUserEmail(email).orElseThrow(()-> new RuntimeException("MFA not found for: " + email));
        mfaRepository.deleteMFAData(mfaData);
        System.out.println("MFA SUCCESSFULLY DELETED");
    }
}
