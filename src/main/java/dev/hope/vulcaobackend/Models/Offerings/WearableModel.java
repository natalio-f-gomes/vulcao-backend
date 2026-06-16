package dev.hope.vulcaobackend.Models.Offerings;

import dev.hope.vulcaobackend.DataTransferObjects.ImageMetadata;
import dev.hope.vulcaobackend.Models.Common.BaseModelEntities;
import dev.hope.vulcaobackend.Models.Properties.Currency;
import dev.hope.vulcaobackend.Models.Properties.OfferingType;
import dev.hope.vulcaobackend.Models.Properties.WearablesCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@DynamoDbBean
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WearableModel extends BaseModelEntities {
    private String name;
    private String brand;
    private String description;
    private String color;
    private Double price;
    private String material;
    private WearablesCategory category;
    private Integer stock;
    private Currency currency;
    private String companyId;
    private String creatorId;
    private String size;
    private OfferingType offeringType = OfferingType.WEARABLE;
    protected String updatedBy;
    private List<ImageMetadata> imagesMetadata;
    private List<String> imgS3UrlKeys = new ArrayList<>();

    @DynamoDbPartitionKey
    @DynamoDbSecondaryPartitionKey(indexNames = "wearable-id-index")
    public String getId(){return this.id;}

    @DynamoDbSecondaryPartitionKey(indexNames = "item-name-index")
    public String getName(){return this.name;}

    @DynamoDbSecondaryPartitionKey(indexNames = "item-price-index")
    public Double getPrice(){return this.price;}

    @DynamoDbSecondaryPartitionKey(indexNames = "item-color-index")
    public String getColor(){return this.color;}

    @DynamoDbSecondaryPartitionKey(indexNames = "creator-id-index")
    public String getCreatorId(){return this.creatorId;}

    @DynamoDbSecondaryPartitionKey(indexNames = "company-id-index")
    public String getCompanyId(){return this.companyId;}

    public void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

    public void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }


}
