package dev.hope.vulcaobackend.TestOfferings;


import dev.hope.vulcaobackend.DataTransferObjects.AccountDTOs.EmployerDTO;
import dev.hope.vulcaobackend.DataTransferObjects.AccountDTOs.StaffDTO;
import dev.hope.vulcaobackend.DataTransferObjects.CompanyDTO;
import dev.hope.vulcaobackend.DataTransferObjects.Offerings.ProductDTO;
import dev.hope.vulcaobackend.Models.Properties.Permissions;
import dev.hope.vulcaobackend.RequestHandler;
import dev.hope.vulcaobackend.TestingData.ProductTestingData;
import dev.hope.vulcaobackend.TestingData.RegistrationTestingData;
import dev.hope.vulcaobackend.VulcaoBackendApplication;
import org.json.JSONArray;
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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
public class TestCRUDProducts {
    @LocalServerPort
    private  int port;
    @Autowired
    private RestTestClient restTestClient;

    RegistrationTestingData registrationTestingData = new RegistrationTestingData();


    private RequestHandler requestHandler;
    @BeforeEach
    void setUp() {
        this.requestHandler = new RequestHandler(restTestClient);
    }


    /** Scenario 1: Employer registers a company successfully, CRUD Products*/
    @Test
    void testEmployerCRUDProducts() throws JSONException{
        String employer1 = new JSONObject().put("username", "testEmp1")
                .put("lastName", "Smith").put("firstName", "John")
                .put("email", "john1.smith1@gmail.com")
                .put("password", "Password123").toString();
        String employer1LoginData = new JSONObject().put("username", "testEmp1")
                .put("password", "Password123").toString();
        String company1 = new JSONObject().put("name", "GreenLeaf Organics Products")
                .put("type", "RESTAURANT")
                .put("taxIdNumber", "TIN-2034567890")
                .put("description", "Organic food production and distribution")
                .put("phoneNumber", "+1-555-0234")
                .put("faxNumber", "+1-555-0235")
                .put("revenue", 1750000.75)
                .put("email", "info@greenleaf.com")
                .put("creatorUsername", "testEmp1")
                .put("category", "BARBERSHOP")
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
        requestHandler.employerRegisterRequest(employer1, 201, port);
        String employerToken1 = requestHandler.employerLoginRequest(employer1LoginData, 200, port);
        /** employer register a company */
        requestHandler.registerCompany(employerToken1, company1 ,201, port);
        CompanyDTO companyDetails = requestHandler.getCompanyByEmail("info@greenleaf.com", port);
        assert companyDetails != null;
        System.out.println(companyDetails.getCreatorUsername());

        EmployerDTO employer = requestHandler.getEmployerByUsername("testUser1", port );
        System.out.println(employer.getFirstName());
        ProductTestingData productTestingData = new ProductTestingData(companyDetails.getId(), companyDetails.getName(),companyDetails.getCreatorUsername(), employer.getId());
        JSONObject[] goodData = productTestingData.getGoodProductData();

        /** employer register a bunch of good products */
        for(int i=0; i< goodData.length; i++){
            requestHandler.employerAddProductRequest(employerToken1, 201, goodData[i].toString(), port);
        }
        List<ProductDTO> productList1 =  requestHandler.getAllProducts(port);

        JSONObject[] updatedProducts = productTestingData.getProductUpdateData();
        System.out.println("PRODUCT 1 LIST SIZE: "+ productList1.size());

        for(int i=0;i< productList1.size(); i++){
            System.out.println("EMPLOYER UPDATE PRODUCTS");
            JSONObject productToUpdate = updatedProducts[i].put("id", productList1.get(i).getId());
            requestHandler.employerUpdateProduct(employerToken1, productToUpdate.toString(), productList1.get(i).getId(), 200, port);
            ProductDTO p = requestHandler.getProductById(productList1.get(i).getId(), port);
            assert p.getName().equals(productToUpdate.get("name"));
            assert p.getExpirationDate().toString().equals(productToUpdate.get("expirationDate"));
            assert p.getDescription().equals(productToUpdate.get("description"));
            assert p.getStock().equals(productToUpdate.get("stock"));
            assert p.getCurrency().toString().equals(productToUpdate.get("currency"));
            assert p.getUpdatedBy().equals(productToUpdate.get("updatedBy"));
            assert p.getProductType().toString().equals(productToUpdate.get("productType"));
            assert p.getCompanyId().equals(productToUpdate.get("companyId"));
            assert p.getUnitType().toString().equals(productToUpdate.get("unitType"));
            //assert p.getPricePerUnit().toString().equals(productToUpdate.get("pricePerUnit").toString());
        }
        /** Test get products by company Name*/
        List<ProductDTO> productList2 =  requestHandler.getProductByCompanyId(companyDetails.getId(),port);
        for(ProductDTO product: productList2){
            System.out.println(product.getName());
            assert !product.getName().isEmpty();
            assert !product.getDescription().isEmpty();
            assert product.getPricePerUnit() >= 0;
            assert product.getUnitType()  != null;
            assert product.getCurrency() != null;
            assert product.getStock() >=0;
            assert product.getExpirationDate().getYear() >= LocalDate.now().getYear();
            assert product.getExpirationDate().getYear() <= LocalDate.now().getYear() + 20;
            assert product.getCompanyId().equals(companyDetails.getId());
        }
        /** Test get products by company Id*/
        List<ProductDTO> productList3 =  requestHandler.getProductByCompanyName(companyDetails.getName(),port);
        for(ProductDTO product: productList3){
            System.out.println(product.getName());
            assert !product.getName().isEmpty();
            assert !product.getDescription().isEmpty();
            assert product.getPricePerUnit() >= 0;
            assert product.getUnitType()  != null;
            assert product.getCurrency() != null;
            assert product.getStock() >=0;
            assert product.getExpirationDate().getYear() >= LocalDate.now().getYear();
            assert product.getExpirationDate().getYear() <= LocalDate.now().getYear() + 20;
            assert product.getCompanyId().equals(companyDetails.getId());
        }

        /** Test get products by Creator Id */
        List<ProductDTO> productList4 =  requestHandler.getProductsByCreatorId(employer.getId(),port);
        for(ProductDTO product: productList4){
            System.out.println(product.getName());
            assert !product.getName().isEmpty();
            assert !product.getDescription().isEmpty();
            assert product.getPricePerUnit() >= 0;
            assert product.getUnitType()  != null;
            assert product.getCurrency() != null;
            assert product.getStock() >=0;
            assert product.getExpirationDate().getYear() >= LocalDate.now().getYear();
            assert product.getExpirationDate().getYear() <= LocalDate.now().getYear() + 20;
            assert product.getCompanyId().equals(companyDetails.getId());
        }

        /** Test get products by Creator username */
        List<ProductDTO> productList5 =  requestHandler.getProductsByCreatorUsername(employer.getUsername(),port);
        for(ProductDTO product: productList5){
            System.out.println(product.getName());
            assert !product.getName().isEmpty();
            assert !product.getDescription().isEmpty();
            assert product.getPricePerUnit() >= 0;
            assert product.getUnitType()  != null;
            assert product.getCurrency() != null;
            assert product.getStock() >=0;
            assert product.getExpirationDate().getYear() >= LocalDate.now().getYear();
            assert product.getExpirationDate().getYear() <= LocalDate.now().getYear() + 20;
            assert product.getCompanyId().equals(companyDetails.getId());
        }

        /** Test get products by price */
        //get all products


        List<ProductDTO> allProducts =  requestHandler.getAllProducts(port);
        for(ProductDTO product: allProducts){
            System.out.println(product.getName());
            /** Test get products by Name */
            List<ProductDTO> productsByName =  requestHandler.getProductsByName(product.getName(), port);
            for(ProductDTO individualProduct: productsByName){
                assert individualProduct.getName().contains(product.getName());
                assert individualProduct.getExpirationDate().toString().equals(product.getExpirationDate().toString());
                assert individualProduct.getDescription().equals(product.getDescription());
                assert individualProduct.getStock().equals(product.getStock());
                assert individualProduct.getCurrency().toString().equals(product.getCurrency().toString());
                assert individualProduct.getUpdatedBy().equals(product.getUpdatedBy());
                assert individualProduct.getProductType().toString().equals(product.getProductType().toString());
                assert individualProduct.getCompanyId().equals(product.getCompanyId());
                assert individualProduct.getUnitType().toString().equals(product.getUnitType().toString());
            }
            /** Test get products by Price */
            List<ProductDTO> productsByPrice =  requestHandler.getProductsByPrice(product.getPricePerUnit(), port);
            for(ProductDTO individualProduct: productsByPrice){
                assert Objects.equals(individualProduct.getPricePerUnit(), product.getPricePerUnit());
                assert individualProduct.getName().contains(product.getName());
                assert individualProduct.getExpirationDate().toString().equals(product.getExpirationDate().toString());
                assert individualProduct.getDescription().equals(product.getDescription());
                assert individualProduct.getStock().equals(product.getStock());
                assert individualProduct.getCurrency().toString().equals(product.getCurrency().toString());
                assert individualProduct.getUpdatedBy().equals(product.getUpdatedBy());
                assert individualProduct.getProductType().toString().equals(product.getProductType().toString());
                assert individualProduct.getCompanyId().equals(product.getCompanyId());
                assert individualProduct.getUnitType().toString().equals(product.getUnitType().toString());
            }

        }

        /** employer register a bunch of bad products */
        JSONObject[] badData = productTestingData.getBadProductData();

        /** employer register a bunch of good products */
        for(int i=0; i< badData.length; i++){
            requestHandler.employerAddProductRequest(employerToken1, 400, badData[i].toString(), port);
        }
    }

