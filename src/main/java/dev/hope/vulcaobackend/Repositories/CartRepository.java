package dev.hope.vulcaobackend.Repositories;


import dev.hope.vulcaobackend.Models.Shopping.CartModel;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.CreateTableEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;

import java.util.Optional;

@Repository
public class CartRepository {

    Logger logger = LoggerFactory.getLogger(CartRepository.class);

    private final DynamoDbTable<CartModel> table;

    public CartRepository(DynamoDbEnhancedClient client){
        this.table = client.table("ShoppingCart", TableSchema.fromBean(CartModel.class));

    }
    @PostConstruct
    public void init(){
        try{
            table.createTable(CreateTableEnhancedRequest.builder()
                 .build());
        }catch (ResourceInUseException e) {
            // table already exists
            logger.debug("ShoppingCart  ALREADY EXISTS");
        }

    }

    public void save(CartModel cart) {
        cart.onCreate();
        logger.debug("NEW CART REGISTERING: " + cart.getId());
        try {
            table.putItem(r -> r.item(cart)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_not_exists(#pid)")
                            .putExpressionName("#pid", "id")
                            .build()));
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("This User CART already exists " + cart.getId(), e);
        }
    }

    public void update(CartModel cart) {
        cart.onUpdate();
        try {
            table.putItem(PutItemEnhancedRequest.builder(CartModel.class)
                    .item(cart)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_exists(id)").build())
                    .build());
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("This USER CART does not exists: "+ cart.getId(), e);
        }
    }

    public Optional<CartModel> findById(String userId) {
        return table
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(userId)))
                .stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
    }





}
