package dev.hope.vulcaobackend.Models.AccountEntities;


import dev.hope.vulcaobackend.DataTransferObjects.AccountDTOs.StaffDTO;
import dev.hope.vulcaobackend.DataTransferObjects.CompanyDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.ArrayList;
import java.util.List;

@DynamoDbBean
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployerModel extends BaseAccountModel{


    private List<CompanyDTO> companiesRegistered = new ArrayList<>();

    private List<String> taskList = new ArrayList<>(); // Feature to be implemented later on



}
