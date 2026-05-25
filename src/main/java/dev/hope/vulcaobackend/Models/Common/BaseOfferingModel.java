package dev.hope.vulcaobackend.Models.Common;


import dev.hope.vulcaobackend.Models.Properties.Currency;
import dev.hope.vulcaobackend.Models.Properties.UnitTypes;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseOfferingModel extends BaseModelEntities{
    protected String name;
    protected String description;
    @Min(0)
    protected Double pricePerUnit;
    protected UnitTypes unitType;
    protected Currency currency;
    protected Integer stock;
    protected String creatorUsername;
    protected String creatorId;
    protected String updatedBy;

    protected String companyId;
    protected String companyName;


    @DynamoDbSecondaryPartitionKey(indexNames = "company-name-index")
    public String getCompanyName(){return this.companyName;}

    @DynamoDbSecondaryPartitionKey(indexNames = "company-id-index")
    public String getCompanyId(){return this.companyId;}

    @DynamoDbSecondaryPartitionKey(indexNames = "creator-name-index")
    public String getCreatorUsername(){return this.creatorUsername;}


    @DynamoDbSecondaryPartitionKey(indexNames = "creator-id-index")
    public String getCreatorId(){return this.creatorId;}

    @DynamoDbSecondaryPartitionKey(indexNames = "item-price-index")
    public Double getPricePerUnit(){return this.pricePerUnit;}

    @DynamoDbSecondaryPartitionKey(indexNames = "item-name-index")
    public String getName(){return this.name;}

    @DynamoDbPartitionKey
    @DynamoDbSecondaryPartitionKey(indexNames = "offering-id-index")
    public String getId(){return this.id;}

    public void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

    public void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

}
