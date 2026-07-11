package dev.hope.vulcaobackend.Repositories;


import dev.hope.vulcaobackend.Models.FeedbackModel;
import dev.hope.vulcaobackend.Models.Offerings.OfferingModel;
import dev.hope.vulcaobackend.Models.Shopping.CartModel;
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
public class FeedbackRepository {

    Logger logger = LoggerFactory.getLogger(FeedbackRepository.class);


    private final DynamoDbTable<FeedbackModel> table;

    public FeedbackRepository(DynamoDbEnhancedClient enhancedClient) {
        this.table = enhancedClient.table("Feedback", TableSchema.fromBean(FeedbackModel.class));
    }

    @PostConstruct
    public void init() {
        try {
            table.createTable(CreateTableEnhancedRequest.builder()
                    .globalSecondaryIndices(
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("LatestIndex")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("updated-at")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("title")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("email")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("status")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("votes")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("name")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build()

                    )
                    .build());
        } catch (ResourceInUseException e) {
            // table already exists
            logger.debug("TABLE USERS ALREADY EXISTS");
        }
    }

    public void save(FeedbackModel newFeedback) {
        newFeedback.onCreate();
        logger.debug("NEW OFFERING SAVING: " + newFeedback.getName());
        try {
            table.putItem(r -> r.item(newFeedback)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_not_exists(#pid)")
                            .putExpressionName("#pid", "id")
                            .build()));
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("This Feedback already exists " + newFeedback.getId(), e);
        }
    }

    public void update(FeedbackModel feedback) {
        feedback.onUpdate();
        try {
            table.putItem(PutItemEnhancedRequest.builder(FeedbackModel.class)
                    .item(feedback)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_exists(id)").build())
                    .build());
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("This Feedback does not exists: "+ feedback.getId(), e);
        }
    }


    public List<FeedbackModel> findByName(String name) {
        return table.index("name")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(name)))
                .stream().flatMap(page -> page.items().stream())
                .toList();
    }
    public List<FeedbackModel> findByEmail(String email) {
        return table.index("email")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(email)))
                .stream().flatMap(page -> page.items().stream())
                .toList();
    }
    public List<FeedbackModel> findByTitle(String title) {
        return table.index("title")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(title)))
                .stream().flatMap(page -> page.items().stream())
                .toList();
    }
    public List<FeedbackModel> findByStatus(String status) {
        return table.index("status")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(status)))
                .stream().flatMap(page -> page.items().stream())
                .toList();
    }
    public List<FeedbackModel> findByVotes(Integer votes) {
        return table.index("votes")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(votes)))
                .stream().flatMap(page -> page.items().stream())
                .toList();
    }



    public Optional<FeedbackModel> findById(String id) {
        return table
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(id)))
                .stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
    }




    public void delete( FeedbackModel feedback  ){
        table.deleteItem(feedback);
    }

    public List<FeedbackModel> findAll() {
        return table.scan()
                .items()
                .stream()
                .toList();
    }


    public List<FeedbackModel> find100Latest() {
        DynamoDbIndex<FeedbackModel> index = table.index("LatestIndex");

        QueryConditional queryConditional = QueryConditional
                .keyEqualTo(k -> k.partitionValue("ALL"));

        QueryEnhancedRequest queryRequest = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .scanIndexForward(false) // newest first
                .limit(100)
                .build();

        Page<FeedbackModel> firstPage = index.query(queryRequest)
                .iterator()
                .next();

        return firstPage.items();
    }
}
