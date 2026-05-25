package dev.hope.vulcaobackend.Models.Common;

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
    private String island;
    private String city;
    private String zone;
    private String street;
    private String postalCode;
}
