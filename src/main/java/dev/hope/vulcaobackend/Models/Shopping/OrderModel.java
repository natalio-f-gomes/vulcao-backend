package dev.hope.vulcaobackend.Models.Shopping;

import dev.hope.vulcaobackend.Models.Common.BaseModelEntities;
import dev.hope.vulcaobackend.Models.Properties.OrderStatus;
import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@DynamoDbBean
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderModel extends BaseModelEntities {
    private String userId;

    private List<OrderItemModel> orderItems = new ArrayList<>();

    @NonNull
    private List<CartItemModel> cartItems = new ArrayList<>();

    private OrderStatus status;
    private Double totalCost;
    private String trackId;

    @NonNull
    private String companyId;

    @DynamoDbPartitionKey
    public String getCompanyId() { return this.companyId; }

    @DynamoDbSecondaryPartitionKey(indexNames = "track-id-index")
    public String getTrackId() { return this.trackId; }

    @DynamoDbSecondaryPartitionKey(indexNames = "user-id-index")
    public String getUserId() { return this.userId; }

    @DynamoDbSecondaryPartitionKey(indexNames = "id-index")
    public String getId() { return this.id; }

    // SORT KEY — unique per order because it ends in the generated order id
    @DynamoDbSortKey
    public String getOrderKey() {
        return this.companyId + "#" + this.userId + "$" + this.id;
    }
    public void setOrderKey(String orderKey) {
        // no-operation
    }

    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

}