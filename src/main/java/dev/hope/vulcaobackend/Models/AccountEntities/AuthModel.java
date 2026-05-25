package dev.hope.vulcaobackend.Models.AccountEntities;



import dev.hope.vulcaobackend.Models.Common.BaseModelEntities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cglib.core.Local;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
public class AuthModel extends BaseModelEntities {

    private String ipAddress;
    private String accessToken;
    private String username;
    private String email;

    private String randomCode;
    private LocalDateTime issuedAt;
    private Integer duration;

    @DynamoDbSortKey
    public LocalDateTime getIssuedAt(){ return issuedAt;    }


    @DynamoDbSecondaryPartitionKey(indexNames = "ipAddress-index")
    public String getIpAddress(){
        return this.ipAddress;
    }


    @DynamoDbSecondaryPartitionKey(indexNames = "user-email-index")
    public String getEmail(){
        return this.email;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "auth-mfa-code-index")
    public String getRandomCode(){
        return this.randomCode;
    }

    @DynamoDbPartitionKey
    public String getUsername(){
        return this.username;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "duration-index")
    public Integer getDuration(){
        return this.duration;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "user-access-token-index")
    public String getAccessToken(){
        return this.accessToken;
    }
}
