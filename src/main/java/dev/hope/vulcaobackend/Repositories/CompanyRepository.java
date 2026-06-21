package dev.hope.vulcaobackend.Repositories;


import dev.hope.vulcaobackend.Models.CompanyModel;
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

@Repository
public class CompanyRepository {

    Logger logger = LoggerFactory.getLogger(CompanyRepository.class);


    private final DynamoDbTable<CompanyModel> companyTable;

    public CompanyRepository(DynamoDbEnhancedClient enhancedClient) {
        this.companyTable = enhancedClient.table("Companies", TableSchema.fromBean(CompanyModel.class));
    }

    @PostConstruct
    public void init() {
        try {
            companyTable.createTable(CreateTableEnhancedRequest.builder()
                    .globalSecondaryIndices(
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("company-category-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("company-name-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("company-creator-id")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("company-email-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("company-id-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build())
                    .build());
        } catch (ResourceInUseException e) {
            // table already exists
            logger.debug("TABLE USERS ALREADY EXISTS");
        }
    }

    public void save(CompanyModel newCompany) {
        try {
            companyTable.putItem(r -> r.item(newCompany)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_not_exists(#pid)")
                            .putExpressionName("#pid", "id")
                            .build()));
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("This Company already exists " + newCompany.getId(), e);
        }
    }

    public void update(CompanyModel updatedCompany) {
        try {
            companyTable.putItem(PutItemEnhancedRequest.builder(CompanyModel.class)
                    .item(updatedCompany)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_exists(id)").build())
                    .build());
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("This Company does not exists: "+ updatedCompany.getId(), e);
        }
    }

    public Optional<CompanyModel> findByEmail(String email) {
        return companyTable.index("company-email-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(email)))
                .stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
    }

    public Optional<CompanyModel> findByName(String name) {
        return companyTable.index("company-name-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(name)))
                .stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
    }

    public Optional<CompanyModel> findById(String id) {
        return companyTable.index("company-id-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(id)))
                .stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
    }



    public List<CompanyModel> findByCreatorId(String creatorId) {
        var result = companyTable.index( "company-creator-id")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(creatorId)))
                .stream()
                .flatMap(page -> page.items().stream()).toList();
        System.out.println("findByCreatorId({"+creatorId+"}) -> {"+result+"}" );
        return result;
    }



    public List<CompanyModel> findByCategory(String category) {
        var result = companyTable.index("company-cetegory-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(category)))
                .stream()
                .flatMap(page -> page.items().stream()).toList();
        System.out.println("findByCategory({"+category+"}) -> {"+result+"}" );
        return result;
    }

    public void deleteCompanyById(CompanyModel company){
        companyTable.deleteItem(company);
    }

    public List<CompanyModel> findAll() {
        return companyTable.scan()
                .items()
                .stream()
                .toList();
    }



}
