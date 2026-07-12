package dev.hope.vulcaobackend.Services;

import dev.hope.vulcaobackend.Models.FeedbackModel;
import dev.hope.vulcaobackend.Repositories.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {
    @Autowired
    FeedbackRepository feedbackRepository;
    @Autowired
    private GlobalServices globalServices;

    public void save(FeedbackModel feedback){
        System.out.println("Feedback Email: " + feedback.getEmail());
        Validators.validateFeedback(feedback);
           String id =  globalServices.generateFeedbackId();
           feedback.setId(id);
        feedbackRepository.save(feedback);
    }

}
