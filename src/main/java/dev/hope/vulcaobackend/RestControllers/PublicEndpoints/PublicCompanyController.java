package dev.hope.vulcaobackend.RestControllers.PublicEndpoints;


import dev.hope.vulcaobackend.DataTransferObjects.CompanyDTO;
import dev.hope.vulcaobackend.Repositories.CompanyRepository;
import dev.hope.vulcaobackend.Services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/public/company")
public class PublicCompanyController {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping("/get/all")
    public List<CompanyDTO> getAllCompanies(){
        return companyService.findAllCompanies();
    }

    @GetMapping("/get/companyName")
    public CompanyDTO getAllCompanyByName(@RequestParam String companyName){
        return companyService.findCompanyByName(companyName);
    }

    @GetMapping("/get/creatorId")
    public List<CompanyDTO> getAllCompaniesByCreatorUsername(@RequestParam String creatorId){
        return companyService.findCompanyByCreatorId(creatorId);
    }


    @GetMapping("/get/{id}")
    public CompanyDTO getBusinessById(@PathVariable String id){
        return companyService.findCompanyById(id);
    }

    @GetMapping("/get/byEmail/{email}")
    public CompanyDTO getBusinessByEmail(@PathVariable String email){
        return companyService.findCompanyByEmail(email);
    }
}


