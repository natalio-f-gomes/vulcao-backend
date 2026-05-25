package dev.hope.vulcaobackend.DataTransferObjects.Common;


import dev.hope.vulcaobackend.Models.Common.Address;
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
    private String email;
    private String phoneNumber;
    private Address address;
}
