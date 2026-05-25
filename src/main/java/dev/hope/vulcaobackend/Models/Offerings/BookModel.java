package dev.hope.vulcaobackend.Models.Offerings;


import dev.hope.vulcaobackend.Models.Common.BaseOfferingModel;
import dev.hope.vulcaobackend.Models.Properties.BookFormat;
import dev.hope.vulcaobackend.Models.Properties.Genres;
import dev.hope.vulcaobackend.Models.Properties.Languages;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;


import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
public class BookModel extends BaseOfferingModel {
    @NotBlank
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



    @DynamoDbSecondaryPartitionKey(indexNames = "book-publisher-index")
    public String getPublisher(){return this.publisher;}

    //private ArrayList<ReviewModel> reviews = new ArrayList<>();

}
