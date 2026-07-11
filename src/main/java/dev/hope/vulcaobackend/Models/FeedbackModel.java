package dev.hope.vulcaobackend.Models;


import dev.hope.vulcaobackend.Properties.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackModel extends BaseModel{
    private String title;
    private String description;
    private String email;
    private String name;
    private String status = "ANALISANDO";
    private Integer votes = 0;
    private String feedbackType;

    @DynamoDbPartitionKey
    public String getId(){return this.id;}

    @DynamoDbSortKey
    @DynamoDbSecondarySortKey(indexNames = "LatestIndex")
    public String getCreatedAt(){return this.createdAt;}

    @DynamoDbSecondaryPartitionKey(indexNames = "updated-at")
    public String getUpdatedAt(){return this.updatedAt;}

    @DynamoDbSecondaryPartitionKey(indexNames = "title")
    public String getTitle(){return this.title;}

    @DynamoDbSecondaryPartitionKey(indexNames = "email")
    public String getEmail(){return this.email;}

    @DynamoDbSecondaryPartitionKey(indexNames = "status")
    public String getStatus(){return this.status;}

    @DynamoDbSecondaryPartitionKey(indexNames = "votes")
    public Integer getVotes(){return this.votes; }

    @DynamoDbSecondaryPartitionKey(indexNames = "LatestIndex")
    public String getFeedbackType(){return this.feedbackType; }

    @DynamoDbSecondaryPartitionKey(indexNames = "name")
    public String getName(){return this.name; }


}
