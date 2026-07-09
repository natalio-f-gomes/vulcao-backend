package dev.hope.vulcaobackend.DTOs;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
public class OrderRequest {
    List<OrderItemRequest> items;
    String note;

    public record OrderItemRequest(String id, Integer quantity, String name, String businessId){}
}


