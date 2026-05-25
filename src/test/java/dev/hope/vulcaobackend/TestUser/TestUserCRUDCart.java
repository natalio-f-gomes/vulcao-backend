package dev.hope.vulcaobackend.TestUser;



import dev.hope.vulcaobackend.DataTransferObjects.AccountDTOs.EmployerDTO;
import dev.hope.vulcaobackend.DataTransferObjects.AccountDTOs.UserDTO;
import dev.hope.vulcaobackend.DataTransferObjects.CompanyDTO;
import dev.hope.vulcaobackend.DataTransferObjects.Offerings.ProductDTO;
import dev.hope.vulcaobackend.Models.Cart.CartItemModel;
import dev.hope.vulcaobackend.RequestHandler;
import dev.hope.vulcaobackend.TestingData.LoginTestingData;
import dev.hope.vulcaobackend.TestingData.ProductTestingData;
import dev.hope.vulcaobackend.TestingData.RegistrationTestingData;
import dev.hope.vulcaobackend.VulcaoBackendApplication;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
public class TestUserCRUDCart {
    @LocalServerPort
    private  int port;
    @Autowired
    private RestTestClient restTestClient;
    LoginTestingData userLoginData = new LoginTestingData();

    RegistrationTestingData registrationTestingData = new RegistrationTestingData();


    private RequestHandler requestHandler;
    @BeforeEach
    void setUp() {
        this.requestHandler = new RequestHandler(restTestClient);
    }

    /** Scenario 1: Employer registers a company successfully, CRUD Products*/
    @Test
    void testUserCRUDCart() throws JSONException {
        String employer1 = new JSONObject().put("username", "testEmployer1")
                .put("lastName", "Smith").put("firstName", "John")
                .put("email", "john.smith1@gmail.com")
                .put("password", "Password123").toString();
        String employer1LoginData = new JSONObject().put("username", "testEmployer1")
                .put("password", "Password123").toString();
        String company1 = new JSONObject().put("name", "GreenLeaf Organics Products")
                .put("type", "RESTAURANT")
                .put("taxIdNumber", "TIN-2034567890")
                .put("description", "Organic food production and distribution")
                .put("phoneNumber", "+1-555-0234")
                .put("faxNumber", "+1-555-0235")
                .put("revenue", 1750000.75)
                .put("email", "info@greenleaf.com")
                .put("address", new JSONObject()
                        .put("street", "456 Harvest Lane")
                        .put("city", "Portland")
                        .put("state", "OR")
                        .put("postalCode", "97201")
                        .put("island", "Santiago")
                        .put("zone", ""))
                .put("status", "OPERATING")
                .toString();
        /** employer registers */
        requestHandler.employerRegisterRequest(employer1, 200, port);
        String employerToken1 = requestHandler.employerLoginRequest(employer1LoginData, 200, port);
        /** employer register a company */
        requestHandler.registerCompany(employerToken1, company1, 201, port);
        CompanyDTO companyDetails = requestHandler.getCompanyByEmail("info@greenleaf.com", port);
        assert companyDetails != null;
        System.out.println(companyDetails.getCreatorUsername());

        EmployerDTO employer = requestHandler.getEmployerByUsername("testEmployer1", port);
        System.out.println(employer.getFirstName());
        ProductTestingData productTestingData = new ProductTestingData(companyDetails.getId(), companyDetails.getName(), companyDetails.getCreatorUsername(), employer.getId());
        JSONObject[] goodData = productTestingData.getGoodProductData();

        /** employer register a bunch of good products */
        for (int i = 0; i < goodData.length; i++) {
            requestHandler.employerAddProductRequest(employerToken1, 201, goodData[i].toString(), port);
        }
        List<ProductDTO> productList1 = requestHandler.getAllProducts(port);

        /** USER REGISTERS AND THEN ADD PRODUCTS TO THE CART*/
        String user1RegistrationData = new JSONObject().put("username", "testUser1")
                .put("lastName", "Natalio").put("firstName", "Gomes")
                .put("email", "natalio.gomes@gmail.com")
                .put("password", "Password123").toString();
        String user1LoginData = new JSONObject().put("username", "testUser1")
                .put("password", "Password123").toString();

        /** Step 1 User submits all required fields with strong password → 201 Created */
        requestHandler.userRegisterRequest(user1RegistrationData, 201, port);

        String token = requestHandler.userLoginRequest(user1LoginData, 200, port);
        /** User adds products to cart*/
        for(ProductDTO product: productList1){
            requestHandler.userAddItemToCart(token, product.getId(), 1, 200, port);
        }


        UserDTO user = requestHandler.getUserProfile(token, port);
        System.out.println("User profile username is: " + user.getUsername() );
        List<CartItemModel> items  = requestHandler.getUserCart(token, port);
        System.out.println("SIZE OF THE ITEMS "+ items.size());
        assert items.size() == productList1.size();
        /** User Gets cart item by id*/
        for(CartItemModel item: items){
            CartItemModel found =  requestHandler.getCartItemById(token, item.getItemId(), port);
            System.out.println("Quantity before updating: "+ found.getQuantity());
            System.out.println("ITEM FETCHED BY ID: " + found.getItemId());
            assert found.getItemType() == item.getItemType();
            assert found.getItemId().equals(item.getItemId());
            assert found.getCartId().equals(item.getCartId());
            assert Objects.equals(found.getQuantity(), item.getQuantity());
        }

        /** User adds products to cart again. The purpose is to verify if the cart already has an item and proceeds to update the quantity of the item*/

        for(ProductDTO product: productList1){
            requestHandler.userAddItemToCart(token, product.getId(), 1, 200, port);
        }
        List<CartItemModel> items2  = requestHandler.getUserCart(token, port);
        System.out.println("SIZE OF THE ITEMS "+ items.size());
        assert items2.size() == productList1.size();

        //loop through items NOT items2
        for(CartItemModel item: items){
            CartItemModel found =  requestHandler.getCartItemById(token, item.getItemId(), port);
            System.out.println("ITEM FETCHED BY ID: " + found.getItemId());
            System.out.println("Quantity after updating: "+ found.getQuantity());
            assert found.getItemType() == item.getItemType();
            assert found.getItemId().equals(item.getItemId());
            assert found.getCartId().equals(item.getCartId());
            assert Objects.equals(found.getQuantity(), item.getQuantity()+1);
        }


        /** User Deletes all items from the cart*/
        for(CartItemModel item: items){
            requestHandler.userDeletesItemFromCart(token, item.getItemId(), 200, port);
        }
        List<CartItemModel> items3  = requestHandler.getUserCart(token, port);
        assert items3.isEmpty();

        requestHandler.userLogoutRequest(token, 200, port);
        for(CartItemModel item: items){
            requestHandler.userDeletesItemFromCart(token, item.getItemId(), 403, port);
        }
        for(ProductDTO product: productList1){
            requestHandler.userAddItemToCart(token, product.getId(), 1, 403, port);
        }
    }
}
