package dev.hope.vulcaobackend.Repositories.Cart;


import dev.hope.vulcaobackend.Models.Cart.CartModel;
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
public class CartRepository {
    Logger logger = LoggerFactory.getLogger(CartModel.class);
    private final DynamoDbTable<CartModel> cartTable;

    public CartRepository(DynamoDbEnhancedClient client){
        this.cartTable = client.table("Carts", TableSchema.fromBean(CartModel.class));
    }
    @PostConstruct
    public void init() {
        try {
            cartTable.createTable(CreateTableEnhancedRequest.builder()
                    .globalSecondaryIndices(
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("id-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build())
                    .build());
        } catch (ResourceInUseException e) {
            // table already exists
            logger.debug("TABLE Cart ALREADY EXISTS");
        }
    }

    public void save(CartModel newCart) {
        try {
            cartTable.putItem(r -> r.item(newCart)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_not_exists(#pid)")
                            .putExpressionName("#pid", "id")
                            .build()));
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("This Cart already exists "+newCart.getId(), e);
        }
    }

    public void update(CartModel updatedCart) {
        try {
            cartTable.putItem(PutItemEnhancedRequest.builder(CartModel.class)
                    .item(updatedCart)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_exists(id)").build())
                    .build());
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("This Cart does not exists: "+updatedCart.getId(), e);
        }
    }

    public List<CartModel> findAll(){
        return cartTable.scan().items().stream().toList();
    }

    public Optional<CartModel> findById(String cartId) {
        return Optional.ofNullable(
                cartTable.getItem(Key.builder().partitionValue(cartId).build()));
    }




}
