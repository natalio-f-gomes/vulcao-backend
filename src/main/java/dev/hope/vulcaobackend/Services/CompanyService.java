package dev.hope.vulcaobackend.Services;

import dev.hope.vulcaobackend.DataTransferObjects.CompanyAboutInfo;
import dev.hope.vulcaobackend.DataTransferObjects.CompanyContact;
import dev.hope.vulcaobackend.DataTransferObjects.CompanyDTO;
import dev.hope.vulcaobackend.Mappers.CompanyMapper;
import dev.hope.vulcaobackend.Models.AccountEntities.EmployerModel;
import dev.hope.vulcaobackend.Models.Common.Address;
import dev.hope.vulcaobackend.Models.CompanyModel;
import dev.hope.vulcaobackend.Models.Properties.CompanyStatuses;
import dev.hope.vulcaobackend.Repositories.CompanyRepository;
import dev.hope.vulcaobackend.Repositories.EmployerRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CompanyService {
    @Autowired private CompanyRepository companyRepository;
    @Autowired private EmployerRepository employerRepository;
    @Autowired
    private CompanyMapper companyMapper;

    //    VALIDATION
    //    private String type;
    //    private Double revenue;
    //    private CompanyStatuses status;

    //    private String creatorUsername;
    public boolean isNameTaxIdAndDescriptionValid(String name, String taxId, String description){
        if(name == null){throw new RuntimeException("Company Name cannot be null");}
        String NAME_PATTERN = "^[\\p{L}0-9 !+_.-]+$";
        String TAX_PATTERN = "^[\\p{L}0-9 +_.-]*$";
        if(!name.matches(NAME_PATTERN)){
            throw new IllegalArgumentException("THE COMPANY NAME "+ name+ " must be from A-Z a-z 0-9 and can use european characters");
        }
        if(!taxId.matches(TAX_PATTERN)){
            throw new IllegalArgumentException("THE COMPANY TAX ID "+ taxId+ " must be from A-Z a-z 0-9 and can use european characters");
        }
        if(!description.matches(NAME_PATTERN)){
            throw new IllegalArgumentException("THE COMPANY DESCRIPTION "+ description+ " must be from A-Z a-z 0-9 and can use european characters");
        }
        return true;
    }
    public void isTypeValid(){}
    public boolean isPhoneNumberAndFaxNumberValid(String phoneNumber, String faxNumber) {
        if (phoneNumber == null) {throw new IllegalArgumentException("COMPANY PHONE NUMBER CANNOT BE NULL");}
        String FORMAT = "^[+]?[0-9\\- ]{7,15}$";
        if(!phoneNumber.matches(FORMAT)){throw new RuntimeException("COMPANY PHONE NUMBER DOES NOT MATCH A VALID PHONE NUMBER ");}
        if(!faxNumber.matches(FORMAT)){throw new RuntimeException("COMPANY FAX  NUMBER DOES NOT MATCH A VALID FAX NUMBER");}
        return true;

    }


    public boolean isEmailValid(String email){
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
            return true;
    }

    public boolean isAddressValid(Address address){
        if(address == null){throw new NullPointerException("COMPANY ADDRESS CANNOT BE NULL");}
        String NAME_PATTERN = "^[\\p{L}0-9 +_.-]*$";
        if(!address.getIsland().matches(NAME_PATTERN)){throw new IllegalArgumentException("COMPANY ISLAND DOES NOT HAVE A VALID FORMAT");}
        if(!address.getCity().matches(NAME_PATTERN)){throw new IllegalArgumentException("COMPANY city DOES NOT HAVE A VALID FORMAT");}
        if(!address.getZone().matches(NAME_PATTERN)){throw new IllegalArgumentException("COMPANY zone DOES NOT HAVE A VALID FORMAT");}
        if(!address.getStreet().matches(NAME_PATTERN)){throw new IllegalArgumentException("COMPANY street DOES NOT HAVE A VALID FORMAT");}
        if(!address.getPostalCode().matches(NAME_PATTERN)){throw new IllegalArgumentException("COMPANY postal code DOES NOT HAVE A VALID FORMAT");}
        return true;
    }
    public boolean isCompanyStatusValid(CompanyStatuses status){
        if(status == null){throw new IllegalArgumentException("COMPANY STATUS CANNOT BE NULL");}
        return true;
    }


    public boolean isCompanyValid(CompanyModel company){
        return isNameTaxIdAndDescriptionValid(company.getPhoneNumber(), company.getTaxIdNumber(), company.getDescription()) &&
                isPhoneNumberAndFaxNumberValid(company.getPhoneNumber(), company.getFaxNumber()) &&
                isAddressValid(company.getAddress()) && isEmailValid(company.getEmail()) && isCompanyStatusValid(company.getStatus());
    }

    public CompanyDTO findCompanyById(String companyID){
        CompanyModel company = companyRepository.findById(companyID)
                .orElseThrow (()  -> new RuntimeException("Company " + companyID + " not found"));
        return companyMapper.transformData(company);
    }

    public CompanyDTO findCompanyDashboard(String companyID){
        if(companyID==null){throw new NullPointerException("Access Denied: User not authorized");}
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        CompanyModel company = companyRepository.findById(companyID).orElseThrow(()-> new RuntimeException("A company with this Id could not be found"));
        boolean isEmployerPresent = employerRepository.findByUsername(sessionUser).isPresent();
//      boolean isStaffPresent = staffRepository.findByUsername(sessionUser).isPresent();
//      if(!isEmployerPresent && !isStaffPresent){
//          throw new UsernameNotFoundException("Access Denied: User not authorized");
//       }
       return companyMapper.transformData(company);
    }


    public CompanyDTO findCompanyByTaxIdNumber(String taxIdNumber){
        CompanyModel company = companyRepository.findByTaxId(taxIdNumber).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"BUSINESS WITH THIS ID NOT FOUND"));
        return companyMapper.transformData(company);
    }

    public List<CompanyDTO> findCompanyByCreatorUsername(String creator){
        return companyRepository.findByCreatorUsername(creator).stream().map(companyMapper::transformData).collect(Collectors.toList());
    }

    public List<CompanyDTO> findCompanyByCategory(String category){
        return companyRepository.findByCategory(category).stream().map(companyMapper::transformData).collect(Collectors.toList());
    }

    public CompanyDTO findCompanyByEmail(String email){
        CompanyModel company = companyRepository.findByEmail(email).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"BUSINESS WITH THIS EMAIL NOT FOUND"));
        return companyMapper.transformData(company);
    }

    public CompanyDTO findCompanyByName(String name){
        CompanyModel company = companyRepository.findByName(name).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"BUSINESS WITH THIS EMAIL NOT FOUND"));
        return companyMapper.transformData(company);
    }
    public List<CompanyDTO> findAllCompanies(){
        return companyRepository.findAll().stream().map(
                companyMapper::transformData
        ).collect(Collectors.toList());
    }


    public void deleteCompanyById(String id, String username){
        CompanyModel company = companyRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Company " + id + " not found"));
        String owner = company.getCreatorUsername();
        if (!owner.equals(username)) {
            throw new RuntimeException("Access Denied: You are not the owner of this company.");
        }
        companyRepository.deleteCompanyById(company);
        System.out.println("SUCCESS: Company deleted.");

    }

    public void updateCompany(String id, CompanyDTO updatedCompany){
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        EmployerModel employer = employerRepository.findByUsername(sessionUser).orElseThrow(() -> new UsernameNotFoundException("This executive does not exists"));
        CompanyModel company = companyRepository.findById(id).orElseThrow(()-> new RuntimeException("A company wiht this id does not exist"));
        if(!company.getName().equals(updatedCompany.getName())){
            boolean companyWithTheSameName = companyRepository.findByName(updatedCompany.getName()).isPresent();
            if(companyWithTheSameName){
                throw new RuntimeException("The new company name is already taken.");
            }
        }
        try {
            company.setName(updatedCompany.getName());
            company.setCategory(updatedCompany.getCategory());
            company.setTaxIdNumber(updatedCompany.getTaxIdNumber());
            company.setDescription(updatedCompany.getDescription());
            company.setPhoneNumber(updatedCompany.getPhoneNumber());
            company.setFaxNumber(updatedCompany.getFaxNumber());
            company.setEmail(updatedCompany.getEmail());
            company.setAddress(updatedCompany.getAddress());
            company.setStatus(updatedCompany.getStatus());
            company.onUpdate();
            companyRepository.update(company);
        }
        catch(Exception e){
            System.out.println(e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot update this company because a different company contains the same email or tax id");
        }
    }

    public void updateCompanyAddress(String id, Address newAddress){
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        EmployerModel employer = employerRepository.findByUsername(sessionUser).orElseThrow(() -> new UsernameNotFoundException("This executive does not exists"));
        CompanyModel company = companyRepository.findById(id).orElseThrow(()-> new RuntimeException("A company wiht this id does not exist"));
        isAddressValid(newAddress);
        try {
            company.setAddress(newAddress);
            company.onUpdate();
            companyRepository.update(company);
        }
        catch(Exception e){
            System.out.println(e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot update this company because a different company contains the same email or tax id");
        }
    }

    public void updateCompanyContact(String id, CompanyContact contact){
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        EmployerModel employer = employerRepository.findByUsername(sessionUser).orElseThrow(() -> new UsernameNotFoundException("This executive does not exists"));
        CompanyModel company = companyRepository.findById(id).orElseThrow(()-> new RuntimeException("A company wiht this id does not exist"));
        isPhoneNumberAndFaxNumberValid(contact.getPhoneNumber(), contact.getFaxNumber() );
        isEmailValid(contact.getEmail());
        isNameTaxIdAndDescriptionValid(company.getName(), contact.getTaxIdNumber(), company.getDescription());
        try {
            company.setEmail(contact.getEmail());
            company.setTaxIdNumber(contact.getTaxIdNumber());
            company.setFaxNumber(contact.getFaxNumber());
            company.setPhoneNumber(contact.getPhoneNumber());
            company.onUpdate();
            companyRepository.update(company);
        }
        catch(Exception e){
            System.out.println(e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot update this company because a different company contains the same email or tax id");
        }
    }


    public void updateCompanyAboutInfo(String id, CompanyAboutInfo info){
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        EmployerModel employer = employerRepository.findByUsername(sessionUser).orElseThrow(() -> new UsernameNotFoundException("This executive does not exists"));
        CompanyModel company = companyRepository.findById(id).orElseThrow(()-> new RuntimeException("A company wiht this id does not exist"));
        isNameTaxIdAndDescriptionValid(company.getName(), company.getTaxIdNumber(), info.getDescription());
        company.setDescription(info.getDescription());
        company.setStatus(info.getStatus());
        company.onUpdate();
        companyRepository.update(company);
    }

    public boolean doesTheCompanyAlreadyExist(CompanyModel newCompany){
        if(companyRepository.findByName(newCompany.getName()).isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A company with this name already Exists");
        }
        return false;
    }

    public void registerBusiness(CompanyModel newBusiness, HttpServletRequest request) throws AccessDeniedException {
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        System.out.println("DEBUG: ATTEMPT TO REGISTER A COMPANY: Session User " + sessionUser);
        doesTheCompanyAlreadyExist(newBusiness);
        System.out.println("DEBUG: ATTEMPT TO REGISTER A COMPANY:THE COMPANY EXISTS " +   doesTheCompanyAlreadyExist(newBusiness));
        boolean response =  isCompanyValid(newBusiness);
        System.out.println("DEBUG: ATTEMPT TO REGISTER A COMPANY: IS COMPANY VALID" +  response);
        if(!response){ throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The company email or taxID are not valid");}
        EmployerModel creator = employerRepository.findByUsername(sessionUser).orElseThrow(()
                -> new UsernameNotFoundException("Employer " + sessionUser + " not found"));
        System.out.println("DEBUG:: Register Company Method-> Username: " + creator.getUsername() );
        newBusiness.setCreatorUsername(creator.getUsername());
        newBusiness.onCreate();
        try{
            companyRepository.save(newBusiness);
            creator.getCompaniesRegistered().add(companyMapper.transformData(newBusiness));
            employerRepository.update(creator);
        }catch (Exception e){
            System.out.println("Exception when trying to register the compay "+e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A company with the same email or Tax Id already exists");
        }

    }

}
