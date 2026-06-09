package dev.hope.vulcaobackend.Models.Shopping;

import dev.hope.vulcaobackend.Models.Common.BaseModelEntities;
import dev.hope.vulcaobackend.Models.Properties.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.ArrayList;
import java.util.List;

@DynamoDbBean
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderModel extends BaseModelEntities {
    private String userId;
    private List<CartItemModel> items = new ArrayList<>();
    private OrderStatus status;
    private Double totalCost;
    private String trackNumber;
    private String companyId;
}
