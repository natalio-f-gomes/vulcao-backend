package dev.hope.vulcaobackend.Repositories;


import dev.hope.vulcaobackend.Models.CompanyModel;
import dev.hope.vulcaobackend.Models.InquiryModel;
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
public class InquiryRepository {

    Logger logger = LoggerFactory.getLogger(InquiryRepository.class);


    private final DynamoDbTable<InquiryModel> table;

    public InquiryRepository(DynamoDbEnhancedClient enhancedClient) {
        this.table = enhancedClient.table("Inquiries", TableSchema.fromBean(InquiryModel.class));
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
                                    .build()
                            )
                    .build());
        } catch (ResourceInUseException e) {
            // table already exists
            logger.debug("TABLE USERS ALREADY EXISTS");
        }
    }

    public void save(InquiryModel newInquiry) {
        try {
            table.putItem(r -> r.item(newInquiry)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_not_exists(#pid)")
                            .putExpressionName("#pid", "id")
                            .build()));
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("This Inquiry already exists " + newInquiry.getId(), e);
        }
    }

    public void update(InquiryModel inquiry) {
        try {
            table.putItem(PutItemEnhancedRequest.builder(InquiryModel.class)
                    .item(inquiry)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_exists(id)").build())
                    .build());
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("This Company does not exists: "+ inquiry.getId(), e);
        }
    }



    public Optional<InquiryModel> findById(String id) {
        return table.index("-id-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(id)))
                .stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
    }


    public List<InquiryModel> findByEmail(String creatorId) {
        var result = table.index( "email-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(creatorId)))
                .stream()
                .flatMap(page -> page.items().stream()).toList();
        System.out.println("findByCreatorId({"+creatorId+"}) -> {"+result+"}" );
        return result;
    }



    public void deleteById(InquiryModel inquiry){
        table.deleteItem(inquiry);
    }

    public List<InquiryModel> findAll() {
        return table.scan()
                .items()
                .stream()
                .toList();
    }



}