    /** Scenario 1: Employer registers a company successfully, and then the STAFF CRUD Products*/
    @Test
    void testStaffCRUDProducts() throws JSONException{
        String employer1 = new JSONObject().put("username", "testUser1")
                .put("lastName", "Smith").put("firstName", "John")
                .put("email", "john.smith1@gmail.com")
                .put("password", "Password123").toString();
        String employer1LoginData = new JSONObject().put("username", "testUser1")
                .put("password", "Password123").toString();
        String company1 = new JSONObject().put("name", "GreenLeaf Organics")
                .put("type", "RESTAURANT")
                .put("taxIdNumber", "TIN-2034567890")
                .put("description", "Organic food production and distribution")
                .put("phoneNumber", "+1-555-0234")
                .put("creatorUsername", "testUser1")
                .put("category", "")
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
        requestHandler.employerRegisterRequest(employer1, 201, port);
        String employerToken1 = requestHandler.employerLoginRequest(employer1LoginData, 200, port);
        /** employer register a company */
        requestHandler.registerCompany(employerToken1,company1 ,201, port);
        CompanyDTO companyDetails = requestHandler.getCompanyByEmail("info@greenleaf.com", port);
        assert companyDetails != null;
        System.out.println(companyDetails.getCreatorUsername());


        JSONObject staffRegistrationData1 =  new JSONObject().put("username", "johnsmith")
                .put("firstName", "John").put("lastName", "Smith")
                .put("email", "john@example.com")
                .put("password", "Password123").put("companyId", companyDetails.getId());
        JSONObject staffLoginData1 =  new JSONObject().put("username", "johnsmith")
                .put("password", "Password123");



        requestHandler.staffRegisterRequest(staffRegistrationData1.toString(), 201, employerToken1, port);
        String staffToken =  requestHandler.staffLoginRequest(staffLoginData1.toString(), 200, port);
        System.out.println("Staff token: " + staffToken);


        StaffDTO staff = requestHandler.getStaffByUsername("johnsmith", port );
        System.out.println(staff.getFirstName());

        ProductTestingData productTestingData = new ProductTestingData(companyDetails.getId(), companyDetails.getName(),staffRegistrationData1.get("username").toString(), staff.getId());
        JSONObject[] goodData = productTestingData.getGoodProductData();

        /** employer register a bunch of good products without permissions */
        for(int i=0; i< goodData.length; i++){
            requestHandler.staffAddProductRequest(staffToken, 403, goodData[i].toString(), port);
        }

        Set<Permissions> staff1Permissions = new HashSet<>();
        staff1Permissions.add(Permissions.ADD_PRODUCT);
        staff1Permissions.add(Permissions.UPDATE_PRODUCT);
        JSONArray array = new JSONArray();
        for(Permissions p: staff1Permissions){
            array.put(p.name());
        }
        String staffPermissions = array.toString();

        requestHandler.employerUpdateStaffPermissions(employerToken1,staffPermissions, staff.getId(),200, port);

        /** staff register a bunch of good products with permissions */
        for(int i=0; i< goodData.length; i++){
            requestHandler.staffAddProductRequest(staffToken, 201, goodData[i].toString(), port);
        }

        List<ProductDTO> productList1 =  requestHandler.getAllProducts(port);

        JSONObject[] updatedProducts = productTestingData.getProductUpdateData();
        System.out.println("PRODUCT 1 LIST SIZE: "+ productList1.size());

        for(int i=0;i< productList1.size(); i++){
            System.out.println("STAFF UPDATE PRODUCTS");
            JSONObject productToUpdate = updatedProducts[i].put("id", productList1.get(i).getId());
            requestHandler.staffUpdateProduct(staffToken, productToUpdate.toString(), productList1.get(i).getId(), 200, port);
            ProductDTO p = requestHandler.getProductById(productList1.get(i).getId(), port);
            assert p.getName().equals(productToUpdate.get("name"));
            assert p.getExpirationDate().toString().equals(productToUpdate.get("expirationDate"));
            assert p.getDescription().equals(productToUpdate.get("description"));
            assert p.getStock().equals(productToUpdate.get("stock"));
            assert p.getCurrency().toString().equals(productToUpdate.get("currency"));
            assert p.getUpdatedBy().equals(productToUpdate.get("updatedBy"));
            assert p.getProductType().toString().equals(productToUpdate.get("productType"));
            assert p.getCompanyId().equals(productToUpdate.get("companyId"));
            assert p.getUnitType().toString().equals(productToUpdate.get("unitType"));
            //assert p.getPricePerUnit().toString().equals(productToUpdate.get("pricePerUnit").toString());
        }
        /** Test get products by company Name*/
        List<ProductDTO> productList2 =  requestHandler.getProductByCompanyId(companyDetails.getId(),port);
        for(ProductDTO product: productList2){
            System.out.println(product.getName());
            assert !product.getName().isEmpty();
            assert !product.getDescription().isEmpty();
            assert product.getPricePerUnit() >= 0;
            assert product.getUnitType()  != null;
            assert product.getCurrency() != null;
            assert product.getStock() >=0;
            assert product.getExpirationDate().getYear() >= LocalDate.now().getYear();
            assert product.getExpirationDate().getYear() <= LocalDate.now().getYear() + 20;
            assert product.getCompanyId().equals(companyDetails.getId());
        }
        /** Test get products by company Id*/
        List<ProductDTO> productList3 =  requestHandler.getProductByCompanyName(companyDetails.getName(),port);
        for(ProductDTO product: productList3){
            System.out.println(product.getName());
            assert !product.getName().isEmpty();
            assert !product.getDescription().isEmpty();
            assert product.getPricePerUnit() >= 0;
            assert product.getUnitType()  != null;
            assert product.getCurrency() != null;
            assert product.getStock() >=0;
            assert product.getExpirationDate().getYear() >= LocalDate.now().getYear();
            assert product.getExpirationDate().getYear() <= LocalDate.now().getYear() + 20;
            assert product.getCompanyId().equals(companyDetails.getId());
        }

        /** Test get products by Creator Id */
        List<ProductDTO> productList4 =  requestHandler.getProductsByCreatorId(staff.getId(),port);
        for(ProductDTO product: productList4){
            System.out.println(product.getName());
            assert !product.getName().isEmpty();
            assert !product.getDescription().isEmpty();
            assert product.getPricePerUnit() >= 0;
            assert product.getUnitType()  != null;
            assert product.getCurrency() != null;
            assert product.getStock() >=0;
            assert product.getExpirationDate().getYear() >= LocalDate.now().getYear();
            assert product.getExpirationDate().getYear() <= LocalDate.now().getYear() + 20;
            assert product.getCompanyId().equals(companyDetails.getId());
        }

        /** Test get products by Creator username */
        List<ProductDTO> productList5 =  requestHandler.getProductsByCreatorUsername(staff.getUsername(),port);
        for(ProductDTO product: productList5){
            System.out.println(product.getName());
            assert !product.getName().isEmpty();
            assert !product.getDescription().isEmpty();
            assert product.getPricePerUnit() >= 0;
            assert product.getUnitType()  != null;
            assert product.getCurrency() != null;
            assert product.getStock() >=0;
            assert product.getExpirationDate().getYear() >= LocalDate.now().getYear();
            assert product.getExpirationDate().getYear() <= LocalDate.now().getYear() + 20;
            assert product.getCompanyId().equals(companyDetails.getId());
        }

        /** Test get products by price */
        //get all products


        List<ProductDTO> allProducts =  requestHandler.getAllProducts(port);
        for(ProductDTO product: allProducts){
            System.out.println(product.getName());
            /** Test get products by Name */
            List<ProductDTO> productsByName =  requestHandler.getProductsByName(product.getName(), port);
            for(ProductDTO individualProduct: productsByName){
                assert individualProduct.getName().contains(product.getName());
                assert individualProduct.getExpirationDate().toString().equals(product.getExpirationDate().toString());
                assert individualProduct.getDescription().equals(product.getDescription());
                assert individualProduct.getStock().equals(product.getStock());
                assert individualProduct.getCurrency().toString().equals(product.getCurrency().toString());
                assert individualProduct.getUpdatedBy().equals(product.getUpdatedBy());
                assert individualProduct.getProductType().toString().equals(product.getProductType().toString());
                assert individualProduct.getCompanyId().equals(product.getCompanyId());
                assert individualProduct.getUnitType().toString().equals(product.getUnitType().toString());
            }
            /** Test get products by Price */
            List<ProductDTO> productsByPrice =  requestHandler.getProductsByPrice(product.getPricePerUnit(), port);
            for(ProductDTO individualProduct: productsByPrice){
                assert Objects.equals(individualProduct.getPricePerUnit(), product.getPricePerUnit());
                assert individualProduct.getName().contains(product.getName());
                assert individualProduct.getExpirationDate().toString().equals(product.getExpirationDate().toString());
                assert individualProduct.getDescription().equals(product.getDescription());
                assert individualProduct.getStock().equals(product.getStock());
                assert individualProduct.getCurrency().toString().equals(product.getCurrency().toString());
                assert individualProduct.getUpdatedBy().equals(product.getUpdatedBy());
                assert individualProduct.getProductType().toString().equals(product.getProductType().toString());
                assert individualProduct.getCompanyId().equals(product.getCompanyId());
                assert individualProduct.getUnitType().toString().equals(product.getUnitType().toString());
            }

        }
    }
}
