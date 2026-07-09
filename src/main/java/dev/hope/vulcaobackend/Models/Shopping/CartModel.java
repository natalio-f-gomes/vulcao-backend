package dev.hope.vulcaobackend.Models.Shopping;

import dev.hope.vulcaobackend.Models.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.ArrayList;
import java.util.List;

@DynamoDbBean
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartModel extends BaseModel {
    //the user id is the cart id
    List<CartItem> items = new ArrayList<>();

    @DynamoDbPartitionKey
    public String getId() { return this.id; }

}
