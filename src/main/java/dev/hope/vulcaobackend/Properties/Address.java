package dev.hope.vulcaobackend.Properties;

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
public class Address {
    private Country country;
    private String state;
    private String island;
    private String city;
    private String zone;
    private String street;
    private String postalCode;
}
