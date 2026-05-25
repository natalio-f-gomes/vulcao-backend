package dev.hope.vulcaobackend.DataTransferObjects.Offerings;


import dev.hope.vulcaobackend.DataTransferObjects.Common.BaseModelDTO;
import dev.hope.vulcaobackend.Models.Properties.Currency;
import dev.hope.vulcaobackend.Models.Properties.UnitTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MusicDTO extends BaseModelDTO {
    protected String name;
    protected String description;
    protected Double pricePerUnit;
    protected UnitTypes unitType;
    protected Currency currency;
    protected Integer stock;
    protected String creatorUsername;
    protected String creatorId;
    protected String updatedBy;

    protected String companyId;
    protected String companyName;

    private String albumName;
    private Set<String> artistName;

    private List<String> featuredArtists;
    private String recordLabel;
    private List<String> writers;
}
