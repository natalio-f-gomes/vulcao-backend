package dev.hope.vulcaobackend.Repositories;


import dev.hope.vulcaobackend.Models.UserModel;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
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
public class UserRepository {

    Logger logger = LoggerFactory.getLogger(UserRepository.class);


    private final DynamoDbTable<UserModel> table;

    public UserRepository(DynamoDbEnhancedClient enhancedClient) {
        this.table = enhancedClient.table("Users", TableSchema.fromBean(UserModel.class));
    }

    @PostConstruct
    public void init() {
        try {
            table.createTable(CreateTableEnhancedRequest.builder()
                    .globalSecondaryIndices(
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("email-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("username-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("user-type-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("staff-creator-id")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("staff-company-id")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("staff-department")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("staff-job-title")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build()
                    )
                    .build());
        } catch (ResourceInUseException e) {
            // table already exists
            logger.debug("TABLE USERS ALREADY EXISTS");
        }
    }

    public void save(UserModel newUser) {
        newUser.onCreate();
        logger.debug("NEW USER REGISTERING: " + newUser.getUsername());
        try {
            table.putItem(r -> r.item(newUser)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_not_exists(#pid)")
                            .putExpressionName("#pid", "id")
                            .build()));
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("This User already exists " + newUser.getId(), e);
        }
    }

    public void update(UserModel updatedUser) {
        updatedUser.onUpdate();
        try {
            table.putItem(PutItemEnhancedRequest.builder(UserModel.class)
                    .item(updatedUser)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_exists(id)").build())
                    .build());
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("This User does not exists: "+ updatedUser.getId(), e);
        }
    }
    public Optional<UserModel> findByEmail(String email) {
        return table.index("email-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(email)))
                .stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
    }


    public Optional<UserModel> findByUsername(String username) {
        var result = table.index("username-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(username)))
                .stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
        System.out.println("findByUsername({"+username+"}) -> {"+result+"}" );
        return result;
    }

    public List<UserModel> findByType(String type) {
        return table.index("user-type-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(type)))
                .stream().flatMap(page -> page.items().stream())
                .toList();
    }

    public List<UserModel> findStaffByCreatorId(String id) {
        return table.index("staff-creator-id")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(id)))
                .stream().flatMap(page -> page.items().stream())
                .toList();
    }

    public List<UserModel> findStaffByCompanyId(String companyId) {
        return table.index("staff-company-id")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(companyId)))
                .stream().flatMap(page -> page.items().stream())
                .toList();
    }

    public List<UserModel> findStaffByDepartment(String department) {
        return table.index("staff-department")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(department)))
                .stream().flatMap(page -> page.items().stream())
                .toList();
    }


    public List<UserModel> findStaffByJobTitle(String jobTitle) {
        return table.index("staff-job-title")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(jobTitle)))
                .stream().flatMap(page -> page.items().stream())
                .toList();
    }

    public void delete( UserModel user  ){
        table.deleteItem(user);
    }

    public List<UserModel> findAll() {
        return table.scan()
                .items()
                .stream()
                .toList();
    }

    public Optional<UserModel> findById(String id) {
        return table
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(id)))
                .stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
    }

}
