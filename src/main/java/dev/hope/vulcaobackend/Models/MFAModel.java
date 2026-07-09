package dev.hope.vulcaobackend.Models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

import java.time.LocalDateTime;

@DynamoDbBean
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MFAModel {
    private String userEmail;
    private String authCode;
    private LocalDateTime expirationTime;
    private LocalDateTime sendTime;

    @DynamoDbPartitionKey
    @DynamoDbSecondaryPartitionKey(indexNames = "user-email-index")
    public String getUserEmail(){return this.userEmail;}

    @DynamoDbSecondaryPartitionKey(indexNames = "auth-code-index")
    public String getAuthCode(){return this.authCode;}


}
