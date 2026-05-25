package dev.hope.vulcaobackend.Repositories.Offerings;

import dev.hope.vulcaobackend.Models.Offerings.MovieModel;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.CreateTableEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.EnhancedGlobalSecondaryIndex;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.ProjectionType;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;

import java.util.List;
import java.util.Optional;

@Repository
public class MovieRepository {

    Logger logger = LoggerFactory.getLogger(MovieRepository.class);

    private final DynamoDbTable<MovieModel> movieTable;

    public MovieRepository(DynamoDbEnhancedClient enhancedClient) {
        logger.info("MovieRepository init() running, creating Movie table");
        this.movieTable = enhancedClient.table("Movies", TableSchema.fromBean(MovieModel.class));
    }

    @PostConstruct
    public void init() {
        logger.info("Movie post init() running, creating Table Schemas");
        try {
            movieTable.createTable(CreateTableEnhancedRequest.builder()
                    .globalSecondaryIndices(
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("creator-id-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("creator-name-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("company-id-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("company-name-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("item-price-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("item-name-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build()
                            )
                    .build());
            logger.info("Successfully finished creating Movie Table Schemas");
        } catch (ResourceInUseException e) {
            // table already exists
            logger.debug("TABLE MOVIE ALREADY EXISTS" + e);
        }
    }

    public void save(MovieModel newMovie) {  movieTable.putItem(r -> r.item(newMovie).conditionExpression(Expression.builder().expression("atribute_not_exists(id").build())); }

    public void update(MovieModel updatedMovie) { movieTable.putItem(updatedMovie); }


    public List<MovieModel> findByCreatorUsername(String creatorName) {
        return movieTable.index("creator-name-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(creatorName)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }
    public List<MovieModel> findByCreatorId(String id) {
        return movieTable.index("creator-id-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(id)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }

    public List<MovieModel> findByName(String productName) {
        return movieTable.index("item-name-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(productName)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }

    public Optional<MovieModel> findById(String id) {
        return movieTable.index("offering-id-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(id)))
                .stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
    }

    public List<MovieModel> findByCompanyName(String companyName) {
        return movieTable.index("company-name-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(companyName)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }

    public List<MovieModel> findByCompanyId(String companyId) {
        return movieTable.index("company-id-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(companyId)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }

    public List<MovieModel> findByPrice(Integer itemPrice) {
        return movieTable.index("item-price-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(itemPrice)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }



    public void deleteMovie(MovieModel movie){
        movieTable.deleteItem(movie);
    }

    public List<MovieModel> findAll() {
        return movieTable.scan()
                .items()
                .stream()
                .toList();
    }

}
