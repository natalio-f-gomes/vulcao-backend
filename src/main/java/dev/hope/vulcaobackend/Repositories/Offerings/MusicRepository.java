package dev.hope.vulcaobackend.Repositories.Offerings;


import dev.hope.vulcaobackend.Models.Offerings.MusicModel;
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
public class MusicRepository {

    Logger logger = LoggerFactory.getLogger(MusicRepository.class);

    private final DynamoDbTable<MusicModel> musicTable;

    public MusicRepository(DynamoDbEnhancedClient enhancedClient) {
        logger.info("MusicRepository init() running, creating Music table");
        this.musicTable = enhancedClient.table("Music", TableSchema.fromBean(MusicModel.class));
    }

    @PostConstruct
    public void init() {
        try {
            musicTable.createTable(CreateTableEnhancedRequest.builder()
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
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("album-name-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build()
                            )
                    .build());
        } catch (ResourceInUseException e) {
            // table already exists
            logger.debug("TABLE Music ALREADY EXISTS" + e);
        }
    }

    public void save(MusicModel newMusic) {  musicTable.putItem(r -> r.item(newMusic).conditionExpression(Expression.builder().expression("atribute_not_exists(id").build())); }

    public void update(MusicModel updatedMusic) {
        musicTable.putItem(updatedMusic);
    }

    public List<MusicModel> findByCreatorUsername(String creatorName) {
        return musicTable.index("creator-name-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(creatorName)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }
    public List<MusicModel> findByCreatorId(String id) {
        return musicTable.index("creator-id-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(id)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }

    public List<MusicModel> findByName(String productName) {
        return musicTable.index("item-name-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(productName)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }

    public Optional<MusicModel> findById(String id) {
        return musicTable.index("offering-id-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(id)))
                .stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
    }

    public List<MusicModel> findByCompanyName(String companyName) {
        return musicTable.index("company-name-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(companyName)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }

    public List<MusicModel> findByCompanyId(String companyId) {
        return musicTable.index("company-id-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(companyId)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }

    public List<MusicModel> findByPrice(Integer itemPrice) {
        return musicTable.index("item-price-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(itemPrice)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }

    public List<MusicModel> findByAlbumName(String albumName) {
        return musicTable.index("album-name-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(albumName)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }


    public void deleteMusic(MusicModel music){
        musicTable.deleteItem(music);
    }

    public List<MusicModel> findAll() {
        return musicTable.scan()
                .items()
                .stream()
                .toList();
    }

}
