package dev.hope.vulcaobackend.Repositories;


import dev.hope.vulcaobackend.Models.Offerings.OfferingModel;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;
import software.amazon.awssdk.services.dynamodb.model.ProjectionType;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;

import java.util.List;
import java.util.Optional;

@Repository
public class OfferingRepository {

    Logger logger = LoggerFactory.getLogger(OfferingRepository.class);


    private final DynamoDbTable<OfferingModel> table;

    public OfferingRepository(DynamoDbEnhancedClient enhancedClient) {
        this.table = enhancedClient.table("Offerings", TableSchema.fromBean(OfferingModel.class));
    }

    @PostConstruct
    public void init() {
        try {
            table.createTable(CreateTableEnhancedRequest.builder()
                    .globalSecondaryIndices(
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("name")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("offering-id")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("creator-id")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("updated-by")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("price")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("stock")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("type")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("business-id")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("LatestIndex")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build()

                    )
                    .build());
        } catch (ResourceInUseException e) {
            // table already exists
            logger.debug("TABLE USERS ALREADY EXISTS");
        }
    }

    public void save(OfferingModel newOffering) {
        newOffering.onCreate();
        logger.debug("NEW OFFERING SAVING: " + newOffering.getName());
        try {
            table.putItem(r -> r.item(newOffering)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_not_exists(#pid)")
                            .putExpressionName("#pid", "id")
                            .build()));
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("This Offering already exists " + newOffering.getId(), e);
        }
    }

    public void update(OfferingModel offering) {
        offering.onUpdate();
        try {
            table.putItem(PutItemEnhancedRequest.builder(OfferingModel.class)
                    .item(offering)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_exists(id)").build())
                    .build());
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("This Offering does not exists: "+ offering.getId(), e);
        }
    }

    public List<OfferingModel> findByType(String type) {
        return table.index("type")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(type)))
                .stream().flatMap(page -> page.items().stream())
                .toList();
    }
    public List<OfferingModel> findByName(String name) {
        return table.index("name")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(name)))
                .stream().flatMap(page -> page.items().stream())
                .toList();
    }
    public List<OfferingModel> findByBusinessId(String companyId) {
        return table.index("business-id")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(companyId)))
                .stream().flatMap(page -> page.items().stream())
                .toList();
    }
    public List<OfferingModel> findByCreatorId(String creatorId) {
        return table.index("creator-id")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(creatorId)))
                .stream().flatMap(page -> page.items().stream())
                .toList();
    }
    public List<OfferingModel> findByUpdatedBy(String updatedBById) {
        return table.index("updated-by")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(updatedBById)))
                .stream().flatMap(page -> page.items().stream())
                .toList();
    }
    public List<OfferingModel> findByPrice(Double price) {
        return table.index("price")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(price)))
                .stream().flatMap(page -> page.items().stream())
                .toList();
    }
    public List<OfferingModel> findByStock(Integer stock) {
        return table.index("stock")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(stock)))
                .stream().flatMap(page -> page.items().stream())
                .toList();
    }



    public void delete( OfferingModel offering  ){
        table.deleteItem(offering);
    }

    public List<OfferingModel> findAll() {
        return table.scan()
                .items()
                .stream()
                .toList();
    }


    public Optional<OfferingModel> findById(String code){
        return table.index("offering-id").query(QueryConditional.keyEqualTo(k -> k.partitionValue(code))).stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
    }

    public List<OfferingModel> find100LatestProducts() {
        int LIMIT = 100;
        DynamoDbIndex<OfferingModel> index = table.index("LatestIndex");

        QueryConditional queryConditional = QueryConditional
                .keyEqualTo(Key.builder().partitionValue("PRODUCT").build());

        QueryEnhancedRequest queryRequest = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .scanIndexForward(false)
                .limit(LIMIT)
                .build();

        Page<OfferingModel> firstPage = index.query(queryRequest)
                .iterator()
                .next();

        return firstPage.items();
    }

}
