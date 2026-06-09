package dev.hope.vulcaobackend.Models.Shopping;


import dev.hope.vulcaobackend.Models.Common.BaseModelEntities;
import dev.hope.vulcaobackend.Models.Properties.OfferingType;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
public class CartItemModel extends BaseModelEntities {

    private Integer quantity = 0;

    @NonNull
    private String userId;

    @NonNull
    @NotBlank(message = "THE COMPANY ID IS MANDATORY")
    private String companyId;

    private OfferingType itemType;

    @DynamoDbSecondaryPartitionKey(indexNames = "id-index")
    public String getId() { return this.id; }

    @DynamoDbPartitionKey
    @DynamoDbSecondaryPartitionKey(indexNames = "user-id-index")
    public String getUserId() { return this.userId; }

    // Composite sort key: "<companyId>#<productId>".
    // Makes each product a distinct row per user, and lets us fetch all of a
    // company's items with begins_with("<companyId>#").
    @DynamoDbSortKey
    public String getCompanyItem() {
        return this.companyId + "#" + this.id;
    }
    public void setCompanyItem(String companyItem) {
        // derived from companyId + id on write; nothing to store on read
    }


    @DynamoDbSecondaryPartitionKey(indexNames = "company-id-index")
    public String getCompanyId() { return this.companyId; }

    public void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

    public void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
