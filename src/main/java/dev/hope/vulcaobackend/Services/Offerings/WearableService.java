package dev.hope.vulcaobackend.Services.Offerings;

import dev.hope.vulcaobackend.DTOs.WearableDTO;
import dev.hope.vulcaobackend.Mapper.OfferingMapper;
import dev.hope.vulcaobackend.Models.Business.BusinessModel;
import dev.hope.vulcaobackend.Models.Offerings.OfferingModel;
import dev.hope.vulcaobackend.Models.UserModel;
import dev.hope.vulcaobackend.Properties.OfferingType;
import dev.hope.vulcaobackend.Repositories.BusinessRepository;
import dev.hope.vulcaobackend.Repositories.OfferingRepository;
import dev.hope.vulcaobackend.Services.ExceptionHandler.ApiException;
import dev.hope.vulcaobackend.Services.GlobalServices;
import dev.hope.vulcaobackend.Services.Validators;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class WearableService {

    @Autowired
    OfferingRepository offeringRepository;
    @Autowired
    GlobalServices globalServices;

    @Autowired OfferingMapper offeringMapper;
    @Autowired
    private BusinessRepository businessRepository;


    public String save(WearableDTO wearable){
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        Validators.validateWearable(wearable);
        BusinessModel business = businessRepository.findById(wearable.getBusinessId()).orElseThrow(()->
            new ApiException(HttpStatus.NOT_FOUND, "Esta empresa não existe, por isso não é possível associar o produto a ela."));
        UserModel user = globalServices.validateAuthorization(sessionUser);
        wearable.setCreatorId(user.getId());
        wearable.setUpdatedBy(user.getId());
        if(!business.getCreatorId().equals(user.getId())){
            throw new ApiException(HttpStatus.FORBIDDEN, "Você não tem permissão para adicionar produtos nesta empresa.");
        }

        OfferingModel offering = offeringMapper.convertWearableDtoToModel(wearable);
        String id = globalServices.generateOfferingId(OfferingType.WEARABLE);
        offering.setId(id);
        offering.setType(OfferingType.WEARABLE.toString());
        offeringRepository.save(offering);
        return id;
    }

    public List<WearableDTO> findAll(){
        return offeringRepository.find100LatestWearables().stream().map(
                offeringMapper::convertModelToWearableDto
        ). filter(Objects::nonNull)
                . collect(Collectors.toList());
    }


    public List<WearableDTO> findByBusinessId(String companyId){
        return offeringRepository.findByBusinessId(companyId).stream().map(
                offeringMapper::convertModelToWearableDto
        ).filter(Objects::nonNull).collect(Collectors.toList());
    }


    public List<WearableDTO> findByCreatorId(String creatorId){
        return offeringRepository.findByCreatorId(creatorId).stream().map(
                offeringMapper::convertModelToWearableDto
        ).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public List<WearableDTO> findByPrice(Double price){
        return offeringRepository.findByPrice(price).stream().map(
                offeringMapper::convertModelToWearableDto
        ).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public WearableDTO findById(String id){
        OfferingModel model =  offeringRepository.findById(id).orElseThrow(
                () -> new ApiException(HttpStatus.NOT_FOUND, "O artigo vestuário com o ID " + id + " não foi encontrado.")
        );
        return offeringMapper.convertModelToWearableDto(model);
    }


    public void update(String id, @Valid WearableDTO wearable) {
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        UserModel user = globalServices.validateAuthorization(sessionUser);
        OfferingModel offering = offeringRepository.findById(id).orElseThrow(()->
                new ApiException(HttpStatus.NOT_FOUND, "A oferta solicitada não foi encontrada."));

        if(!offering.getId().equals(wearable.getId())){
            throw new ApiException(HttpStatus.FORBIDDEN, "Os dados de identificação não coincidem.");
        }
        Validators.validateWearable(wearable);
        OfferingModel model = offeringMapper.convertWearableDtoToModel(wearable);
        offeringRepository.update(model);

    }


}
