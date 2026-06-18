package dev.hope.vulcaobackend.Models.Common;

import dev.hope.vulcaobackend.Models.Properties.CvIslands;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@Getter
@Setter
@AllArgsConstructor
@DynamoDbBean
@NoArgsConstructor
public class Address {
    private CvIslands island;
    private String city;
    private String zone;
    private String street;
    private String postalCode;
}
