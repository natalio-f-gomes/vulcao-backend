package dev.hope.vulcaobackend.Services.Offerings;

import dev.hope.vulcaobackend.DataTransferObjects.CompanyDTO;
import dev.hope.vulcaobackend.DataTransferObjects.Offerings.ProductDTO;
import dev.hope.vulcaobackend.Mappers.OfferingsMapper;
import dev.hope.vulcaobackend.Models.AccountEntities.EmployerModel;
import dev.hope.vulcaobackend.Models.AccountEntities.StaffModel;
import dev.hope.vulcaobackend.Models.CompanyModel;
import dev.hope.vulcaobackend.Models.Offerings.ProductModel;
import dev.hope.vulcaobackend.Models.Offerings.WearableModel;
import dev.hope.vulcaobackend.Models.Properties.Permissions;
import dev.hope.vulcaobackend.Repositories.CompanyRepository;
import dev.hope.vulcaobackend.Repositories.EmployerRepository;
import dev.hope.vulcaobackend.Repositories.Offerings.ProductRepository;
import dev.hope.vulcaobackend.Repositories.Offerings.WearableRepository;
import dev.hope.vulcaobackend.Repositories.StaffRepository;
import dev.hope.vulcaobackend.Services.GlobalAccessControl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static dev.hope.vulcaobackend.Services.CompanyService.debugInput;
import static dev.hope.vulcaobackend.Services.Offerings.ProductService.NAME_PATTERN;
import static java.rmi.server.LogStream.log;

@Slf4j
@Service
public class WearableService {
    @Autowired private EmployerRepository employerRepository;
    @Autowired private StaffRepository staffRepository;

    @Autowired private CompanyRepository companyRepository;
    @Autowired private ProductRepository productRepository;

    @Autowired private OfferingsMapper offeringsMapper;
    @Autowired private GlobalAccessControl globalAccessControl;
    @Autowired
    private WearableRepository wearableRepository;


    public ResponseEntity<?> employerSaveWearable(WearableModel newItem, String sessionUser){
        System.out.println("EMPLOYER SAVE PRODUCT SERVICE REACHED "+ newItem.getName());
        EmployerModel employer = employerRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is no employer with this name"));
        CompanyModel company = companyRepository.findById(newItem.getCompanyId()).orElseThrow(()-> new RuntimeException("There is no company with this id"));
        newItem.setCreatorId(employer.getId());
        newItem.onCreate();
        newItem.setUpdatedBy(employer.getUsername());
        System.out.println("DEBUG ID: " + newItem.getId());
        System.out.println("DEBUG CREATED_AT: " + newItem.getCreatedAt());
        System.out.println("DEBUG UPDATED_AT: " + newItem.getUpdatedAt());

        ResponseEntity<?> response = isWearableValid(newItem);
        if(!response.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return response;
        }
        wearableRepository.save(newItem);
        return ResponseEntity.accepted().build();
    }



    public WearableModel updateWearable(WearableModel item, WearableModel updatedItem, String username){
        item.setName(updatedItem.getName());
        item.setBrand(updatedItem.getBrand());
        item.setDescription(updatedItem.getDescription());
        item.setColor(updatedItem.getColor());
        item.setPrice(updatedItem.getPrice());
        item.setMaterial(updatedItem.getMaterial());
        item.setCategory(updatedItem.getCategory());
        item.setStock(updatedItem.getStock());
        item.setCurrency(updatedItem.getCurrency());
        item.setUpdatedBy(username);
        item.setSize(updatedItem.getSize());
        item.onUpdate();
        return updatedItem;
    }

    public void employerDeleteWearable(String itemId, String sessionUser){
        EmployerModel employer = employerRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is no employer with this name"));
        WearableModel item = wearableRepository.findById(itemId).orElseThrow(()-> new RuntimeException("There is no product with this id"));
        wearableRepository.delete(item);
    }

