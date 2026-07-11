package dev.hope.vulcaobackend.Services;

import dev.hope.vulcaobackend.Authentication.CookieUtil;
import dev.hope.vulcaobackend.Models.UserModel;
import dev.hope.vulcaobackend.Properties.OfferingType;
import dev.hope.vulcaobackend.Properties.UserType;
import dev.hope.vulcaobackend.Repositories.BusinessRepository;
import dev.hope.vulcaobackend.Repositories.FeedbackRepository;
import dev.hope.vulcaobackend.Repositories.OfferingRepository;
import dev.hope.vulcaobackend.Repositories.UserRepository;
import dev.hope.vulcaobackend.Services.ExceptionHandler.ApiException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;
import java.util.UUID;


@Service
public class GlobalServices {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private BusinessRepository businessRepository;
    @Autowired
    private OfferingRepository offeringRepository;
    @Autowired
    private FeedbackRepository feedbackRepository;


    public String generateUserId(UserType type){
        String id = type.toString().toLowerCase() + "-" + UUID.randomUUID();
        while(userRepository.findById(id).isPresent()){
            id =  type + "-" + UUID.randomUUID();
        }
        return id;
    }
    public String generateBusinessId(){
        String id = "business-" +  UUID.randomUUID();
        while(businessRepository.findById(id).isPresent()){
            id =  "business-" + UUID.randomUUID();
        }
        return  id;
    }

    public String generateOfferingId(OfferingType offeringType){
        String id = offeringType.toString().toLowerCase() + "-"  +  UUID.randomUUID();
        while(offeringRepository.findById(id).isPresent()){
            id = offeringType.toString().toLowerCase() + "-"  +  UUID.randomUUID();
        }
        return  id;
    }

    public String generateFeedbackId(){
        String id = "feedback-" +  UUID.randomUUID();
        while(feedbackRepository.findById(id).isPresent()){
            id =  "feedback-" + UUID.randomUUID();
        }
        return  id;
    }




    public void isUsernameTaken(String username){
        if(userRepository.findByUsername(username).isPresent()){
            throw new ApiException(HttpStatus.CONFLICT, "Este nome de usuário já está em uso.Por favor escolhe um nome the usuário differente");
        }
    }

    public UserModel getClientByEmail(String email){
        UserModel user = userRepository.findByEmail(email).orElseThrow(
                () -> new ApiException(HttpStatus.NOT_FOUND,"Este e-mail não está associado a nenhum cliente." )
        );
        if(!user.getUserType().equals(UserType.CLIENT.toString())){
            throw new ApiException(HttpStatus.NOT_FOUND,"Este e-mail não está associado a nenhum cliente." );
        }
        return user;
    }

    public UserModel getEmployerByEmail(String email){
        UserModel user = userRepository.findByEmail(email).orElseThrow(
                () -> new ApiException(HttpStatus.NOT_FOUND,"Este e-mail não está associado a nenhum cliente." )
        );
        if(!user.getUserType().equals(UserType.EMPLOYER.toString())){
            throw new ApiException(HttpStatus.NOT_FOUND,"Este e-mail não está associado a nenhum cliente." );
        }
        return user;
    }

    public UserModel getEmployerByUsername(String username){
        UserModel user = userRepository.findByUsername(username).orElseThrow(
                () -> new ApiException(HttpStatus.NOT_FOUND,"Este nome de usuário não está associado a nenhum cliente." )
        );
        if(!user.getUserType().equals(UserType.EMPLOYER.toString())){
            throw new ApiException(HttpStatus.NOT_FOUND,"Este nome de usuário não está associado a nenhum cliente." );
        }
        return user;
    }

    public UserModel validateAuthorization(String username){
        UserModel user = userRepository.findByUsername(username).orElseThrow(
                () -> new ApiException(HttpStatus.NOT_FOUND,"Este nome de usuário não está associado a nenhum usuario." )
        );
        if(!user.getUserType().equals(UserType.EMPLOYER.toString()) && !user.getUserType().equals(UserType.STAFF.toString())){
            throw new ApiException(HttpStatus.FORBIDDEN,  "Você não tem permissão para adicionar produtos.");
        }
        return user;
    }

    public void deleteCookie(HttpServletResponse response,CookieUtil cookieUtil ){
        response.addHeader(HttpHeaders.SET_COOKIE,cookieUtil.clearCookie().toString());
        SecurityContextHolder.clearContext();
    }

    public String convertDateTime(String input){
        ZonedDateTime dateTime = ZonedDateTime.parse(input);
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("d 'de' MMMM 'DE' yyyy")
                .appendLiteral(" AS ")
                .appendPattern("HH:mm")
                .toFormatter(new Locale("pt", "PT"));

        return dateTime.format(formatter).toUpperCase();
    }

}
