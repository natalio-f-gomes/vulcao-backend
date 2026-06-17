package dev.hope.vulcaobackend;

import dev.hope.vulcaobackend.Models.AccountEntities.EmployerModel;
import dev.hope.vulcaobackend.Models.AccountEntities.StaffModel;
import dev.hope.vulcaobackend.Models.AccountEntities.UserModel;
import dev.hope.vulcaobackend.Models.CompanyModel;
import dev.hope.vulcaobackend.Models.Offerings.ProductModel;
import dev.hope.vulcaobackend.Models.Offerings.WearableModel;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Component
public class CleanTable {

    @Autowired
    protected DynamoDbEnhancedClient enhancedClient;


    @BeforeEach
    void wipeTables() {
        DynamoDbTable<UserModel> users = enhancedClient.table("Users",
                TableSchema.fromBean(UserModel.class));
        users.scan().items().forEach(users::deleteItem);

        DynamoDbTable<StaffModel> staffs = enhancedClient.table("Staffs",
                TableSchema.fromBean(StaffModel.class));
        staffs.scan().items().forEach(staffs::deleteItem);

        DynamoDbTable<CompanyModel> companies = enhancedClient.table("Companies",
                TableSchema.fromBean(CompanyModel.class));
        companies.scan().items().forEach(companies::deleteItem);

        DynamoDbTable<EmployerModel> employers = enhancedClient.table("Employers",
                TableSchema.fromBean(EmployerModel.class));
        employers.scan().items().forEach(employers::deleteItem);

        DynamoDbTable<ProductModel> products = enhancedClient.table("Products",
                TableSchema.fromBean(ProductModel.class));
        products.scan().items().forEach(products::deleteItem);

        DynamoDbTable<WearableModel> wearables = enhancedClient.table("Wearables",
                TableSchema.fromBean(WearableModel.class));
        wearables.scan().items().forEach(wearables::deleteItem);

    }
}
