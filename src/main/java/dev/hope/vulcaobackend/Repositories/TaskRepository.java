package dev.hope.vulcaobackend.Repositories;

import dev.hope.vulcaobackend.Models.AccountEntities.StaffModel;
import dev.hope.vulcaobackend.Models.AccountEntities.UserModel;
import dev.hope.vulcaobackend.Models.TaskModel;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.config.Task;
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
public class TaskRepository {

    Logger logger = LoggerFactory.getLogger(TaskRepository.class);


    private final DynamoDbTable<TaskModel> table;

    public TaskRepository(DynamoDbEnhancedClient enhancedClient) {
        this.table = enhancedClient.table("Tasks", TableSchema.fromBean(TaskModel.class));
    }

    @PostConstruct
    public void init() {
        try {
            table.createTable(CreateTableEnhancedRequest.builder()
                    .globalSecondaryIndices(
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("creator-id-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("title-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("id-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("company-id-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build()
                            )
                    .build());
        } catch (ResourceInUseException e) {
            // table already exists
            logger.debug("TABLE TASKS ALREADY EXISTS");
        }
    }

    public void save(TaskModel newTask) {
        logger.debug("NEW TASK ADDED: " + newTask);
        try {
            table.putItem(r -> r.item(newTask)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_not_exists(#pid)")
                            .putExpressionName("#pid", "id")
                            .build()));
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("This User already exists " + newTask.getId(), e);
        }
    }

    public void update(TaskModel updatedTask) {
        try {
            table.putItem(PutItemEnhancedRequest.builder(TaskModel.class)
                    .item(updatedTask)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_exists(id)").build())
                    .build());
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("This Task does not exists: "+ updatedTask.getId(), e);
        }
    }


    public Optional<TaskModel> findById(String id) {
        return table.index("id-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(id)))
                .stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
    }




    public List<TaskModel> findByCreatorId(String creatorId) {
        return table.index("creator-id-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(creatorId)))
                .stream().flatMap(page -> page.items().stream())
                .toList();
    }

    public List<TaskModel> findByCompanyId(String companyId) {
        return table.index("company-id-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(companyId)))
                .stream().flatMap(page -> page.items().stream())
                .toList();
    }


    public void deleteTask(TaskModel task ){
        table.deleteItem(task);
    }

    public List<TaskModel> findAll() {
        return table.scan()
                .items()
                .stream()
                .toList();
    }

}
