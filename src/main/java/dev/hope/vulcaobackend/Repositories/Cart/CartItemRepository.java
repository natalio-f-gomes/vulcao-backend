package dev.hope.vulcaobackend.Repositories.Cart;


import dev.hope.vulcaobackend.Models.Shopping.CartItemModel;
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
import java.util.stream.Collectors;
@Repository
public class CartItemRepository {
    private static final Logger logger = LoggerFactory.getLogger(CartItemRepository.class);
    private final DynamoDbTable<CartItemModel> cartItemTable;

    public CartItemRepository(DynamoDbEnhancedClient client){
        this.cartItemTable = client.table("CartItems", TableSchema.fromBean(CartItemModel.class));
    }

    @PostConstruct
    public void init() {
        try {
            cartItemTable.createTable(CreateTableEnhancedRequest.builder()
                    .globalSecondaryIndices(
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("id-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("user-id-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("company-id-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build()
                    )
                    .build());
        } catch (ResourceInUseException e) {
            logger.debug("TABLE CartItems ALREADY EXISTS");
        }
    }

    public void save(CartItemModel newItem) {
        try {
            cartItemTable.putItem(r -> r.item(newItem)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_not_exists(#pid)")
                            .putExpressionName("#pid", "id")
                            .build()));
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("This Item already exists "+newItem.getId(), e);
        }
    }

    public void update(CartItemModel updatedItem) {
        try {
            cartItemTable.putItem(PutItemEnhancedRequest.builder(CartItemModel.class)
                    .item(updatedItem)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_exists(id)").build())
                    .build());
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("This Cart does not exists: "+updatedItem.getId(), e);
        }
    }


    public List<CartItemModel> findAll(){
        return cartItemTable.scan().items().stream().toList();
    }

    public List<CartItemModel> findByCartId(String cartId) {
        return cartItemTable.index("cart-id-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(cartId)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }

    public List<CartItemModel> findByCompanyId(String companyId) {
        return cartItemTable.index("company-id-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(companyId)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }


    public Optional<CartItemModel> findByItemId(String itemId) {

        return cartItemTable.index("id-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(itemId)))
                .stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
    }

    public void delete(CartItemModel item) {
        cartItemTable.deleteItem(item);
    }


    public List<CartItemModel> findCartItems(String userId, String companyId) {
        logger.debug("Entering findCartItem with userId: {}, companyId: {}", userId, companyId);
        QueryConditional q = QueryConditional.sortBeginsWith(
                k -> k.partitionValue(userId).sortValue(companyId + "#"));
        return cartItemTable.query(r -> r.queryConditional(q))
                .items().stream().collect(Collectors.toList());
    }

    public Optional<CartItemModel> findCartItem(String userId, String companyId, String productId) {
        CartItemModel item = cartItemTable.getItem(Key.builder()
                .partitionValue(userId)
                .sortValue(companyId + "#" + productId)
                .build());
        return Optional.ofNullable(item);
    }


}