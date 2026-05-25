package dev.hope.vulcaobackend.Repositories;


import dev.hope.vulcaobackend.Models.AccountEntities.StaffModel;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.CreateTableEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.EnhancedGlobalSecondaryIndex;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.List;
import java.util.Optional;

@Repository
public class StaffRepository {

    private final DynamoDbClient dynamoDbClient;
    Logger logger = LoggerFactory.getLogger(StaffRepository.class);


    private final DynamoDbTable<StaffModel> staffTable;

    public StaffRepository(DynamoDbEnhancedClient enhancedClient, DynamoDbClient dynamoDbClient) {
        this.staffTable = enhancedClient.table("Staffs", TableSchema.fromBean(StaffModel.class));
        this.dynamoDbClient = dynamoDbClient;
    }

    @PostConstruct
    public void init() {
        try {
            staffTable.createTable(CreateTableEnhancedRequest.builder()
                    .globalSecondaryIndices(
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("creator-id-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("creator-username-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("creator-fullName-index")
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
                                    .indexName("staff-department-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
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
                                    .build()
                            )
                    .build());
        } catch (ResourceInUseException e) {
            // table already exists
            logger.debug("TABLE Products ALREADY EXISTS" + e);
            DescribeTableResponse desc = dynamoDbClient.describeTable(
                    r -> r.tableName("Staffs"));
            List<KeySchemaElement> keys = desc.table().keySchema();
            logger.info("Existing key schema: {}", keys);
            if (keys.size() != 1 || !"id".equals(keys.get(0).attributeName())) {
                throw new IllegalStateException(
                        "Products table has wrong key schema: " + keys +
                                ". Expected single PK 'id'. Delete and recreate the table.");
            }
        }
    }

    public void save(StaffModel newStaff) {
        try {
            staffTable.putItem(r -> r.item(newStaff)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_not_exists(#pid)")
                            .putExpressionName("#pid", "id")
                            .build()));
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("This Staff already exists "+newStaff.getId(), e);
        }
    }

    public void update(StaffModel updatedStaff) {
        try {
            staffTable.putItem(PutItemEnhancedRequest.builder(StaffModel.class)
                    .item(updatedStaff)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_exists(id)").build())
                    .build());
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("This staff does not exists: "+ updatedStaff.getId(), e);
        }
    }

    public Optional<StaffModel> findByEmail(String email) {
        return staffTable.index("email-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(email)))
                .stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
    }

    public Optional<StaffModel> findById(String id) {
        return staffTable.index("id-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(id)))
                .stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
    }

    public Optional<StaffModel> findByUsername(String username) {
        var result = staffTable.index("username-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(username)))
                .stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
        System.out.println("findByUsername({"+username+"}) -> {"+result+"}" );
        return result;
    }

    public void deleteUserById(String id){
        StaffModel user =  findById(id).orElseThrow(()-> new RuntimeException("There aren't any users with this id "));
        staffTable.deleteItem(user);
    }

    public List<StaffModel> findStaffByCreatorUsername(String username) {
        return staffTable.index("creator-username-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(username)))
                .stream().flatMap(page -> page.items().stream())
                .toList();
    }

    public List<StaffModel> findStaffByCreatorId(String id) {
        return staffTable.index("creator-id-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(id)))
                .stream().flatMap(page -> page.items().stream())
                .toList();
    }

    public List<StaffModel> findCompanyStaffByCompanyId(String companyId) {
        return staffTable.index("company-id-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(companyId)))
                .stream().flatMap(page -> page.items().stream())
                .toList();
    }

    public List<StaffModel> findCompanyStaffByCompanyName(String companyName) {
        return staffTable.index("company-name-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(companyName)))
                .stream().flatMap(page -> page.items().stream())
                .toList();
    }

    public List<StaffModel> findCompanyStaffByDepartment(String department) {
        return staffTable.index("staff-department-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(department)))
                .stream().flatMap(page -> page.items().stream())
                .toList();
    }



    public List<StaffModel> findAll() {
        return staffTable.scan()
                .items()
                .stream()
                .toList();
    }

    public void deleteStaff(StaffModel staff){
        staffTable.deleteItem(staff);
    }



}
