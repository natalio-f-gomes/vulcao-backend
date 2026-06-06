package dev.hope.vulcaobackend.DataTransferObjects;


import dev.hope.vulcaobackend.Models.Properties.OfferingType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UploadImageRequest {
    private String offeringId;
    private OfferingType offeringType;
    List<ImageMetadata> imageMetadataList = new ArrayList<>();
}


