package dev.hope.vulcaobackend.Repositories;

import dev.hope.vulcaobackend.Models.AccountEntities.UserModel;
import dev.hope.vulcaobackend.Models.Shopping.OrderItemModel;
import dev.hope.vulcaobackend.Models.Shopping.OrderModel;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class OrderItemRepository {

    private static final Logger logger = LoggerFactory.getLogger(OrderItemRepository.class);
    private final DynamoDbTable<OrderItemModel> table;

    public OrderItemRepository(DynamoDbEnhancedClient client) {
        this.table = client.table("OrderItems", TableSchema.fromBean(OrderItemModel.class));
    }

    @PostConstruct
    public void init() {
        try {
            table.createTable(CreateTableEnhancedRequest.builder()
                    .globalSecondaryIndices(
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("id-index")
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
    public void save(OrderItemModel newOrderItem) {
        logger.debug("NEW ORDER REGISTERING: " + newOrderItem.getItemName());
        table.putItem(newOrderItem);
        //its okay of the item exists twice, they are not the same at all
    }

    public List<OrderItemModel> findAll() {
        return table.scan().items().stream().toList();
    }

    public void update(OrderItemModel updatedOrderItem) {
        try {
            table.putItem(PutItemEnhancedRequest.builder(OrderItemModel.class)
                    .item(updatedOrderItem)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_exists(#pk)")
                            .putExpressionName("#pk", "companyId")
                            .build())
                    .build());
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("This Order Item does not exist: " + updatedOrderItem.getId(), e);
        }
    }


    public Optional<OrderItemModel> findById(String orderId) {
        return table.index("id-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(orderId)))
                .stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
    }





    public void delete(OrderItemModel item) {
        table.deleteItem(item);
    }
}