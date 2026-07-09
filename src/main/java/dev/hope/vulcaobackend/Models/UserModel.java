package dev.hope.vulcaobackend.Models;

import dev.hope.vulcaobackend.Properties.Address;
import dev.hope.vulcaobackend.Properties.Permissions;
import lombok.*;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Set;

@DynamoDbBean
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserModel extends BaseModel{

    private String username;
    private String email;
    private String firstName, lastName;
    private String password;
    private String userType;
    private String phoneNumber;



    //Staff Only fields
    private String creatorId;
    private String companyId;
    private Integer salary;
    private LocalDate hiredAt;
    private Set<Permissions> assignedPermissions;
    private String department;
    private String jobTitle;

    private Address address;

    @DynamoDbPartitionKey
    public String getId() { return this.id; }

    @DynamoDbSecondaryPartitionKey(indexNames = "email-index")
    public String getEmail() { return this.email; }

    @DynamoDbSecondaryPartitionKey(indexNames = "username-index")
    public String getUsername() { return this.username; }

    @DynamoDbSecondaryPartitionKey(indexNames = "user-type-index")
    public String getUserType() { return this.userType; }

    @DynamoDbSecondaryPartitionKey(indexNames = "staff-creator-id")
    public String getCreatorId() { return this.creatorId; }

    @DynamoDbSecondaryPartitionKey(indexNames = "staff-company-id")
    public String getCompanyId() { return this.companyId; }

    @DynamoDbSecondaryPartitionKey(indexNames = "staff-department")
    public String getDepartment() { return this.department; }

    @DynamoDbSecondaryPartitionKey(indexNames = "staff-job-title")
    public String getJobTitle() { return this.jobTitle; }





}
