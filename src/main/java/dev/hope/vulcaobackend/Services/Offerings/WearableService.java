package dev.hope.vulcaobackend.Services.Offerings;

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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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


    public void employerSaveWearable(WearableModel newItem, String sessionUser){
        System.out.println("EMPLOYER SAVE PRODUCT SERVICE REACHED "+ newItem.getName());
        EmployerModel employer = employerRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is no employer with this name"));
        CompanyModel company = companyRepository.findById(newItem.getCompanyId()).orElseThrow(()-> new RuntimeException("There is no company with this id"));
        newItem.setCreatorId(employer.getId());
        newItem.onCreate();
        newItem.setUpdatedBy(employer.getUsername());
        System.out.println("DEBUG ID: " + newItem.getId());
        System.out.println("DEBUG CREATED_AT: " + newItem.getCreatedAt());
        System.out.println("DEBUG UPDATED_AT: " + newItem.getUpdatedAt());

        globalAccessControl.isWearableValid(newItem);
        wearableRepository.save(newItem);
    }

    public void staffSaveWearable(WearableModel newItem, String sessionUser){
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
        globalAccessControl.isWearableValid(newItem);
        wearableRepository.save(newItem);

    }

    public void employerUpdateProduct(String id, WearableModel updatedItem, String sessionUser){
        if(!id.equals(updatedItem.getId())){
            throw new RuntimeException("PRODUCT WITH ID DOES NOT MATCH THE ID IN THE PARAMETER: " + id);
        }
        // 1. Verify the product actually exists in DB first
        WearableModel item = wearableRepository.findById(id).orElseThrow(()-> new RuntimeException("THERE IS NO PRODUCT WITH THIS ID"));
        EmployerModel employer = employerRepository.findByUsername(sessionUser)
                .orElseThrow(() -> new UsernameNotFoundException("Employer not found"));
        globalAccessControl.isWearableValid(updatedItem);
        WearableModel itemUpdated =  updateWearable(item, updatedItem, employer.getUsername());
        wearableRepository.update(itemUpdated);

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



    public void staffUpdateWearable(String id, WearableModel updatedItem, String sessionUser){
        StaffModel staff = staffRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is no Staff with this name"));
        if(!staff.getAssignedPermissions().contains(Permissions.UPDATE_PRODUCT)){
            throw new AccessDeniedException("STAFF " + staff.getUsername() + " Does not the the permissions to save Wearable");
        }
        CompanyModel company = companyRepository.findById(updatedItem.getCompanyId()).orElseThrow(()-> new RuntimeException("There is no company with this id"));
        WearableModel item = wearableRepository.findById(id).orElseThrow(()-> new RuntimeException("THERE IS NO WEARABLE WITH THIS ID"));
        globalAccessControl.isWearableValid(updatedItem);
        WearableModel itemUpdated =  updateWearable(item, updatedItem, staff.getUsername());
        wearableRepository.update(itemUpdated);
    }

    public void employerDeleteProduct(String itemId, String sessionUser){
        EmployerModel employer = employerRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is no employer with this name"));
        WearableModel item = wearableRepository.findById(itemId).orElseThrow(()-> new RuntimeException("There is no product with this id"));
        CompanyModel company = companyRepository.findById(item.getCompanyId()).orElseThrow(()-> new RuntimeException("There is no company with this id"));
        wearableRepository.delete(item);
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

}


