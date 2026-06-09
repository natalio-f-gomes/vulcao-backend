package dev.hope.vulcaobackend.Models.Shopping;

import dev.hope.vulcaobackend.Models.Common.BaseModelEntities;
import dev.hope.vulcaobackend.Models.Properties.OrderStatus;
import lombok.*;
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
public class OrderModel extends BaseModelEntities {
    @NonNull
    private String userId;

    @NonNull
    private List<CartItemModel> items = new ArrayList<>();

    private OrderStatus status;

    private Double totalCost;

    private String trackId;
    @NonNull
    private String companyId;

    @DynamoDbPartitionKey
    public String getCompanyId() {return this.companyId;}

    @DynamoDbSecondaryPartitionKey(indexNames = "track-id-index")
    public String getTrackId(){return this.trackId;}

    @DynamoDbSecondaryPartitionKey(indexNames = "id-index")
    public String getId(){return this.id;}

    @DynamoDbSortKey
    public String getCompanyItem() {
        return this.companyId + "#" + this.userId;
    }
    public void setCompanyItem(String companyItem) {
        // derived from companyId + id on write; nothing to store on read
    }

}
