package dev.hope.vulcaobackend.DataTransferObjects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {
    private String newPassword;
}
