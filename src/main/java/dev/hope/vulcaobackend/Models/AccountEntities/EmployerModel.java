package dev.hope.vulcaobackend.Models.AccountEntities;


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


    private List<String> companiesRegistered = new ArrayList<>();

    private List<String> staffList = new ArrayList<>();

    private List<String> taskList = new ArrayList<>();



}
