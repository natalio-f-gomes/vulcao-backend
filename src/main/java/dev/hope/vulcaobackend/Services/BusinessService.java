package dev.hope.vulcaobackend.Services;

import dev.hope.vulcaobackend.DTOs.BusinessAboutInfo;
import dev.hope.vulcaobackend.DTOs.BusinessDTO;
import dev.hope.vulcaobackend.DTOs.Contact;
import dev.hope.vulcaobackend.Models.Business.BusinessModel;
import dev.hope.vulcaobackend.Models.Business.WeeklySchedule;
import dev.hope.vulcaobackend.Models.UserModel;
import dev.hope.vulcaobackend.Properties.Address;
import dev.hope.vulcaobackend.Properties.BusinessStatus;
import dev.hope.vulcaobackend.Properties.BusinessType;
import dev.hope.vulcaobackend.Repositories.BusinessRepository;
import dev.hope.vulcaobackend.Services.ExceptionHandler.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class BusinessService {
    @Autowired
    BusinessRepository businessRepository;
    @Autowired GlobalServices globalServices;

    public BusinessModel convertDtoToModel(BusinessDTO businessDTO){
        BusinessModel model = new BusinessModel();
        model.setId(businessDTO.getId());
        model.setCreatedAt(businessDTO.getCreatedAt());
        model.setUpdatedAt(businessDTO.getUpdatedAt());
        model.setName(businessDTO.getName());
        model.setType(businessDTO.getType().toString());
        model.setDescription(businessDTO.getDescription());
        model.setPhoneNumber(businessDTO.getPhoneNumber());
        model.setRevenue(businessDTO.getRevenue());
        model.setEmail(businessDTO.getEmail());
        model.setImgS3UrlKeys(businessDTO.getImgS3UrlKeys());
        model.setImagesMetadata(businessDTO.getImagesMetadata());
        model.setAddress(businessDTO.getAddress());
        model.setWeeklySchedule(businessDTO.getWeeklySchedule());
        model.setCreatorId(businessDTO.getCreatorId());
        model.setStatus(businessDTO.getStatus().toString());
        return model;
    }

    public BusinessDTO convertModelToDto(BusinessModel model){
        BusinessDTO dto = new BusinessDTO();
        dto.setId(model.getId());
        dto.setCreatedAt(model.getCreatedAt());
        dto.setUpdatedAt(model.getUpdatedAt());
        dto.setName(model.getName());
        dto.setType(BusinessType.valueOf(model.getType()));
        dto.setDescription(model.getDescription());
        dto.setPhoneNumber(model.getPhoneNumber());
        dto.setRevenue(model.getRevenue());
        dto.setEmail(model.getEmail());
        dto.setImgS3UrlKeys(model.getImgS3UrlKeys());
        dto.setImagesMetadata(model.getImagesMetadata());
        dto.setAddress(model.getAddress());
        dto.setWeeklySchedule(model.getWeeklySchedule());
        dto.setCreatorId(model.getCreatorId());
        dto.setStatus(BusinessStatus.valueOf(model.getStatus()) );
        return dto;
    }

    public void validateBusiness(BusinessDTO business){
        Validators.validateEmail(business.getEmail());
        Validators.validateAddress(business.getAddress());
        Validators.validateCompanyName(business.getName());
        Validators.validatePhoneNumber(business.getPhoneNumber());
        Validators.validateCompanyDescription(business.getDescription());
    }

    public void checkForName(String name){
        Optional<BusinessModel> business = businessRepository.findByName(name);
        if(business.isPresent()){
            throw new ApiException(HttpStatus.CONFLICT,"Já existe uma empresa com este nome.");
        }
    }

    public String save(BusinessDTO newBusiness){
        validateBusiness(newBusiness);
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        checkForName(newBusiness.getName());
        UserModel user = globalServices.getEmployerByUsername(sessionUser);
        newBusiness.setCreatorId(user.getId());
        String randomId = globalServices.generateBusinessId();
        newBusiness.setId(randomId);
        BusinessModel business = convertDtoToModel(newBusiness);
        businessRepository.save(business);
        return randomId;
    }

    public BusinessDTO findById(String businessId){
        BusinessModel company = businessRepository.findById(businessId)
                .orElseThrow (()  -> new ApiException(HttpStatus.NOT_FOUND ,"O negocio com o ID " + businessId + " não foi encontrada. Esta empresa poderá ter sido eliminada."));
        return convertModelToDto(company);
    }

    public BusinessDTO findByName(String name){
        BusinessModel company = businessRepository.findByName(name)
                .orElseThrow (()  -> new ApiException(HttpStatus.NOT_FOUND ,"O negocio com o nome: " + name + " não foi encontrada. Esta empresa poderá ter sido eliminada."));
        return convertModelToDto(company);
    }

    public List<BusinessDTO> findByCreatorId(String creatorId){
        List<BusinessModel> companies = businessRepository.findByCreatorId(creatorId);

        return companies.stream().map(this::convertModelToDto).toList();
    }

    public List<BusinessDTO> findByType(String type){
        List<BusinessModel> companies = businessRepository.findByType(type);

        return companies.stream().map(this::convertModelToDto).toList();
    }


    public List<BusinessDTO> findByStatus(String status){
        return businessRepository.findByStatus(status).stream().map(this::convertModelToDto).toList();
    }

    public List<BusinessDTO> findAll(){
        return businessRepository.findAll().stream().map(this::convertModelToDto).toList();
    }

    public void deleteById(String id){
        BusinessModel business = businessRepository.findById(id)
                .orElseThrow (()  -> new ApiException(HttpStatus.NOT_FOUND ,"O negocio com o ID " + id + " não foi encontrada. Esta empresa poderá ter sido eliminada."));
        businessRepository.delete(business);
    }

    public BusinessModel isUpdateRequestValid(String businessId){
        BusinessModel business = businessRepository.findById(businessId)
                .orElseThrow (()  -> new ApiException(HttpStatus.NOT_FOUND ,"O negocio com o ID " + businessId + " não foi encontrada. Esta empresa poderá ter sido eliminada."));
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        UserModel user = globalServices.getEmployerByUsername(sessionUser);
        if(!business.getCreatorId().equals(user.getId())){
            throw new ApiException(HttpStatus.FORBIDDEN, "You do not have authrization to update this business");
        }
        return business;
    }


    public void updateAboutInfo(String businessId,BusinessAboutInfo info) {
        Validators.validateCompanyDescription(info.getDescription());
        Validators.validateCompanyName(info.getName());
        BusinessModel business = isUpdateRequestValid(businessId);
        business.setDescription(info.getDescription());
        business.setName(info.getName());
        business.setStatus(info.getStatus().toString());
        business.setType(info.getType().toString());
        businessRepository.update(business);

    }

    public void updateAddress(String businessId, Address address) {
        Validators.validateAddress(address);
        BusinessModel business = isUpdateRequestValid(businessId);
        business.setAddress(address);
        businessRepository.update(business);
    }

    public void updateContact(String businessId, Contact contact) {
        Validators.validateEmail(contact.getEmail());
        Validators.validatePhoneNumber(contact.getPhoneNumber());
        BusinessModel business = isUpdateRequestValid(businessId);
        business.setPhoneNumber(contact.getPhoneNumber());
        business.setEmail(contact.getEmail());
        businessRepository.update(business);
    }

    public void updateSchedule(String businessId, WeeklySchedule schedule) {
        BusinessModel business = isUpdateRequestValid(businessId);
        business.setWeeklySchedule(schedule);
        businessRepository.update(business);
    }

    public void update(String id, BusinessDTO updatedCompany){
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
       UserModel user =  globalServices.getEmployerByUsername(sessionUser);
        BusinessModel businessModel = businessRepository.findById(id).orElseThrow(()-> new ApiException(HttpStatus.NOT_FOUND,"O negocio com o ID " + id + " não foi encontrada. Este negocio poderá ter sido eliminada."));
        if(!updatedCompany.getName().equals(businessModel.getName())){
            boolean companyWithTheSameName = businessRepository.findByName(updatedCompany.getName()).isPresent();
            if(companyWithTheSameName){
                throw new ApiException(HttpStatus.CONFLICT, "Já existe um negocio com este nome.");
            }
        }
        if(!id.equals(updatedCompany.getId())){
            throw new ApiException(HttpStatus.FORBIDDEN, "O ID fornecido não coincide com o ID do negocio.");
        }
        if(!updatedCompany.getCreatorId().equals(user.getId())){
            throw new ApiException(HttpStatus.FORBIDDEN, "Não tem autorização para atualizar este negocio.");
        }
        validateBusiness(updatedCompany);

        businessModel.setName(updatedCompany.getName());
        businessModel.setType(updatedCompany.getType().toString());
        businessModel.setDescription(updatedCompany.getDescription());
        businessModel.setRevenue(updatedCompany.getRevenue());
        businessModel.setPhoneNumber(updatedCompany.getPhoneNumber());
        businessModel.setEmail(updatedCompany.getEmail());
        businessModel.setAddress(updatedCompany.getAddress());
        businessModel.setStatus(updatedCompany.getStatus().toString());
        businessModel.setWeeklySchedule(updatedCompany.getWeeklySchedule());
        businessRepository.update(businessModel);
    }

}
