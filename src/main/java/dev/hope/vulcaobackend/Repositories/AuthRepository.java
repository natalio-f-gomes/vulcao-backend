package dev.hope.vulcaobackend.Repositories;


import dev.hope.vulcaobackend.Models.AccountEntities.AuthModel;
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

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthRepository {

    Logger logger = LoggerFactory.getLogger(AuthRepository.class);

    private final DynamoDbTable<AuthModel> authTable;

    public AuthRepository(DynamoDbEnhancedClient client){
        this.authTable = client.table("AuthenticationData", TableSchema.fromBean(AuthModel.class));

    }
    @PostConstruct
    public void init(){
        try{
            authTable.createTable(CreateTableEnhancedRequest.builder()
                    .globalSecondaryIndices(
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("ipAddress-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("user-email-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("auth-mfa-code-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("duration-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("user-access-token-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build()

                    ).build());
        }catch (ResourceInUseException e) {
            // table already exists
            logger.debug("TABLE USERS ALREADY EXISTS");
        }

    }

    public void save(AuthModel authData) { authTable.putItem(authData); }

    public List<AuthModel> findByUserEmail(String email) {
        return authTable.index("user-email-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(email)))
                .stream().flatMap(page -> page.items().stream()).toList();
    }

    public List<AuthModel> findByUserIpAddress(String ipAddress) {
        return authTable.index("ipAddress-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(ipAddress)))
                .stream().flatMap(page -> page.items().stream()).toList();
    }

    public Optional<AuthModel> findByMFACode(String code){
        return authTable.index("auth-mfa-code-index").query(QueryConditional.keyEqualTo(k -> k.partitionValue(code))).stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
    }

    public List<AuthModel> findByUsername(String username) {
        return authTable.query(QueryConditional.keyEqualTo(
                        k -> k.partitionValue(username)))
                .items().stream().toList();
    }

    public void deleteUserLoginData(String username){
        //to be called when the user sign out
        List<AuthModel> result = findByUsername(username);
        for(AuthModel auth: result){
            authTable.deleteItem(auth);
        }
    }

    public List<AuthModel> findAll() {
        return authTable.scan()
                .items()
                .stream()
                .toList();
    }


}
