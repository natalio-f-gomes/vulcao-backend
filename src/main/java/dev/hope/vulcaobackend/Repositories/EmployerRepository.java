package dev.hope.vulcaobackend.Repositories;


import dev.hope.vulcaobackend.Models.AccountEntities.EmployerModel;
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
public class EmployerRepository {

    Logger logger = LoggerFactory.getLogger(EmployerRepository.class);


    private final DynamoDbTable<EmployerModel> employerTable;

    public EmployerRepository(DynamoDbEnhancedClient enhancedClient) {
        this.employerTable = enhancedClient.table("Employers", TableSchema.fromBean(EmployerModel.class));
    }

    @PostConstruct
    public void init() {
        try {
            employerTable.createTable(CreateTableEnhancedRequest.builder()
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
            logger.debug("TABLE Employers ALREADY EXISTS");
        }
    }

    public void save(EmployerModel newEmployer) {
        try {
            employerTable.putItem(r -> r.item(newEmployer)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_not_exists(#pid)")
                            .putExpressionName("#pid", "id")
                            .build()));
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("This Employer already exists "+newEmployer.getId(), e);
        }
    }

    public void update(EmployerModel updatedEmployer) {
        try {
            employerTable.putItem(PutItemEnhancedRequest.builder(EmployerModel.class)
                    .item(updatedEmployer)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_exists(id)").build())
                    .build());
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("This Employer does not exists: "+ updatedEmployer.getId(), e);
        }
    }

    public Optional<EmployerModel> findByEmail(String email) {
        return employerTable.index("email-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(email)))
                .stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
    }

    public Optional<EmployerModel> findById(String id) {
        return employerTable.index("id-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(id)))
                .stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
    }

    public Optional<EmployerModel> findByUsername(String username) {
        var result = employerTable.index("username-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(username)))
                .stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
        System.out.println("findByUsername({"+username+"}) -> {"+result+"}" );
        return result;
    }

    public void deleteUserById(String id){
        EmployerModel user =  findById(id).orElseThrow(()-> new RuntimeException("There aren't any users with this id "));
        employerTable.deleteItem(user);
    }

    public List<EmployerModel> findAll() {
        return employerTable.scan()
                .items()
                .stream()
                .toList();
    }

}
