package dev.hope.vulcaobackend.Services.Offerings;

import dev.hope.vulcaobackend.DTOs.ProductDTO;
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
public class ProductService {

    @Autowired
    OfferingRepository offeringRepository;
    @Autowired
    GlobalServices globalServices;

    @Autowired OfferingMapper offeringMapper;
    @Autowired
    private BusinessRepository businessRepository;


    public String save(ProductDTO product){
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        Validators.validateProduct(product);
        BusinessModel business = businessRepository.findById(product.getBusinessId()).orElseThrow(()->
            new ApiException(HttpStatus.NOT_FOUND, "Esta empresa não existe, por isso não é possível associar o produto a ela."));
        UserModel user = globalServices.validateAuthorization(sessionUser);
        product.setCreatorId(user.getId());
        product.setUpdatedBy(user.getId());
        if(!business.getCreatorId().equals(user.getId())){
            throw new ApiException(HttpStatus.FORBIDDEN, "Você não tem permissão para adicionar produtos nesta empresa.");
        }

        OfferingModel offering = offeringMapper.convertProductDtoToModel(product);
        String id = globalServices.generateOfferingId(OfferingType.PRODUCT);
        offering.setId(id);
        offering.setType(OfferingType.PRODUCT.toString());
        offeringRepository.save(offering);
        return id;
    }

    public List<ProductDTO> findAll(){
            return offeringRepository.find100LatestProducts().stream().map(
                    offeringMapper::convertModelToProductDto
            ). filter(Objects::nonNull)
                            . collect(Collectors.toList());
    }


    public List<ProductDTO> findByBusinessId(String companyId){
        return offeringRepository.findByBusinessId(companyId).stream().map(
                offeringMapper::convertModelToProductDto
        ).filter(Objects::nonNull).collect(Collectors.toList());
    }


    public List<ProductDTO> findByCreatorId(String creatorId){
        return offeringRepository.findByCreatorId(creatorId).stream().map(
                offeringMapper::convertModelToProductDto
        ).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public List<ProductDTO> findByPrice(Double price){
        return offeringRepository.findByPrice(price).stream().map(
                offeringMapper::convertModelToProductDto
        ).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public ProductDTO findById(String id){
        OfferingModel model =  offeringRepository.findById(id).orElseThrow(
                ()-> new ApiException(HttpStatus.NOT_FOUND, "Product with this id: "+ id + " not found")
        );
        return offeringMapper.convertModelToProductDto(model);
    }


    public void update(String id, @Valid ProductDTO product) {
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        UserModel user = globalServices.validateAuthorization(sessionUser);
        OfferingModel offering = offeringRepository.findById(id).orElseThrow(()->
                new ApiException(HttpStatus.NOT_FOUND, "A oferta solicitada não foi encontrada."));

        if(!offering.getId().equals(product.getId())){
            throw new ApiException(HttpStatus.FORBIDDEN, "Os dados de identificação não coincidem.");
        }
        Validators.validateProduct(product);
        OfferingModel model = offeringMapper.convertProductDtoToModel(product);
        offeringRepository.update(model);

    }

}
