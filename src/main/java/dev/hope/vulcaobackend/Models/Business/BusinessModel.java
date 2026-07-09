package dev.hope.vulcaobackend.Models.Business;

import dev.hope.vulcaobackend.DTOs.ImageMetadata;
import dev.hope.vulcaobackend.Models.BaseModel;
import dev.hope.vulcaobackend.Properties.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
public class BusinessModel extends BaseModel {
    @NotBlank
    private String name;
    @NotNull(message = "Category is mandatory")
    private String type;
    private String taxIdNumber;
    private String description;
    @NotBlank
    private String phoneNumber;
    private String faxNumber;
    private Double revenue;
    @NotBlank
    private String email;
    private List<String> imgS3UrlKeys = new ArrayList<>();

    private List<ImageMetadata> imagesMetadata = new ArrayList<>();

    private Address address;
    private String status;

    private WeeklySchedule weeklySchedule;

    @NotBlank
    private String creatorId;

    @DynamoDbAttribute("weeklySchedule")
    public WeeklySchedule getWeeklySchedule() {
        return weeklySchedule;
    }


    @DynamoDbPartitionKey
    public String getId() { return this.id; }

    @DynamoDbSecondaryPartitionKey(indexNames = "name")
    public String getName() { return this.name; }


    @DynamoDbSecondaryPartitionKey(indexNames = "email")
    public String getEmail() { return this.email; }

    @DynamoDbSecondaryPartitionKey(indexNames = "creator-id")
    public String getCreatorId() { return this.creatorId; }

    @DynamoDbSecondaryPartitionKey(indexNames = "status")
    public String getStatus() { return this.status; }

    @DynamoDbSecondaryPartitionKey(indexNames = "type")
    public String getType() { return this.type; }

    @DynamoDbSortKey
    public String getCreatedAt(){return this.createdAt;}

    @DynamoDbSecondaryPartitionKey(indexNames = "updated-at")
    public String getUpdatedAt(){return this.updatedAt;}






}
