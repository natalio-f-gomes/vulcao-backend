package dev.hope.vulcaobackend.Models;

import dev.hope.vulcaobackend.Models.Common.BaseModelEntities;
import dev.hope.vulcaobackend.Models.Offerings.BookModel;
import dev.hope.vulcaobackend.Models.Properties.TaskPriority;
import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

import java.time.Duration;
import java.time.LocalDate;

@DynamoDbBean
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskModel extends BaseModelEntities {
    @NonNull
    private String title;
    private String description;
    private TaskPriority priority;

    private LocalDate startDate;
    private LocalDate deadline;
    private LocalDate completionDate;

    private Boolean pastDue;

    @NonNull
    private String creatorId;
    @NonNull
    private String companyId;

    private Duration duration;
    private Duration estimatedTime;

    @DynamoDbSecondaryPartitionKey(indexNames = "creator-id-index")
    public String getCreatorId(){return this.creatorId;}


    @DynamoDbSecondaryPartitionKey(indexNames = "title-index")
    public String getTitle(){return this.title;}

    @DynamoDbSecondaryPartitionKey(indexNames = "company-id-index")
    public String getCompanyId(){return this.companyId;}

    @DynamoDbPartitionKey
    @DynamoDbSecondaryPartitionKey(indexNames = "id-index")
    public String getId() { return this.id; }


}
