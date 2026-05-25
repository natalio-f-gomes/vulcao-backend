package dev.hope.vulcaobackend.DataTransferObjects.AccountDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;


@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployerDTO extends BaseAccountDTO{
    private List<String> companiesRegistered;


}
