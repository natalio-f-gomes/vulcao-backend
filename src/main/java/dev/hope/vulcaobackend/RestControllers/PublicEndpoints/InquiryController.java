package dev.hope.vulcaobackend.RestControllers.PublicEndpoints;

import dev.hope.vulcaobackend.Configuration.AWSSESConfig;
import dev.hope.vulcaobackend.DataTransferObjects.ResetPasswordRequest;
import dev.hope.vulcaobackend.Models.AccountEntities.UserModel;
import dev.hope.vulcaobackend.Models.InquiryModel;
import dev.hope.vulcaobackend.Repositories.InquiryRepository;
import dev.hope.vulcaobackend.Services.AuthService;
import dev.hope.vulcaobackend.Services.CompanyService;
import dev.hope.vulcaobackend.Services.MFAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/public/inquiry")
public class InquiryController {

    @Autowired
    InquiryRepository inquiryRepository;
    @Autowired
    CompanyService companyService; // use for validation
    @Autowired
    MFAService mfaService;

    AWSSESConfig sesService;
    @Autowired
    private AuthService authService;

    public InquiryController(AWSSESConfig sesService) {
        this.sesService = sesService;
    }

    @PostMapping("/post/request/{email}")
    public ResponseEntity postInquiryRequest(@PathVariable String email) {
        ResponseEntity<?> response = companyService.isCompanyEmailValid(email);
        if (!response.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("E-mail inválido. Por favor, insira um e-mail válido!");
        }
        String randomCode = mfaService.save(email);
        sesService.sendCode(email, randomCode);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Enviámos um código de verificação para o seu e-mail.");
    }


    @PostMapping("/post/confirm/{mfaCode}")
    public ResponseEntity<?> postInquiryConfirm( @PathVariable String mfaCode, @RequestBody InquiryModel inquiry) {
        System.out.println("Inquiry Confirm endpoint reached");
        ResponseEntity<?> emailResponse = authService.validateEmail(inquiry.getUserEmail());
        if(!emailResponse.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("E-mail inválido. Por favor, insira um e-mail válido!");
        }

        ResponseEntity<?> mfaResponse = mfaService.confirmMFA(inquiry.getUserEmail(), mfaCode);
        if(!mfaResponse.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return mfaResponse;
        }

        if(!companyService.isCompanyNameValid(inquiry.getUserName()).getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O nome é inválido. Por favor, insira um nome válido!");
        }

        if(!companyService.isCompanyDescriptionValid(inquiry.getDescription()).getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A descriçao é inválida. Por favor, insira uma descriçao válida!");
        }
        inquiryRepository.save(inquiry);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/get/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(inquiryRepository.findAll());

    }
}
