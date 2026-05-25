package dev.hope.vulcaobackend.DataTransferObjects;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCredentials {
    @NotBlank
    @Size(min = 8, max = 50)
    private String username;

    @NotBlank
    @Size(min = 8, max = 128)
    private String password;

}
