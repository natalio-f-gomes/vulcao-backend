package dev.hope.vulcaobackend.Mappers;



import dev.hope.vulcaobackend.DataTransferObjects.AccountDTOs.StaffDTO;
import dev.hope.vulcaobackend.Models.AccountEntities.StaffModel;
import org.springframework.stereotype.Component;

@Component
public class StaffMapper {

    public StaffDTO transformData(StaffModel staff) {
        return StaffDTO.builder()
                // Fields from BaseModelDTO
                .id(staff.getId())
                .createdAt(staff.getCreatedAt())
                .updatedAt(staff.getUpdatedAt())

                // Fields from BaseAccountDTO
                .firstName(staff.getFirstName())
                .lastName(staff.getLastName())
                .username(staff.getUsername())
                .email(staff.getEmail())
                .phoneNumber(staff.getPhoneNumber())
                .address(staff.getAddress())

                // Fields from StaffDTO
                .creatorUsername(staff.getCreatorUsername())
                .companyId(staff.getCompanyId())
                .department(staff.getDepartment())
                .jobTitle(staff.getJobTitle())
                .hiredAt(staff.getHiredAt())
                .salary(staff.getSalary())
                .assignedPermissions(staff.getAssignedPermissions())
                .build();
    }
}