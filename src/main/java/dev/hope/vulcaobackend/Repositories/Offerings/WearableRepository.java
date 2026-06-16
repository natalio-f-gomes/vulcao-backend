package dev.hope.vulcaobackend.Repositories.Offerings;

import dev.hope.vulcaobackend.Models.Offerings.WearableModel;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.CreateTableEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.EnhancedGlobalSecondaryIndex;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.List;
import java.util.Optional;

@Repository
public class WearableRepository {


    private final DynamoDbClient dynamoDbClient;
    Logger logger = LoggerFactory.getLogger(WearableRepository.class);

    private final DynamoDbTable<WearableModel> table;

    public WearableRepository(DynamoDbEnhancedClient enhancedClient, DynamoDbClient dynamoDbClient) {
        this.table = enhancedClient.table("Wearables", TableSchema.fromBean(WearableModel.class));
        this.dynamoDbClient = dynamoDbClient;
    }

    @PostConstruct
    public void init() {
        logger.info("WearableRepository init() running, creating Wearable table");
        try {
            table.createTable(CreateTableEnhancedRequest.builder()
                    .globalSecondaryIndices(
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("creator-id-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("company-id-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("item-price-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("item-color-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("item-name-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("wearable-id-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build()
                    )
                    .build());
            logger.info("Wearables table created successfully with all indexes");
        } catch (ResourceInUseException e) {
            // table already exists
            logger.debug("TABLE Wearables ALREADY EXISTS" + e);
            DescribeTableResponse desc = dynamoDbClient.describeTable(
                    r -> r.tableName("Wearables"));
            List<KeySchemaElement> keys = desc.table().keySchema();
            logger.info("Existing key schema: {}", keys);
            if (keys.size() != 1 || !"id".equals(keys.get(0).attributeName())) {
                throw new IllegalStateException(
                        "Products table has wrong key schema: " + keys +
                                ". Expected single PK 'id'. Delete and recreate the table.");
            }
        }


    }

    public void save(WearableModel newItem) {
        try {
            table.putItem(r -> r.item(newItem)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_not_exists(#pid)")
                            .putExpressionName("#pid", "id")
                            .build()));
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("This product already exists "+newItem.getId(), e);
        }
    }

    public void update(WearableModel newItem) {
        try {
            table.putItem(PutItemEnhancedRequest.builder(WearableModel.class)
                    .item(newItem)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_exists(id)").build())
                    .build());
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("This product does not exists: " + newItem.getId(), e);
        }
    }

    public List<WearableModel> findByCreatorId(String id) {
        return table.index("creator-id-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(id)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }

    public List<WearableModel> findByItemName(String name) {
        return table.index("item-name-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(name)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }


    public Optional<WearableModel> findById(String id) {
        return Optional.ofNullable(
                table.getItem(Key.builder().partitionValue(id).build()));
    }

    public List<WearableModel> findByCompanyId(String companyId) {
        return table.index("company-id-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(companyId)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }

    public List<WearableModel> findByItemColor(String companyId) {
        return table.index("item-color-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(companyId)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }

    public List<WearableModel> findByPrice(Double itemPrice) {
        return table.index("item-price-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(itemPrice)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }

    public List<WearableModel> findByName(String itemPrice) {
        return table.index("item-name-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(itemPrice)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }


    public void delete(WearableModel item){
        table.deleteItem(item);
    }

    public List<WearableModel> findAll() {
        return table.scan()
                .items()
                .stream()
                .toList();
    }



}
