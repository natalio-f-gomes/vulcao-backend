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
import software.amazon.awssdk.enhanced.dynamodb.model.CreateTableEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.EnhancedGlobalSecondaryIndex;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;
import software.amazon.awssdk.services.dynamodb.model.ProjectionType;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepository {
    private static final Logger logger = LoggerFactory.getLogger(CartItemRepository.class);
    private final DynamoDbTable<OrderModel> orderTable;

    public OrderRepository(DynamoDbEnhancedClient client){
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
            logger.debug("TABLE CartItems ALREADY EXISTS");
        }
    }

    public void save(OrderModel newOrder) {
        try {
            orderTable.putItem(r -> r.item(newOrder)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_not_exists(#pid)")
                            .putExpressionName("#pid", "id")
                            .build()));
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("This Item already exists "+newOrder.getId(), e);
        }
    }
    public List<OrderModel> findAll(){
        return orderTable.scan().items().stream().toList();
    }


    public void update(OrderModel updatedOrder) {
        try {
            orderTable.putItem(PutItemEnhancedRequest.builder(OrderModel.class)
                    .item(updatedOrder)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_exists(id)").build())
                    .build());
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("This Cart does not exists: "+updatedOrder.getId(), e);
        }
    }

    public Optional<OrderModel> findByTrackId(String id) {
        return orderTable.index("track-id-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(id)))
                .stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
    }

    public Optional<OrderModel> findOrder(String companyId, String userId) {
        OrderModel item = orderTable.getItem(Key.builder()
                .partitionValue(companyId)
                .sortValue(companyId + "#" + userId)
                .build());
        return Optional.ofNullable(item);
    }

    public void delete(OrderModel order) {
        orderTable.deleteItem(order);
    }




}
