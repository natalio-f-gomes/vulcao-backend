package dev.hope.vulcaobackend.Models.Offerings;

import dev.hope.vulcaobackend.Properties.WearableType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.List;

@DynamoDbBean
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WearableProperties {
    private String brand;
    private String colors;
    private String material;
    private String size;
    private WearableType wearableType;
}
