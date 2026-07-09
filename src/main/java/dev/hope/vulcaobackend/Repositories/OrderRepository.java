package dev.hope.vulcaobackend.Repositories;



import dev.hope.vulcaobackend.Models.Shopping.OrderModel;

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
public class OrderRepository {

    Logger logger = LoggerFactory.getLogger(OrderRepository.class);

    private final DynamoDbTable<OrderModel> table;

    public OrderRepository(DynamoDbEnhancedClient client){
        this.table = client.table("Orders", TableSchema.fromBean(OrderModel.class));

    }

    @PostConstruct
    public void init() {
        try {
            table.createTable(CreateTableEnhancedRequest.builder()
                    .globalSecondaryIndices(
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("order-id")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("track-id")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("business-id")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("total-cost")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("status")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build()
                    )
                    .build());
        } catch (ResourceInUseException e) {
            // table already exists
            logger.debug("TABLE ORDERS ALREADY EXISTS");
        }
    }
    public void save(OrderModel order) {
        order.onCreate();
        logger.debug("NEW ORDER REGISTERING: " + order.getId());
        try {
            table.putItem(r -> r.item(order)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_not_exists(#pid)")
                            .putExpressionName("#pid", "id")
                            .build()));
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("This Order already exists " + order.getId(), e);
        }
    }

    public void update(OrderModel order) {
        order.onUpdate();
        try {
            table.putItem(PutItemEnhancedRequest.builder(OrderModel.class)
                    .item(order)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_exists(id)").build())
                    .build());
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("This Order does not exists: "+ order.getId(), e);
        }
    }

    public Optional<OrderModel> findById(String orderId) {
        return table
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(orderId)))
                .stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
    }

    public List<OrderModel> findByOrderId(String clientId) {
        return table.index("order-id")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(clientId)))
                .stream().flatMap(page -> page.items().stream())
                .toList();
    }



    public Optional<OrderModel> findByTrackId(String trackId) {
        var result = table.index("track-id")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(trackId)))
                .stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
        System.out.println("findByTrackId({"+trackId+"}) -> {"+result+"}" );
        return result;
    }



    public List<OrderModel> findByBusinessId(String businessId) {
        return table.index("business-id")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(businessId)))
                .stream().flatMap(page -> page.items().stream())
                .toList();
    }

    public List<OrderModel> findByTotalCost(Double totalCost) {
        return table.index("total-cost")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(totalCost)))
                .stream().flatMap(page -> page.items().stream())
                .toList();
    }

    public List<OrderModel> findByStatus(String status) {
        return table.index("status")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(status)))
                .stream().flatMap(page -> page.items().stream())
                .toList();
    }

    public List<OrderModel> getLatestOrders(String clientId) {
        int LIMIT = 10; // I think this is a good number for the latest client orders

        // 1. Define the partition key condition
        QueryConditional queryConditional = QueryConditional
                .keyEqualTo(Key.builder().partitionValue(clientId).build());

        // 2. Build the query request with ScanIndexForward = false
        QueryEnhancedRequest queryRequest = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .scanIndexForward(false) // CRITICAL: false = Descending order (Newest -> Oldest)
                .limit(LIMIT)            // Optional: pagination limit (e.g., top 10)
                .build();

        Page<OrderModel> firstPage = table.query(queryRequest)
                .iterator()
                .next(); // first page only — at most `limit` items

        return firstPage.items();

//        // 3. In case If If I want to get all items sorted by date
//        return table.query(queryRequest)
//                .items()
//                .stream()
//                .collect(Collectors.toList());
    }

}