    public ResponseEntity<?> doesTheItemBelongToTheCompany(EmployerModel employer, WearableModel item, CompanyModel company){
        List<CompanyDTO> companies = employer.getCompaniesRegistered();
        if(!companies.isEmpty()){
            for(CompanyDTO comp: companies){
                if(comp.getId().equals(company.getId())){
                    return ResponseEntity.ok().build();
                }
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado porque este empresário não é proprietário desta empresa: " + company.getName());
    }

    public ResponseEntity<?> employerUpdateWearable(String itemId, WearableModel updatedItem, String sessionUser){
        EmployerModel employer = employerRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is no employer with this name"));
        WearableModel item = wearableRepository.findById(itemId).orElseThrow(()-> new RuntimeException("There is no product with this id"));
        CompanyModel company = companyRepository.findById(item.getCompanyId()).orElseThrow(()-> new RuntimeException("There is no company with this id"));
       ResponseEntity<?> response =  doesTheItemBelongToTheCompany(employer, item, company);
       if(!response.getStatusCode().isSameCodeAs(HttpStatus.OK)){
           return response;
       }
       ResponseEntity<?> isValid =  isWearableValid(updatedItem);
       WearableModel updatedWearable =  updateWearable(item, updatedItem, sessionUser);
       if(!isValid.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){return isValid;}
        wearableRepository.update(updatedWearable);
       return ResponseEntity.accepted().build();
    }


    public List<WearableModel> findAll(){
        return wearableRepository.findAll();
    }

    public List<WearableModel> findByCompanyId(String companyId){
        return wearableRepository.findByCompanyId(companyId);
    }

    public List<WearableModel> findByCreatorId(String creatorId){
        return wearableRepository.findByCreatorId(creatorId);
    }


    public WearableModel findById(String id){
       return wearableRepository.findById(id).orElseThrow(()-> new RuntimeException("There is no product with this id: "+ id));
    }

    public List<WearableModel> findByPrice(Double price){
        return wearableRepository.findByPrice(price);
    }

    public List<WearableModel> findByName(String itemName){
        return wearableRepository.findByName(itemName);
    }

    public List<WearableModel> findByColor(String color){
        return wearableRepository.findByItemColor(color);
    }

    public ResponseEntity<?> isFieldValid(String field){
        debugInput(field);
        if(field == null || field.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(!field.matches(NAME_PATTERN)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.accepted().build();
    }


    public ResponseEntity<?> isWearableValid(WearableModel wearable) {
        if (wearable.getName() == null || wearable.getName().isBlank() || wearable.getName().length() > 200) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("O nome do produto não pode estar vazio nem ultrapassar os 200 caracteres.");
        }
        if(!isFieldValid(wearable.getName()).getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("O nome do Vestível é invalida!");
        }
        if (wearable.getDescription() == null || wearable.getDescription().isBlank() || wearable.getDescription().length() > 2500) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A descrição do produto não pode estar vazia nem ultrapassar os 2500 caracteres.");
        }
        if(!isFieldValid(wearable.getDescription()).getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A descrição do vestíel é invalida");
        }

        if(!isFieldValid(wearable.getSize()).getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("O Tamanho do vestíel é invalida");
        }

        if(!isFieldValid(wearable.getBrand()).getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A marca do vestíel é invalida");
        }

        if(!isFieldValid(wearable.getMaterial()).getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("O material do vestíel é invalido");
        }

        if(!isFieldValid(wearable.getColor()).getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A cor do vestíel é invalida");
        }

        if (wearable.getPrice() == null || wearable.getPrice() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("O preço do vestível não pode ser nulo nem negativo.");
        }

        if (wearable.getStock() == null ||wearable.getStock() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("O stock do vestível não podem ser nulos, negativos ou conter letras .");
        }


        // Se passou todas as validações de barreira:
        log("Vestivel " + wearable.getName() + " é Válido");

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body("O Vestivel é válido e foi aceite com sucesso.");
    }



    public ResponseEntity<?> staffSaveWearable(WearableModel newItem, String sessionUser){
        StaffModel staff = staffRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is no Staff with this name"));
        if(!staff.getAssignedPermissions().contains(Permissions.ADD_PRODUCT)){
            throw new AccessDeniedException("STAFF " + staff.getUsername() + " Does not the the permissions to save produts");
        }
        CompanyModel company = companyRepository.findById(newItem.getCompanyId()).orElseThrow(()-> new RuntimeException("There is no company with this id"));
        newItem.setCreatorId(staff.getId());
        newItem.onCreate();
        newItem.setCompanyId(company.getId());
        newItem.setUpdatedBy(staff.getUsername());
        System.out.println("DEBUG ID: " + newItem.getId());
        System.out.println("DEBUG CREATED_AT: " + newItem.getCreatedAt());
        System.out.println("DEBUG UPDATED_AT: " + newItem.getUpdatedAt());
        ResponseEntity<?> response = isWearableValid(newItem);
        if(!response.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return response;
        }
        wearableRepository.save(newItem);
        return ResponseEntity.accepted().build();

    }


    public void staffDeleteProduct(String itemId, String sessionUser){
        StaffModel staff = staffRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is no Staff with this name"));
        if(!staff.getAssignedPermissions().contains(Permissions.DELETE_WEARABLE)){
            throw new AccessDeniedException("STAFF " + staff.getUsername() + " Does not the the permissions to save produts");
        }
        WearableModel item = wearableRepository.findById(itemId).orElseThrow(()-> new RuntimeException("There is no product with this id"));
        CompanyModel company = companyRepository.findById(item.getCompanyId()).orElseThrow(()-> new RuntimeException("There is no company with this id"));
        wearableRepository.delete(item);
    }

    public ResponseEntity<?> staffUpdateWearable(String id, WearableModel updatedItem, String sessionUser) {
        StaffModel staff = staffRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is no staff with this name"));
        WearableModel item = wearableRepository.findById(id).orElseThrow(()-> new RuntimeException("There is no product with this id"));
        CompanyModel company = companyRepository.findById(item.getCompanyId()).orElseThrow(()-> new RuntimeException("There is no company with this id"));
        if(!staff.getCompanyId().equals(updatedItem.getCompanyId())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Não tem permissão para atualizar um vestível que não pertence à mesma empresa em que trabalha.");
        }
        if(!staff.getAssignedPermissions().contains(Permissions.UPDATE_WEARABLE)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("O funcionário não tem permissão para atualizar o vestível. Por favor, entre em contato com o seu gerente para atribuir esta permissão.");
        }
        ResponseEntity<?> response =  isWearableValid(updatedItem);
        if(!response.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){return response;}
        WearableModel updatedWearable =  updateWearable(item, updatedItem, sessionUser);
        wearableRepository.update(updatedWearable);
        return ResponseEntity.accepted().build();
    }




}


