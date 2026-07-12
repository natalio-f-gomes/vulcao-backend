package dev.hope.vulcaobackend.RestControllers.Public;

import dev.hope.vulcaobackend.Configuration.AWSSESConfig;
import dev.hope.vulcaobackend.Models.FeedbackModel;
import dev.hope.vulcaobackend.Models.UserModel;
import dev.hope.vulcaobackend.Repositories.FeedbackRepository;
import dev.hope.vulcaobackend.Repositories.UserRepository;
import dev.hope.vulcaobackend.Services.FeedbackService;
import dev.hope.vulcaobackend.Services.MFAService;
import dev.hope.vulcaobackend.Services.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/v2/public/feedback")
public class FeedbackController {

    @Autowired
    MFAService mfaService;
    AWSSESConfig sesService;
    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FeedbackRepository feedbackRepository;

    public FeedbackController(AWSSESConfig sesService) {
        this.sesService = sesService;
    }

    @PostMapping("/request")
    public ResponseEntity<?> feedbackRequest( @RequestBody FeedbackModel feedback){
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        Optional<UserModel> user = userRepository.findByUsername(sessionUser);
        if(user.isEmpty()){
            System.out.println("The user is empty");
            Validators.validateEmail(feedback.getEmail());
            String randomCode = mfaService.save(feedback.getEmail());
            sesService.sendCode(feedback.getEmail(), randomCode);
            return ResponseEntity.accepted().build();
        }else{
            UserModel userModel = user.get();
            feedback.setEmail(userModel.getEmail());
            feedback.setName(userModel.getFirstName() + " " + userModel.getLastName());
            feedbackService.save(feedback);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirm/{mfaCode}")
    public ResponseEntity<?> feedbackConfirm(@PathVariable String mfaCode, @RequestBody FeedbackModel feedback){
        mfaService.confirmMFA(feedback.getEmail(), mfaCode);
        mfaService.deleteMFA(feedback.getEmail());
        feedbackService.save(feedback);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get/all")
    public ResponseEntity<?> getAll(){

        return ResponseEntity.ok(feedbackRepository.findAll());
    }



}
