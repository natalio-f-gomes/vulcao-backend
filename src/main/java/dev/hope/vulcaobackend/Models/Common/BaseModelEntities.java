package dev.hope.vulcaobackend.Models.Common;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseModelEntities {
    @NonNull
    protected String id;
    @NonNull
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;

    //private List<String> imagesUrl; //Maximum of 5 images per entity


}
