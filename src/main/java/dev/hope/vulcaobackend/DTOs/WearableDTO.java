package dev.hope.vulcaobackend.DTOs;

import dev.hope.vulcaobackend.Models.BaseModel;
import dev.hope.vulcaobackend.Models.Offerings.OfferingModel;
import dev.hope.vulcaobackend.Properties.WearableType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
public class WearableDTO extends OfferingModel {
    private String brand;
    private String colors;
    private String material;
    private String size;
    private WearableType wearableType;
}
