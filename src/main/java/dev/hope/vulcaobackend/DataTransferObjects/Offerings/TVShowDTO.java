package dev.hope.vulcaobackend.DataTransferObjects.Offerings;


import dev.hope.vulcaobackend.DataTransferObjects.Common.BaseModelDTO;
import dev.hope.vulcaobackend.Models.Properties.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TVShowDTO extends BaseModelDTO {
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

    protected Set<String> directors;

    protected Set<String> leadActors;

    protected Set<String> producers;

    protected String title;

    protected Set<Genres> genres;

    protected Set<Languages> languages;
    protected LocalDate releaseDate;
    protected Countries country;
    protected AgeRating ageRating;

    protected String trailerUrl;
    private Integer runtime;

    private Integer seasons;
    private Integer episodes;
    private Set<String> writers;
    private MediaStatus status;
    private MediaType mediaType;
    private Integer episodeRuntime;
}
