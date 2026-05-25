package dev.hope.vulcaobackend.DataTransferObjects.Offerings;


import dev.hope.vulcaobackend.DataTransferObjects.Common.BaseModelDTO;
import dev.hope.vulcaobackend.Models.Properties.*;
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
public class BookDTO extends BaseModelDTO {
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
    private String SBN;
    private List<String> authors;
    private String publisher;
    private String edition;
    private String volume;
    private Integer pageNumber;
    private Double weight;
    private List<String> keywords;
    private List<Genres> genres;
    private List<Languages> languages;
    private LocalDate publicationDate;
    private BookFormat format;
}
