package dev.hope.vulcaobackend.Models.AccountEntities;


import dev.hope.vulcaobackend.Models.Common.Address;
import dev.hope.vulcaobackend.Models.Common.BaseModelEntities;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;


import java.time.LocalDateTime;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
public class BaseAccountModel extends BaseModelEntities {
    private String firstName;
    private String lastName;

    @Size(min = 8, max = 20)
    private String username;

    @Email
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    private String phoneNumber;
    private Address address;

    @DynamoDbPartitionKey
    @DynamoDbSecondaryPartitionKey(indexNames = "id-index")
    public String getId() { return this.id; }

    @DynamoDbSecondaryPartitionKey(indexNames = "email-index")
    public String getEmail() { return this.email; }

    @DynamoDbSecondaryPartitionKey(indexNames = "username-index")
    public String getUsername() { return this.username; }

    public void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

    public void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}