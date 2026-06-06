package dev.hope.vulcaobackend.DataTransferObjects.Offerings;

import dev.hope.vulcaobackend.DataTransferObjects.Common.BaseModelDTO;
import dev.hope.vulcaobackend.Models.Properties.Currency;
import dev.hope.vulcaobackend.Models.Properties.ProductType;
import dev.hope.vulcaobackend.Models.Properties.UnitTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO extends BaseModelDTO {
    private LocalDate expirationDate;
    private ProductType productType;
    private String name;
    private String description;
    private Double pricePerUnit;
    private UnitTypes unitType;
    private Currency currency;
    private Integer stock;
    private String creatorUsername;
    private String creatorId;
    private String updatedBy;
    private String companyId;
    private String companyName;
    private List<String> imgS3UrlKeys;
}
