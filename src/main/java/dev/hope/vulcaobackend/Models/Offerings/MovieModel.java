package dev.hope.vulcaobackend.Models.Offerings;


import dev.hope.vulcaobackend.Models.Common.BaseOfferingModel;
import dev.hope.vulcaobackend.Models.Properties.AgeRating;
import dev.hope.vulcaobackend.Models.Properties.Countries;
import dev.hope.vulcaobackend.Models.Properties.Genres;
import dev.hope.vulcaobackend.Models.Properties.Languages;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

import java.time.LocalDate;
import java.util.Set;


@Getter
@Setter
@DynamoDbBean
@AllArgsConstructor
@NoArgsConstructor
public class MovieModel extends BaseOfferingModel {
    protected Set<String> directors;

    protected Set<String> leadActors;

    protected Set<String> producers;

    protected Set<Genres> genres;

    protected Set<Languages> languages;
    protected LocalDate releaseDate;
    protected Countries country;
    protected AgeRating ageRating;
    //private ArrayList<ReviewModel> reviews = new ArrayList<>();
}
