package dev.hope.vulcaobackend.Mappers;


import dev.hope.vulcaobackend.DataTransferObjects.AccountDTOs.EmployerDTO;
import dev.hope.vulcaobackend.Models.AccountEntities.EmployerModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class EmployerMapper {

    public EmployerDTO transformData(EmployerModel employer){
        ArrayList<String> businesses = new ArrayList<>();

        return EmployerDTO.builder()
                        .id(employer.getId()).username(employer.getUsername())
                        .firstName(employer.getFirstName())
                        .lastName(employer.getLastName())
                        .username(employer.getUsername())
                        .email(employer.getEmail())
                        .phoneNumber(employer.getPhoneNumber())
                        .address(employer.getAddress()).build();

    }
}

