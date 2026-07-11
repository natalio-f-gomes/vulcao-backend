package dev.hope.vulcaobackend.Models.Offerings;

import dev.hope.vulcaobackend.DTOs.ImageMetadata;
import dev.hope.vulcaobackend.Models.BaseModel;
import dev.hope.vulcaobackend.Properties.Currency;
import dev.hope.vulcaobackend.Properties.Unit;
import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DynamoDbBean
public class OfferingModel extends BaseModel {
    protected String name;
    protected Double price;
    protected Integer stock;
    protected String description;
    protected String creatorId;
    protected String businessId;
    protected String updatedBy;
    protected String currency;
    protected String unit;
    protected List<String> imgS3UrlKeys = new ArrayList<>();
    @NonNull
    protected String type;
    protected List<ImageMetadata> imagesMetadata =new ArrayList<>();

    @DynamoDbPartitionKey
    @DynamoDbSecondaryPartitionKey(indexNames = "business-id")
    public String getBusinessId(){return this.businessId;}

    @DynamoDbSortKey
    @DynamoDbSecondarySortKey(indexNames = "LatestIndex")
    public String getCreatedAt(){return this.createdAt;}

    @DynamoDbSecondaryPartitionKey(indexNames = "updated-at")
    public String getUpdatedAt(){return this.updatedAt;}


    @DynamoDbSecondaryPartitionKey(indexNames = "name")
    public String getName(){return this.name;}

    @DynamoDbSecondaryPartitionKey(indexNames = "offering-id")
    public String getId(){return this.id;}

    @DynamoDbSecondaryPartitionKey(indexNames = "creator-id")
    public String getCreatorId(){return this.creatorId;}

    @DynamoDbSecondaryPartitionKey(indexNames = "updated-by")
    public String getUpdatedBy(){return this.updatedBy;}

    @DynamoDbSecondaryPartitionKey(indexNames = "price")
    public Double getPrice(){return this.price;}

    @DynamoDbSecondaryPartitionKey(indexNames = "stock")
    public Integer getStock(){return this.stock;}

    @DynamoDbAttribute("type")
    @DynamoDbSecondaryPartitionKey(indexNames = "LatestIndex")
    public String getType(){return this.type;}

    @DynamoDbSecondaryPartitionKey(indexNames = "unit")
    public String getUnit(){return this.unit;}

    @DynamoDbSecondaryPartitionKey(indexNames = "currency")
    public String getCurrency(){return this.currency;}

    //Products
    private ProductProperties product;
    //wearables
    private WearableProperties wearable;

}
