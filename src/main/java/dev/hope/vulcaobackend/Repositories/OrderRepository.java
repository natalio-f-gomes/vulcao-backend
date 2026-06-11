package dev.hope.vulcaobackend.Repositories;

import dev.hope.vulcaobackend.Models.AccountEntities.StaffModel;
import dev.hope.vulcaobackend.Models.Shopping.CartItemModel;
import dev.hope.vulcaobackend.Models.Shopping.OrderModel;
import dev.hope.vulcaobackend.Repositories.Cart.CartItemRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
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

    private static final Logger logger = LoggerFactory.getLogger(OrderRepository.class);
    private final DynamoDbTable<OrderModel> orderTable;

    public OrderRepository(DynamoDbEnhancedClient client) {
        this.orderTable = client.table("Orders", TableSchema.fromBean(OrderModel.class));
    }

    @PostConstruct
    public void init() {
        try {
            orderTable.createTable(CreateTableEnhancedRequest.builder()
                    .globalSecondaryIndices(
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("id-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("track-id-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build()
                    )
                    .build());
        } catch (ResourceInUseException e) {
            logger.debug("TABLE Orders ALREADY EXISTS");
        }
    }

    /**
     * Saves a NEW order. The condition now guards the table's actual key
     * (partition key), not the "id" attribute — attribute_not_exists on a
     * non-key attribute silently passes when the stored item never had
     * that attribute set, which is exactly why the old overwrite slipped
     * through without a ConditionalCheckFailedException.
     */
    public void save(OrderModel newOrder) {
        try {
            orderTable.putItem(r -> r.item(newOrder)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_not_exists(#pk) AND attribute_not_exists(#sk)")
                            .putExpressionName("#pk", "companyId")
                            .putExpressionName("#sk", "orderKey")
                            .build()));
            System.out.println("PK=" + newOrder.getCompanyId());
            System.out.println("SK=" + newOrder.getOrderKey());
        } catch (ConditionalCheckFailedException e) {
            System.out.println("PK=" + newOrder.getCompanyId());
            System.out.println("SK=" + newOrder.getOrderKey());
            throw e;
        }
    }

    public List<OrderModel> findAll() {
        return orderTable.scan().items().stream().toList();
    }

    public void update(OrderModel updatedOrder) {
        try {
            orderTable.putItem(PutItemEnhancedRequest.builder(OrderModel.class)
                    .item(updatedOrder)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_exists(#pk)")
                            .putExpressionName("#pk", "companyId")
                            .build())
                    .build());
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("This Order does not exist: " + updatedOrder.getId(), e);
        }
    }

    public Optional<OrderModel> findByTrackId(String trackId) {
        return orderTable.index("track-id-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(trackId)))
                .stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
    }

    public Optional<OrderModel> findById(String orderId) {
        return orderTable.index("id-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(orderId)))
                .stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
    }

    /**
     * One specific order — now requires the orderId, because
     * (companyId, userId) alone no longer identifies a single item.
     */
    public Optional<OrderModel> findOrder(String companyId, String userId, String orderId) {
        OrderModel item = orderTable.getItem(Key.builder()
                .partitionValue(companyId)
                .sortValue("#" + userId + "#" + orderId)
                .build());
        return Optional.ofNullable(item);
    }

    /**
     * ALL of a user's orders at a company. begins_with on the sort key
     * is what makes multiple orders per (company, user) possible —
     * the old keyEqualTo on the full key could only ever return one item.
     */
    public List<OrderModel> findAllUserOrders(String userId, String companyId) {
        QueryConditional conditional =
                QueryConditional.sortBeginsWith(
                        Key.builder()
                                .partitionValue(companyId)
                                .sortValue(companyId + "#" + userId + "$")
                                .build()
                );

        return orderTable.query(r -> r.queryConditional(conditional))
                .items()
                .stream()
                .toList();
    }

    public void delete(OrderModel order) {
        orderTable.deleteItem(order);
    }
}