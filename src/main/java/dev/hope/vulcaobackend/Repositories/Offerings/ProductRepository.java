package dev.hope.vulcaobackend.Repositories.Offerings;

import dev.hope.vulcaobackend.Models.Offerings.ProductModel;
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
public class ProductRepository {

    private final DynamoDbClient dynamoDbClient;
    Logger logger = LoggerFactory.getLogger(ProductRepository.class);

    private final DynamoDbTable<ProductModel> productTable;

    public ProductRepository(DynamoDbEnhancedClient enhancedClient, DynamoDbClient dynamoDbClient) {
        this.productTable = enhancedClient.table("Products", TableSchema.fromBean(ProductModel.class));
        this.dynamoDbClient = dynamoDbClient;
    }

    @PostConstruct
    public void init() {
        logger.info("ProductRepository init() running, creating Products table");
        try {
            productTable.createTable(CreateTableEnhancedRequest.builder()
                    .globalSecondaryIndices(
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("creator-id-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("creator-name-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("company-id-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("company-name-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("item-price-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("item-name-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build()
                            )
                    .build());
            logger.info("Product table created successfully with all indexes");
        } catch (ResourceInUseException e) {
            // table already exists
            logger.debug("TABLE Products ALREADY EXISTS" + e);
            DescribeTableResponse desc = dynamoDbClient.describeTable(
                    r -> r.tableName("Products"));
            List<KeySchemaElement> keys = desc.table().keySchema();
            logger.info("Existing key schema: {}", keys);
            if (keys.size() != 1 || !"id".equals(keys.get(0).attributeName())) {
                throw new IllegalStateException(
                        "Products table has wrong key schema: " + keys +
                                ". Expected single PK 'id'. Delete and recreate the table.");
            }
        }
    }

    public void save(ProductModel newProduct) {
        try {
            productTable.putItem(r -> r.item(newProduct)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_not_exists(#pid)")
                            .putExpressionName("#pid", "id")
                            .build()));
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("This product already exists "+newProduct.getId(), e);
        }
    }

    public void update(ProductModel updatedProduct) {
        try {
            productTable.putItem(PutItemEnhancedRequest.builder(ProductModel.class)
                    .item(updatedProduct)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_exists(id)").build())
                    .build());
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("This product does not exists: "+updatedProduct.getId(), e);
        }
    }

    public List<ProductModel> findByCreatorUsername(String creatorName) {
        return productTable.index("creator-name-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(creatorName)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }
    public List<ProductModel> findByCreatorId(String id) {
        return productTable.index("creator-id-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(id)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }

    public List<ProductModel> findByName(String productName) {
        return productTable.index("item-name-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(productName)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }

    public Optional<ProductModel> findById(String id) {
        return Optional.ofNullable(
                productTable.getItem(Key.builder().partitionValue(id).build()));
    }

    public List<ProductModel> findByCompanyName(String companyName) {
        return productTable.index("company-name-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(companyName)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }

    public List<ProductModel> findByCompanyId(String companyId) {
        return productTable.index("company-id-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(companyId)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }

    public List<ProductModel> findByPrice(Double itemPrice) {
        return productTable.index("item-price-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(itemPrice)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }



    public void deleteProduct(ProductModel product){
        productTable.deleteItem(product);
    }

    public List<ProductModel> findAll() {
        return productTable.scan()
                .items()
                .stream()
                .toList();
    }

}
