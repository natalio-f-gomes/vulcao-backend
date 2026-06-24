package dev.hope.vulcaobackend.Models;

import dev.hope.vulcaobackend.Models.Common.BaseModelEntities;
import dev.hope.vulcaobackend.Models.Properties.FeatureRequestCategory;
import dev.hope.vulcaobackend.Models.Properties.FeatureRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

import java.time.LocalDateTime;

/** This model serves as inquiry requests from people wanting to see more features */
@DynamoDbBean
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InquiryModel extends BaseModelEntities {
    private String userName;
    private String userEmail;
    private String description;
    private Integer votes;
    private String title;
    private FeatureRequestCategory category;
    private FeatureRequestStatus status;


    @DynamoDbPartitionKey
    @DynamoDbSecondaryPartitionKey(indexNames = "id-index")
    public String getId(){return this.id;}

    @DynamoDbSecondaryPartitionKey(indexNames = "email-index")
    public String getUserEmail(){return this.userEmail;}

    public void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

    public void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
