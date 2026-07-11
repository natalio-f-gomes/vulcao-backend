package dev.hope.vulcaobackend.Mapper;

import dev.hope.vulcaobackend.DTOs.ProductDTO;
import dev.hope.vulcaobackend.DTOs.WearableDTO;
import dev.hope.vulcaobackend.Models.Offerings.OfferingModel;
import dev.hope.vulcaobackend.Models.Offerings.ProductProperties;
import dev.hope.vulcaobackend.Models.Offerings.WearableProperties;
import org.springframework.stereotype.Component;

@Component
public class OfferingMapper {


    public ProductDTO convertModelToProductDto(OfferingModel product) {
        if(product.getProduct()== null){return null;}
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

    public WearableDTO convertModelToWearableDto(OfferingModel product){
        if(product.getWearable() == null){return null;}
        WearableDTO wearable = new WearableDTO();
        wearable.setId(product.getId());
        wearable.setUpdatedAt(product.getUpdatedAt());
        wearable.setCreatedAt(product.getCreatedAt());

        wearable.setName(product.getName());
        wearable.setBusinessId(product.getBusinessId());
        wearable.setPrice(product.getPrice());
        wearable.setStock(product.getStock());
        wearable.setUnit(product.getUnit());
        wearable.setUpdatedBy(product.getUpdatedBy());
        wearable.setCurrency(product.getCurrency());
        wearable.setDescription(product.getDescription());
        wearable.setCreatorId(product.getCreatorId());
        wearable.setImgS3UrlKeys(product.getImgS3UrlKeys());
        wearable.setType(product.getType());

        wearable.setBrand(product.getWearable().getBrand() );
        wearable.setWearableType(product.getWearable().getWearableType());
        wearable.setColors(product.getWearable().getColors());
        wearable.setSize(product.getWearable().getSize());
        wearable.setMaterial(product.getWearable().getMaterial());
        return wearable;
    }


    public OfferingModel convertWearableDtoToModel(WearableDTO wearable){
        OfferingModel model = new OfferingModel();
        model.setId(wearable.getId());
        model.setUpdatedAt(wearable.getUpdatedAt());
        model.setCreatedAt(wearable.getCreatedAt());

        model.setName(wearable.getName());
        model.setBusinessId(wearable.getBusinessId());
        model.setPrice(wearable.getPrice());
        model.setStock(wearable.getStock());
        model.setUnit(wearable.getUnit());
        model.setUpdatedBy(wearable.getUpdatedBy());
        model.setCurrency(wearable.getCurrency());
        model.setDescription(wearable.getDescription());
        model.setCreatorId(wearable.getCreatorId());
        model.setImgS3UrlKeys(wearable.getImgS3UrlKeys());

        model.setType(wearable.getType());

        WearableProperties properties = new WearableProperties();
        properties.setBrand(wearable.getBrand());
        properties.setColors(wearable.getColors());
        properties.setSize(wearable.getSize());
        properties.setMaterial(wearable.getMaterial());
        properties.setWearableType(wearable.getWearableType());

        model.setWearable(properties);

        return model;
    }


}
