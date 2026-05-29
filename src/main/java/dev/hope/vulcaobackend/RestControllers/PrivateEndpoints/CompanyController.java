package dev.hope.vulcaobackend.RestControllers.PrivateEndpoints;


import dev.hope.vulcaobackend.DataTransferObjects.CompanyAboutInfo;
import dev.hope.vulcaobackend.DataTransferObjects.CompanyContact;
import dev.hope.vulcaobackend.DataTransferObjects.CompanyDTO;
import dev.hope.vulcaobackend.Models.Common.Address;
import dev.hope.vulcaobackend.Models.CompanyModel;
import dev.hope.vulcaobackend.Repositories.CompanyRepository;
import dev.hope.vulcaobackend.Services.CompanyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/company")
public class CompanyController {

    @Autowired private CompanyService companyService;
    @Autowired
    private CompanyRepository companyRepository;

    public String generateNewIdForCompany() {
        boolean doesUserIdExists = true;
        String randomId = UUID.randomUUID().toString();
        while (doesUserIdExists) {
            doesUserIdExists = companyRepository.findById(randomId).isPresent();
            randomId = UUID.randomUUID().toString();
        }
        return randomId;
    }



    @PostMapping("/register")
    public ResponseEntity<?> registerBusiness(@Valid @RequestBody CompanyModel newBusiness, HttpServletRequest request){
        try{
            String id = generateNewIdForCompany();
            newBusiness.setId(id);
            companyService.registerBusiness(newBusiness,request);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (AccessDeniedException e){
            System.out.println("ERROR: " + e.getClass().getName() + " - " + e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }catch (ResponseStatusException e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBusiness(@RequestBody CompanyDTO updatedBusiness, @PathVariable String id){
        companyService.updateCompany(id, updatedBusiness);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/update/address/{id}")
    public ResponseEntity<?> updateBusinessAddress(@RequestBody Address newAddress, @PathVariable String id){
        companyService.updateCompanyAddress(id, newAddress);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/update/description/{id}")
    public ResponseEntity<?> updateBusinessDescription(@RequestBody CompanyAboutInfo info, @PathVariable String id){
        companyService.updateCompanyAboutInfo(id, info);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/update/contact/{id}")
    public ResponseEntity<?> updateBusinessContact(@RequestBody CompanyContact newContact, @PathVariable String id){
        companyService.updateCompanyContact(id, newContact);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
