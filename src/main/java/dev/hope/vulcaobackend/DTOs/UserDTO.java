package dev.hope.vulcaobackend.DTOs;

import dev.hope.vulcaobackend.Models.BaseModel;
import dev.hope.vulcaobackend.Properties.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
public class UserDTO extends BaseModel {
    private String username;
    private String email;
    private String firstName, lastName;
    private String password;
    private String userType;
    private String phoneNumber;
    private Address address;

}
