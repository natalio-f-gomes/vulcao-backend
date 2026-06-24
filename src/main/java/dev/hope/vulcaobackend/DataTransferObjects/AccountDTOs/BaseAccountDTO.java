package dev.hope.vulcaobackend.DataTransferObjects.AccountDTOs;


import dev.hope.vulcaobackend.DataTransferObjects.Common.BaseModelDTO;
import dev.hope.vulcaobackend.Models.Common.Address;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BaseAccountDTO extends BaseModelDTO {
    private String firstName;
    private String lastName;
    private String username;
    @Email(message = "Invalid email format")
    private String email;
    private String phoneNumber;
    private Address address;
    private String fullName;
}
