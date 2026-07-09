package dev.hope.vulcaobackend.Repositories;


import dev.hope.vulcaobackend.Models.Business.BusinessModel;
import dev.hope.vulcaobackend.Models.Offerings.OfferingModel;
import dev.hope.vulcaobackend.Models.UserModel;
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
import java.util.stream.Collectors;

@Repository
public class BusinessRepository {

    Logger logger = LoggerFactory.getLogger(BusinessRepository.class);


    private final DynamoDbTable<BusinessModel> table;

    public BusinessRepository(DynamoDbEnhancedClient enhancedClient) {
        this.table = enhancedClient.table("Businesses", TableSchema.fromBean(BusinessModel.class));
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
                                    .indexName("email")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("creator-id")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("status")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("type")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build()
                    )
                    .build());
        } catch (ResourceInUseException e) {
            // table already exists
            logger.debug("TABLE Business ALREADY EXISTS");
        }
    }

    public void save(BusinessModel newBusiness) {
        newBusiness.onCreate();
        logger.debug("NEW Business REGISTERING: " + newBusiness.getName());
        try {
            table.putItem(r -> r.item(newBusiness)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_not_exists(#pid)")
                            .putExpressionName("#pid", "id")
                            .build()));
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("This Business already exists " + newBusiness.getId(), e);
        }
    }

    public void update(BusinessModel business) {
        business.onUpdate();
        try {
            table.putItem(PutItemEnhancedRequest.builder(BusinessModel.class)
                    .item(business)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_exists(id)").build())
                    .build());
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("This Business does not exists: "+ business.getId(), e);
        }
    }

    public Optional<BusinessModel> findByEmail(String email) {
        return table.index("email")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(email)))
                .stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
    }

    public List<BusinessModel> findByCreatorId(String id) {
        return table.index("creator-id")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(id)))
                .stream().flatMap(page -> page.items().stream())
                .toList();
    }


    public Optional<BusinessModel> findByName(String name) {
        var result = table.index("name")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(name)))
                .stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
        System.out.println("findByName({"+name+"}) -> {"+result+"}" );
        return result;
    }

    public List<BusinessModel> findByStatus(String status) {
        return table.index("status")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(status)))
                .stream().flatMap(page -> page.items().stream())
                .toList();
    }

    public List<BusinessModel> findByType(String type) {
        return table.index("type")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(type)))
                .stream().flatMap(page -> page.items().stream())
                .toList();
    }


    public void delete( BusinessModel business  ){
        table.deleteItem(business);
    }

    public List<BusinessModel> findAll() {
        return table.scan()
                .items()
                .stream()
                .toList();
    }

    public Optional<BusinessModel> findById(String id) {
        return table
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(id)))
                .stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
    }

}
