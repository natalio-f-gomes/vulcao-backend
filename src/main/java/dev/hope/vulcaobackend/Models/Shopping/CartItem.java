package dev.hope.vulcaobackend.Models.Shopping;

import dev.hope.vulcaobackend.Models.BaseModel;
import dev.hope.vulcaobackend.Models.Offerings.OfferingModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@DynamoDbBean
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CartItem extends BaseModel {
    //the id is the item Id;
    private OfferingModel offering;
    private Integer quantity;

}
