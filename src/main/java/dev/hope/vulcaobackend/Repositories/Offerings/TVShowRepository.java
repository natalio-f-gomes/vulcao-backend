package dev.hope.vulcaobackend.Repositories.Offerings;

import dev.hope.vulcaobackend.Models.Offerings.TVShowModel;
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
public class TVShowRepository {

    Logger logger = LoggerFactory.getLogger(TVShowRepository.class);

    private final DynamoDbTable<TVShowModel> tvShowTable;

    public TVShowRepository(DynamoDbEnhancedClient enhancedClient) {
        this.tvShowTable = enhancedClient.table("TvShows", TableSchema.fromBean(TVShowModel.class));
    }

    @PostConstruct
    public void init() {
        logger.info("TV SHOW repository init() running, creating TV SHOW table");
        try {
            tvShowTable.createTable(CreateTableEnhancedRequest.builder()
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
                                    .build(),
                            // Add missing indexes
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("offering-id-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("show-episode-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build()
                    )
                    .build());
            logger.info("TV SHOW table created successfully with all indexes");
        } catch (ResourceInUseException e) {
            logger.debug("TABLE TvShows ALREADY EXISTS: " + e.getMessage());
        }
    }

    public void save(TVShowModel newShow) {  tvShowTable.putItem(r -> r.item(newShow).conditionExpression(Expression.builder().expression("atribute_not_exists(id").build())); }

    public void update(TVShowModel updatedShow) {
        tvShowTable.putItem(updatedShow);
    }

    public List<TVShowModel> findByCreatorUsername(String creatorName) {
        return tvShowTable.index("creator-name-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(creatorName)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }
    public List<TVShowModel> findByCreatorId(String id) {
        return tvShowTable.index("creator-id-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(id)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }

    public List<TVShowModel> findByName(String productName) {
        return tvShowTable.index("item-name-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(productName)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }

    public Optional<TVShowModel> findById(String id) {
        return tvShowTable.index("offering-id-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(id)))
                .stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
    }

    public List<TVShowModel> findByCompanyName(String companyName) {
        return tvShowTable.index("company-name-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(companyName)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }

    public List<TVShowModel> findByCompanyId(String companyId) {
        return tvShowTable.index("company-id-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(companyId)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }

    public List<TVShowModel> findByPrice(Integer itemPrice) {
        return tvShowTable.index("item-price-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(itemPrice)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }



    public void deleteTvShow(TVShowModel tvShow){
        tvShowTable.deleteItem(tvShow);
    }

    public List<TVShowModel> findAll() {
        return tvShowTable.scan()
                .items()
                .stream()
                .toList();
    }

}
