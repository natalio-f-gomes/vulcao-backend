package dev.hope.vulcaobackend.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateImageRequest {
    private List<ImageMetadata> imagesMetadata;
}
