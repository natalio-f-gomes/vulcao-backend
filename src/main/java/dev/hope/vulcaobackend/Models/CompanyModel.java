package dev.hope.vulcaobackend.Models;


import dev.hope.vulcaobackend.DataTransferObjects.ImageMetadata;
import dev.hope.vulcaobackend.Models.Common.Address;
import dev.hope.vulcaobackend.Models.Common.BaseModelEntities;
import dev.hope.vulcaobackend.Models.Properties.BusinessCategory;
import dev.hope.vulcaobackend.Models.Properties.CompanyStatuses;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
public class CompanyModel extends BaseModelEntities {
    @NotBlank
    private String name;
    @NotNull(message = "Category is mandatory")
    private BusinessCategory category;
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
    private CompanyStatuses status;

    @NotBlank
    private String creatorUsername;

    @DynamoDbSecondaryPartitionKey(indexNames = "company-category-index")
    public BusinessCategory getCategory() { return category; }

    @DynamoDbSecondaryPartitionKey(indexNames = "company-creator-index")
    public String getCreatorUsername() { return creatorUsername; }

    @DynamoDbSecondaryPartitionKey(indexNames = "company-name-index")
    public String getName() { return name; }

    @DynamoDbPartitionKey
    @DynamoDbSecondaryPartitionKey(indexNames = "company-id-index")
    public String getId() { return this.id; }

    @DynamoDbSecondaryPartitionKey(indexNames = "company-email-index")
    public String getEmail() { return email; }

    @DynamoDbSecondaryPartitionKey(indexNames = "company-taxId-index")
    public String getTaxIdNumber() { return taxIdNumber; }

    public void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

    public void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }


}



