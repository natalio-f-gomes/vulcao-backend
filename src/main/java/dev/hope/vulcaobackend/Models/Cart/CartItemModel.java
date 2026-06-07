package dev.hope.vulcaobackend.Models.Cart;


import dev.hope.vulcaobackend.Models.Common.BaseModelEntities;
import dev.hope.vulcaobackend.Models.Properties.OfferingType;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
public class CartItemModel extends BaseModelEntities {
    @NonNull
    @NotBlank(message = "THE CART ID IS MANDATORY")
    private String cartId;
    private Integer quantity;
    private String itemId;
    private String companyId;
    private OfferingType itemType;

    @DynamoDbPartitionKey
    @DynamoDbSecondaryPartitionKey(indexNames = "id-index")
    public String getId() { return this.id; }

    @DynamoDbSecondaryPartitionKey(indexNames = "cart-id-index")
    public @org.jspecify.annotations.NonNull String getCartId() { return this.cartId; }

    @DynamoDbSecondaryPartitionKey(indexNames = "item-id-index")
    public String getItemId() { return this.itemId; }

    public void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

    public void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
