package dev.hope.vulcaobackend.Models.Shopping;

import dev.hope.vulcaobackend.Models.Common.BaseModelEntities;
import dev.hope.vulcaobackend.Models.Properties.OfferingType;
import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DynamoDbBean
public class OrderItemModel extends BaseModelEntities {
    private String companyId;
    private String userId;
    private String itemName;
    private Integer quantity;
    private Double priceAtPurchase;
    private OfferingType itemType;

    @DynamoDbPartitionKey()
    @DynamoDbSecondaryPartitionKey(indexNames = "id-index")
    public String getId() { return this.id; }

    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}