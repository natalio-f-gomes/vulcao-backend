package dev.hope.vulcaobackend.DataTransferObjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserFullName {
    private String firstName;
    private String lastName;

}
