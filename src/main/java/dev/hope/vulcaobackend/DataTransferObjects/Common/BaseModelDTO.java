package dev.hope.vulcaobackend.DataTransferObjects.Common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class BaseModelDTO {
    private String id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
