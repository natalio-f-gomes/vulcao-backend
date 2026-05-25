package dev.hope.vulcaobackend.Models.AccountEntities;


import dev.hope.vulcaobackend.Models.Properties.Permissions;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@DynamoDbBean
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StaffModel extends BaseAccountModel {
    @NotBlank
    private String creatorId;
    @NotBlank
    private String creatorUsername;
    @NotBlank
    private String creatorFullName;
    @NotBlank
    private String companyId;
    @NotBlank
    private String companyName;
    private String department;
    private String jobTitle;

    private Integer salary;
    private LocalDate hiredAt;
    private Set<Permissions> assignedPermissions;

    @DynamoDbSecondaryPartitionKey(indexNames = "creator-username-index")
    public String getCreatorUsername(){ return this.creatorUsername; }

    @DynamoDbSecondaryPartitionKey(indexNames = "creator-id-index")
    public String getCreatorId(){ return this.creatorId; }

    @DynamoDbSecondaryPartitionKey(indexNames = "creator-fullName-index")
    public String getCreatorFullName(){ return this.creatorFullName; }

    @DynamoDbSecondaryPartitionKey(indexNames = "company-id-index")
    public String getCompanyId(){ return this.companyId; }

    @DynamoDbSecondaryPartitionKey(indexNames = "company-name-index")
    public String getCompanyName(){ return this.companyName; }

    @DynamoDbSecondaryPartitionKey(indexNames = "staff-department-index")
    public String getDepartment(){ return this.department; }



}
