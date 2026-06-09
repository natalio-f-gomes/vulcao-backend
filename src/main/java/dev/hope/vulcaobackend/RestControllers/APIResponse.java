package dev.hope.vulcaobackend.RestControllers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class APIResponse {
    private String message;
    private String error;
    private Integer status;
    private LocalDateTime timestamp;
}
