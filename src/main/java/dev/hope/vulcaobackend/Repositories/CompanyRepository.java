package dev.hope.vulcaobackend.Repositories;


import dev.hope.vulcaobackend.Models.CompanyModel;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.CreateTableEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.EnhancedGlobalSecondaryIndex;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
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
                                    .indexName("company-creator-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("company-name-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("company-email-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("company-taxId-index")
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

    public void save(CompanyModel newCompany) { companyTable.putItem(newCompany); }

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

    public Optional<CompanyModel> findByTaxId(String id) {
        return companyTable.index("company-taxId-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(id)))
                .stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
    }


    public List<CompanyModel> findByCreatorUsername(String username) {
        var result = companyTable.index("company-creator-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(username)))
                .stream()
                .flatMap(page -> page.items().stream()).toList();
        System.out.println("findByUsername({"+username+"}) -> {"+result+"}" );
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
