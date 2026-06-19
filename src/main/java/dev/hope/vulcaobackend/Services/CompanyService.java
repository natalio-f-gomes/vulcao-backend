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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class CompanyService {
    @Autowired private CompanyRepository companyRepository;
    @Autowired private EmployerRepository employerRepository;
    @Autowired
    private CompanyMapper companyMapper;


    private final static String NAME_PATTERN = "^[\\p{L}0-9 +_.\\-/()&',]{0,500}$";


    public static ResponseEntity<?> isCompanyAddressValid(Address address){
        if (address == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Os dados da morada devem ser preenchidos.");
        }

        debugInput(address.getCity() );
        debugInput(address.getZone() );
        debugInput(address.getPostalCode() );
        debugInput(address.getStreet() );

        // 1. Validação da Zona (Obrigatória)
        if (address.getZone() == null || address.getZone().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A zona da morada deve ser preenchida.");
        }

        if (!address.getZone().matches(NAME_PATTERN)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A zona da morada é inválida. Utilize apenas letras e números.");
        }

        // 2. Validação do Código Postal (Opcional - Aceita nulo ou vazio)
        if (address.getPostalCode() != null && !address.getPostalCode().isEmpty()) {
            if (!address.getPostalCode().matches(NAME_PATTERN)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O código postal é inválido. Utilize apenas letras e números.");
            }
        }

        // 3. Validação da Rua (Opcional - Aceita nulo ou vazio)
        if (address.getStreet() != null && !address.getStreet().isEmpty()) {
            if (!address.getStreet().matches(NAME_PATTERN)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A rua da morada é inválida. Utilize apenas letras e números.");
            }
        }

        // 4. Validação da Cidade (Obrigatória)
        if (address.getCity() == null || address.getCity().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A cidade da morada deve ser preenchida.");
        }
        if (!address.getCity().matches(NAME_PATTERN)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A cidade da morada é inválida. Utilize apenas letras e números.");
        }

        return ResponseEntity.accepted().build();
    }

    public ResponseEntity<?> isCompanyEmailValid(String email){

        System.out.println("EMAIL=[" + email + "] len=" + (email == null ? -1 : email.length())
                + " atIndex=" + (email == null ? -1 : email.indexOf('@')));
         Pattern EMAIL = Pattern.compile(
                "^[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*" + // local part (dot-atoms)
                        "@" +
                        "(?:[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?\\.)+" +                   // one or more domain labels
                        "[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?$"                            // final label (TLD), dot required
        );
        if (email == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("O email não pode estar vazio");
        int len = email.length();
        if (len < 3 || len > 254) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("O email deve ter entre 3 e 254 caracteres.");
        }
        int at = email.indexOf('@');
        if (at < 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O email deve conter '@'.");
        if (at < 1 || at > 64)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("O nome de utilizador antes do '@' deve ter entre 1 e 64 caracteres.");
        if (len - at - 1 > 255) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("O domínio do email após o '@' não pode exceder 255 caracteres.");
        }
        if (!EMAIL.matcher(email).matches()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("O formato do email é inválido.");
        }
        return ResponseEntity.accepted().build();
    }

    public ResponseEntity<?> isCompanyNameValid(String name){
        debugInput(name);
        if(name == null || name.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O nome da empresa deve ser preenchido.");
        }
        if(!name.matches(NAME_PATTERN)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O nome da empresa é inválido. Utilize apenas letras e números.");
        }
        return ResponseEntity.accepted().build();
    }

    public ResponseEntity<?> isCompanyDescriptionValid(String description){
        debugInput(description);
        if(description == null || description.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A descrição da empresa deve ser preenchida.");
        }
        if(!description.matches(NAME_PATTERN)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A descrição da empresa é inválida. Utilize apenas letras e números.");
        }
        return ResponseEntity.accepted().build();
    }

    public ResponseEntity<?> isCompanyTaxIdValid(String taxId){
        debugInput(taxId);
        String TAX_ID_FORMAT = "^[a-zA-Z0-9-]{0,50}$";

        if(!taxId.matches(TAX_ID_FORMAT)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O NIF da empresa é inválido. Utilize apenas números e letras (máximo 50 dígitos).");
        }
        return ResponseEntity.accepted().build();
    }

    public static ResponseEntity<?> isCompanyPhoneNumberValid(String phoneNumber){
        debugInput(phoneNumber);
        String CV_PHONE_FORMAT = "^[+0-9\\- ]{7,15}$";
        if(phoneNumber == null || phoneNumber.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O telefone da empresa deve ser preenchido.");
        }
        if(!phoneNumber.matches(CV_PHONE_FORMAT)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O telefone da empresa é inválido. Utilize apenas números, espaços ou hífens (entre 7 e 15 dígitos).");
        }
        return ResponseEntity.accepted().build();
    }

    public static ResponseEntity<?> isCompanyFaxValid(String fax){
        debugInput(fax);
        String FAX_FORMAT = "^\\d{0,50}$";
        if(!fax.matches(FAX_FORMAT)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O fax da empresa é inválido. Utilize apenas números (máximo 50 dígitos).");
        }
        return ResponseEntity.accepted().build();
    }

    public static void debugInput(String field){
        System.out.println(field.toUpperCase() + "=[" + field + "] len=" + (field == null ? -1 : field.length()));
    }


    public ResponseEntity<?> isCompanyValid(CompanyModel company){
        ResponseEntity<?> companyName = isCompanyNameValid(company.getName());
        if(!companyName.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return companyName;
        }
        ResponseEntity<?> companyDescription = isCompanyDescriptionValid(company.getDescription());
        if(!companyDescription.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return companyDescription;
        }

        ResponseEntity<?> companyTaxId = isCompanyTaxIdValid(company.getTaxIdNumber());
        if(!companyTaxId.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return companyTaxId;
        }

        ResponseEntity<?> companyFax = isCompanyFaxValid(company.getFaxNumber());
        if(!companyFax.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return companyFax;
        }

        ResponseEntity<?> companyPhoneNumber = isCompanyPhoneNumberValid(company.getPhoneNumber());
        if(!companyPhoneNumber.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return companyPhoneNumber;
        }

        ResponseEntity<?> companyEmail = isCompanyEmailValid(company.getEmail());
        if(!companyEmail.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return companyEmail;
        }

        return ResponseEntity.accepted().build();
    }

    public CompanyDTO findCompanyById(String companyID){
        CompanyModel company = companyRepository.findById(companyID)
                .orElseThrow (()  -> new RuntimeException("A empresa com o ID " + companyID + " não foi encontrada. Esta empresa poderá ter sido eliminada."));
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

    public ResponseEntity<?> updateCompany(String id, CompanyDTO updatedCompany){
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        EmployerModel employer = employerRepository.findByUsername(sessionUser).orElseThrow(() -> new UsernameNotFoundException("Este(a) empresário(a) não existe: " + sessionUser));
        CompanyModel company = companyRepository.findById(id).orElseThrow(()-> new RuntimeException("A empresa com o ID " + id + " não foi encontrada. Esta empresa poderá ter sido eliminada."));
        if(!company.getName().equals(updatedCompany.getName())){
            boolean companyWithTheSameName = companyRepository.findByName(updatedCompany.getName()).isPresent();
            if(companyWithTheSameName){
              return ResponseEntity.status(HttpStatus.CONFLICT).body("Já existe uma empresa com este nome.");
            }
        }

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
        return ResponseEntity.accepted().build();
        }



    public ResponseEntity<?> updateCompanyAddress(String id, Address newAddress){
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        EmployerModel employer = employerRepository.findByUsername(sessionUser).orElseThrow(() -> new UsernameNotFoundException("Este empresário(a) " + sessionUser +" não existe!"));
        CompanyModel company = companyRepository.findById(id).orElseThrow(()-> new RuntimeException("Não existe nenhuma empresa com este ID."));
        ResponseEntity<?> valid = isCompanyAddressValid(newAddress);
        if(!valid.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return valid;
        }

        company.setAddress(newAddress);
        company.onUpdate();
        companyRepository.update(company);

        return ResponseEntity.accepted().build();
    }

    public ResponseEntity<?> updateCompanyContact(String id, CompanyContact contact){
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        EmployerModel employer = employerRepository.findByUsername(sessionUser).orElseThrow(() -> new UsernameNotFoundException("Este empresário(a) " + sessionUser +" não existe!"));
        CompanyModel company = companyRepository.findById(id).orElseThrow(()-> new RuntimeException("Não existe nenhuma empresa com este ID."));


        ResponseEntity<?> companyFax = isCompanyFaxValid(contact.getFaxNumber());
        if(!companyFax.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return companyFax;
        }

        ResponseEntity<?> companyPhoneNumber = isCompanyPhoneNumberValid(contact.getPhoneNumber());
        if(!companyPhoneNumber.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return companyPhoneNumber;
        }

        ResponseEntity<?> companyEmail = isCompanyEmailValid(contact.getEmail());
        if(!companyEmail.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return companyEmail;
        }

        company.setEmail(contact.getEmail());
        company.setFaxNumber(contact.getFaxNumber());
        company.setPhoneNumber(contact.getPhoneNumber());
        company.onUpdate();
        companyRepository.update(company);
        return ResponseEntity.accepted().build();

    }


    public ResponseEntity<?> updateCompanyAboutInfo(String id, CompanyAboutInfo info){
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        EmployerModel employer = employerRepository.findByUsername(sessionUser).orElseThrow(() -> new UsernameNotFoundException("Este empresário(a) " + sessionUser +" não existe!"));
        CompanyModel company = companyRepository.findById(id).orElseThrow(()-> new RuntimeException("Não existe nenhuma empresa com este ID."));

        ResponseEntity<?> companyDescription = isCompanyDescriptionValid(info.getDescription());
        if(!companyDescription.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return companyDescription;
        }

        ResponseEntity<?> companyTaxId = isCompanyTaxIdValid(company.getTaxIdNumber());
        if(!companyTaxId.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return companyTaxId;
        }
        company.setDescription(info.getDescription());
        company.setStatus(info.getStatus());
        company.onUpdate();
        companyRepository.update(company);
        return ResponseEntity.accepted().build();
    }

    public ResponseEntity<?> doesTheCompanyAlreadyExist(CompanyModel newCompany){
        if(companyRepository.findByName(newCompany.getName()).isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Já existe uma empresa com este nome.");
        }
        return ResponseEntity.accepted().build();
    }

    public ResponseEntity<?> registerBusiness(CompanyModel newBusiness) throws AccessDeniedException {
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        System.out.println("DEBUG: ATTEMPT TO REGISTER A COMPANY: Session User " + sessionUser);
        ResponseEntity<?> companyExists = doesTheCompanyAlreadyExist(newBusiness);
        if(!companyExists.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            System.out.println("DEBUG: ATTEMPT TO REGISTER A COMPANY:THE COMPANY EXISTS WITH THIS NAME");
            return companyExists;
        }
        ResponseEntity<?> response =  isCompanyValid(newBusiness);
        if(!response.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return response;
        }

        EmployerModel creator = employerRepository.findByUsername(sessionUser).orElseThrow(()
                -> new UsernameNotFoundException("Employer " + sessionUser + " not found"));
        System.out.println("DEBUG:: Register Company Method-> Username: " + creator.getUsername() );
        newBusiness.setCreatorUsername(creator.getUsername());
        newBusiness.onCreate();

        companyRepository.save(newBusiness);
        creator.getCompaniesRegistered().add(companyMapper.transformData(newBusiness));
        employerRepository.update(creator);

        return ResponseEntity.accepted().build();
    }

}
