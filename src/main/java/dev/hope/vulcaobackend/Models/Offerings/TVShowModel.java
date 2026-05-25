package dev.hope.vulcaobackend.Models.Offerings;


import dev.hope.vulcaobackend.Models.Common.BaseOfferingModel;
import dev.hope.vulcaobackend.Models.Properties.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

import java.time.LocalDate;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
public class TVShowModel extends BaseOfferingModel {
    protected Set<String> directors;

   protected Set<String> leadActors;

   protected Set<String> producers;

    protected String title;
    //
    protected Set<Genres> genres;
    //
    protected Set<Languages> languages;
    protected LocalDate releaseDate;
    protected Countries country;
    protected AgeRating ageRating;


    // protected List<String> studios;
    //protected String poster;
    protected String trailerUrl;
    private Integer runtime;

    private Integer seasons;
    private Integer episodes;
    private Set<String> writers;
    private MediaStatus status;
    private MediaType mediaType;
    private Integer episodeRuntime;

    @DynamoDbSecondaryPartitionKey(indexNames = "show-episode-index")
    public Integer getEpisodes(){return this.episodes;}


    //private ArrayList<ReviewModel> reviews = new ArrayList<>();
}
