package dev.hope.vulcaobackend.Mapper;

import dev.hope.vulcaobackend.DTOs.ProductDTO;
import dev.hope.vulcaobackend.Models.Offerings.OfferingModel;
import dev.hope.vulcaobackend.Models.Offerings.ProductProperties;
import org.springframework.stereotype.Component;

@Component
public class OfferingMapper {


    public ProductDTO convertModelToProductDto(OfferingModel product){
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setUpdatedAt(product.getUpdatedAt());
        productDTO.setCreatedAt(product.getCreatedAt());

        productDTO.setName(product.getName());
        productDTO.setBusinessId(product.getBusinessId());
        productDTO.setPrice(product.getPrice());
        productDTO.setStock(product.getStock());
        productDTO.setUnit(product.getUnit());
        productDTO.setUpdatedBy(product.getUpdatedBy());
        productDTO.setCurrency(product.getCurrency());
        productDTO.setDescription(product.getDescription());
        productDTO.setCreatorId(product.getCreatorId());
        productDTO.setImgS3UrlKeys(product.getImgS3UrlKeys());
        productDTO.setType(product.getType());

        productDTO.setExpirationDate(product.getProduct().getExpirationDate());
        productDTO.setProductType(product.getProduct().getProductType());
        return productDTO;
    }

    public OfferingModel convertProductDtoToModel(ProductDTO product){
        OfferingModel model = new OfferingModel();
        model.setId(product.getId());
        model.setUpdatedAt(product.getUpdatedAt());
        model.setCreatedAt(product.getCreatedAt());

        model.setName(product.getName());
        model.setBusinessId(product.getBusinessId());
        model.setPrice(product.getPrice());
        model.setStock(product.getStock());
        model.setUnit(product.getUnit());
        model.setUpdatedBy(product.getUpdatedBy());
        model.setCurrency(product.getCurrency());
        model.setDescription(product.getDescription());
        model.setCreatorId(product.getCreatorId());
        model.setImgS3UrlKeys(product.getImgS3UrlKeys());
        model.setType(product.getType());
        ProductProperties properties = new ProductProperties();
        properties.setProductType(product.getProductType());
        properties.setExpirationDate(product.getExpirationDate());
        model.setProduct(properties);
        return model;
    }

}
