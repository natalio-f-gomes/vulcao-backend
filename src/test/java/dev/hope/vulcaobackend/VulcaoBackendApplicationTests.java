package dev.hope.vulcaobackend;

import dev.hope.vulcaobackend.Models.AccountEntities.EmployerModel;
import dev.hope.vulcaobackend.Models.AccountEntities.StaffModel;
import dev.hope.vulcaobackend.Models.AccountEntities.UserModel;
import dev.hope.vulcaobackend.Models.CompanyModel;
import dev.hope.vulcaobackend.Models.Offerings.ProductModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@SpringBootTest
@ActiveProfiles("test")
class VulcaoBackendApplicationTests {

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

		DynamoDbTable<ProductModel> musics = enhancedClient.table("Music",
				TableSchema.fromBean(ProductModel.class));
		musics.scan().items().forEach(musics::deleteItem);

		DynamoDbTable<ProductModel> tvShows = enhancedClient.table("TvShows",
				TableSchema.fromBean(ProductModel.class));
		tvShows.scan().items().forEach(tvShows::deleteItem);

		DynamoDbTable<ProductModel> movies = enhancedClient.table("Movies",
				TableSchema.fromBean(ProductModel.class));
		movies.scan().items().forEach(movies::deleteItem);
	}
	@Test
	void contextLoads() {
	}


}
