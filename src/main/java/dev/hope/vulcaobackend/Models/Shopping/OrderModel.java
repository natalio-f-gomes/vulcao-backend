package dev.hope.vulcaobackend.Models.Shopping;

import dev.hope.vulcaobackend.Models.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.util.ArrayList;
import java.util.List;

@DynamoDbBean
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderModel extends BaseModel {

    List<OrderItem> items = new ArrayList<>();
    private String clientId;
    private String trackId;
    private String businessId;
    private Double totalCost;
    private String note;
    private String status;
    private Boolean canceled = false;

    @DynamoDbPartitionKey
    public String getClientId() { return this.clientId; }

    @DynamoDbSecondaryPartitionKey(indexNames = "order-id")
    public String getId() { return this.id; }

   @DynamoDbSecondaryPartitionKey(indexNames = "track-id")
   public String getTrackId(){return this.trackId;}

    @DynamoDbSecondaryPartitionKey(indexNames = "business-id")
    public String getBusinessId(){return this.businessId;}

    @DynamoDbSecondaryPartitionKey(indexNames = "total-cost")
    public Double getTotalCost(){return this.totalCost;}

    @DynamoDbSecondaryPartitionKey(indexNames = "status")
    public String getStatus(){return this.status;}

    @DynamoDbSortKey
    public String getCreatedAt(){return this.createdAt;}

    @DynamoDbSecondaryPartitionKey(indexNames = "updated-at")
    public String getUpdatedAt(){return this.updatedAt;}


}
