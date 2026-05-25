package dev.hope.vulcaobackend.Repositories;

import dev.hope.vulcaobackend.Models.AccountEntities.UserModel;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
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
                                    .indexName("id-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("email-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("username-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build())
                    .build());
        } catch (ResourceInUseException e) {
            // table already exists
            logger.debug("TABLE USERS ALREADY EXISTS");
        }
    }

    public void save(UserModel newUser) {
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

    public Optional<UserModel> findById(String id) {
        return table.index("id-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(id)))
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

    public void deleteUserById(String id){
        UserModel user =  findById(id).orElseThrow(()-> new RuntimeException("There aren't any users with this id "));
        table.deleteItem(user);
    }

    public List<UserModel> findAll() {
        return table.scan()
                .items()
                .stream()
                .toList();
    }

}
