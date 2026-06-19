package dev.hope.vulcaobackend.Repositories;


import dev.hope.vulcaobackend.Models.AccountEntities.AuthModel;
import dev.hope.vulcaobackend.Models.CompanyModel;
import dev.hope.vulcaobackend.Models.MFAModel;
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

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
public class MfaRepository {

    Logger logger = LoggerFactory.getLogger(MfaRepository.class);

    private final DynamoDbTable<MFAModel> mfaTable;

    public MfaRepository(DynamoDbEnhancedClient client){
        this.mfaTable = client.table("MultiFactorAuthentication", TableSchema.fromBean(MFAModel.class));

    }
    @PostConstruct
    public void init(){
        try{
            mfaTable.createTable(CreateTableEnhancedRequest.builder()
                    .globalSecondaryIndices(
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("user-email-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("auth-code-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build()

                    ).build());
        }catch (ResourceInUseException e) {
            // table already exists
            logger.debug("TABLE MFA  ALREADY EXISTS");
        }

    }

    public void save(MFAModel authData) { mfaTable.putItem(authData); }



    public Optional<MFAModel> findByMFACode(String code){
        return mfaTable.index("auth-code-index").query(QueryConditional.keyEqualTo(k -> k.partitionValue(code))).stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
    }

    public Optional<MFAModel> findByUserEmail(String email){
        return mfaTable.index("user-email-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(email)))
                .stream()
                .flatMap(page -> page.items().stream())
                .max(Comparator.comparing(MFAModel::getSendTime));
    }

    public void deleteMFAData(MFAModel data){
        //to be called when the user sign out
       mfaTable.deleteItem(data);
    }

    public void update(MFAModel updatedMfa) {
        try {
            mfaTable.putItem(PutItemEnhancedRequest.builder(MFAModel.class)
                    .item(updatedMfa)
                    .conditionExpression(Expression.builder()
                            .expression("attribute_exists(userEmail)").build())
                    .build());
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("This MFA Data does not exists: "+ updatedMfa.getUserEmail(), e);
        }
    }



}
