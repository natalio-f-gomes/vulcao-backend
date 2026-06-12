package dev.hope.vulcaobackend.DataTransferObjects.AccountDTOs;


import dev.hope.vulcaobackend.Models.Properties.Permissions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@DynamoDbBean // since I am creating a list of staff dto in employer model, i need this annotation
public class StaffDTO extends BaseAccountDTO {

    private String creatorUsername;
    private String companyId;
    private String companyName;
    private String department;
    private String jobTitle;

    private Integer salary;
    private LocalDate hiredAt;
    private Set<Permissions> assignedPermissions;


}
