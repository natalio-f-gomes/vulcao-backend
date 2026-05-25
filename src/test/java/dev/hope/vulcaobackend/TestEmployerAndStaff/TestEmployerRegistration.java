package dev.hope.vulcaobackend.TestEmployerAndStaff;

import dev.hope.vulcaobackend.DataTransferObjects.AccountDTOs.EmployerDTO;
import dev.hope.vulcaobackend.DataTransferObjects.AccountDTOs.StaffDTO;
import dev.hope.vulcaobackend.DataTransferObjects.CompanyDTO;
import dev.hope.vulcaobackend.DataTransferObjects.Offerings.ProductDTO;
import dev.hope.vulcaobackend.Models.Properties.Permissions;
import dev.hope.vulcaobackend.Repositories.UserRepository;
import dev.hope.vulcaobackend.RequestHandler;
import dev.hope.vulcaobackend.TestingData.CompanyData;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
public class TestEmployerRegistration  {
    @LocalServerPort
    private  int port;
    @Autowired
    private RestTestClient restTestClient;

    RegistrationTestingData registrationTestingData = new RegistrationTestingData();
    CompanyData companyData = new CompanyData();

    private RequestHandler requestHandler;
    @BeforeEach
    void setUp() {
        this.requestHandler = new RequestHandler(restTestClient);
    }

    @Test
    /** Scenario 1: Employer registers with valid data */
    void testEmployerRegistrationWithValidData() throws JSONException {
        String user1RegistrationData = new JSONObject().put("username", "testUser1")
                .put("lastName", "Smith").put("firstName", "John")
                .put("email", "john.smith1@gmail.com")
                .put("password", "Password123").toString();
        String user1LoginData = new JSONObject().put("username", "testUser1")
                .put("password", "Password123").toString();
        String user2LoginData = new JSONObject().put("username", "testUser2")
                .put("password", "Password123").toString();

        /** Step 1 Employer submits all required fields with strong password → 201 Created */
        requestHandler.employerRegisterRequest(user1RegistrationData, 201, port);

        requestHandler.employerRegisterRequest(user1RegistrationData, 409, port);

        String token = requestHandler.employerLoginRequest(user1LoginData, 200, port);

        System.out.println("Token: " + token);

        requestHandler.employerLogoutRequest(token, 200, port);

        requestHandler.employerLogoutRequest(token, 403, port);

        /** Step 2 Employer submits with optional name fields populated → 201 Created */
        String user2RegistrationData = new JSONObject().put("username", "testUser3")
                .put("lastName", " bgdhdghgdgd").put("firstName", "gngnfgngfn")
                .put("email", "john.smith2@gmail.com")
                .put("password", "Password123").toString();
        requestHandler.employerRegisterRequest(user2RegistrationData, 201, port);

        /** Step 3 User submits with international name characters (Pelé, अजय, 用户) → 201 Created */

        String[] userRegistrationData = registrationTestingData.getGoodUsernameData();
        for(int i=0;i<userRegistrationData.length;i++){
            requestHandler.employerRegisterRequest(userRegistrationData[i], 201, port);
        }
        /** Step 4 User submits with plus-tag email (user+tag@domain) → 201 Created */
        String[] userRegistrationData2 = registrationTestingData.getPlusTagEmailData();
        for(int i=0;i<userRegistrationData2.length;i++){
            requestHandler.employerRegisterRequest(userRegistrationData2[i], 201, port);
        }

        /** Step 5 User submits with subdomain-deep email → 201 Created */
        String[] userRegistrationData3 = registrationTestingData.getDeepSubdomainEmailData();
        for(int i=0;i<userRegistrationData3.length;i++){
            requestHandler.employerRegisterRequest(userRegistrationData3[i], 201, port);
        }
    }

    @Test
    /** Scenario 2: User registers with duplicate credentials within same entity */
    void testEmployerRegistrationWitDuplicateCredentials() throws JSONException {
        /** Step 1 User registers with email already taken by another user → 409 Conflict */
        /** Step 2 User registers with username already taken by another user → 409 Conflict */
        String[] userRegistrationData = registrationTestingData.getGoodUsernameData();
        for(int i=0;i<userRegistrationData.length;i++){
            requestHandler.employerRegisterRequest(userRegistrationData[i], 201, port);
            requestHandler.employerRegisterRequest(userRegistrationData[i], 409, port);
        }
        /** Step 3 User registers with case-variant of existing email (UPPER vs lower) → 409 Conflict */
        String userRegistrationData3 = registrationTestingData.getCaseVariantEmail();
        requestHandler.employerRegisterRequest(userRegistrationData3, 201, port);
        requestHandler.employerRegisterRequest(userRegistrationData3, 409, port);
        /** Step 4 User registers with case-variant of existing username → 409 Conflict */
        String userRegistrationData4 = registrationTestingData.getCaseVariantUsername();
        requestHandler.employerRegisterRequest(userRegistrationData4, 201, port);
        requestHandler.employerRegisterRequest(userRegistrationData4, 409, port);
        /** Step 5 User registers with whitespace-padded existing email → 409 Conflict */
        String userRegistrationData5 = registrationTestingData.getWhitespacePaddedEmail();
        requestHandler.employerRegisterRequest(userRegistrationData5, 400, port);
        requestHandler.employerRegisterRequest(userRegistrationData4, 409, port);
        /** Step 6 User registers with trailing newline on existing username → 400 Conflict */
        String userRegistrationData6 = registrationTestingData.getTrailingNewlineUsername();
        requestHandler.employerRegisterRequest(userRegistrationData6, 400, port);
        /** Step 7 User registers with Unicode NFC variant of NFD-stored email → 201  */
        String userRegistrationData7 = registrationTestingData.getNfcVariantEmail();
        requestHandler.employerRegisterRequest(userRegistrationData7, 400, port);
        /** Step 8 User registers with homograph variant of existing username (Cyrillic а vs Latin a) → 409 Conflict */

        String homograph = "existingUsera".replace("a", "\u0430");  // visually identical
        String user1 =  new JSONObject().put("username", homograph)
                .put("firstName", "Homo").put("lastName", "Graph")
                .put("email", "new8@example.com")
                .put("password", "Password123").toString();

        String user2 =  new JSONObject().put("username", "existingUsera")
                .put("firstName", "Homo").put("lastName", "Graph")
                .put("email", "new8@example.com")
                .put("password", "Password123").toString();
        requestHandler.employerRegisterRequest(user1, 400, port);
        String userRegistrationData8 = registrationTestingData.getHomographUsername();
        requestHandler.employerRegisterRequest(user2, 201, port);
        /** Step 9 User registers with zero-width-space appended to existing username → 201 Conflict */
        requestHandler.employerRegisterRequest(registrationTestingData.getZeroWidthSpaceUsername(), 400, port);

        /** Step 10 User registers with BOM prefix on existing username → 409 Conflict */
        requestHandler.employerRegisterRequest(registrationTestingData.getBomPrefixUsername(), 400, port);
        /** Step 11 User registers with null-byte truncation matching an existing username → 409 Conflict */
        requestHandler.employerRegisterRequest(registrationTestingData.getNullByteTruncatedUsername(),400, port);
    }

    /** Scenario 3: User registers with credentials owned by other entity types */
    @Test
    void testEmployerWithOthersCredentials() throws JSONException {
        /** Step 1 User registers with email already taken by an employer → 409 Conflict */
        /** Step 2 User registers with username matching an existing employer username → 409 Conflict */
        //@TODOS
        /** Step 2 User registers with email already taken by a staff account → 409 Conflict */
        /** Step 3 User registers with username matching an existing staff username → 409 Conflict */

        String employer1 = new JSONObject().put("username", "testUser1")
                .put("lastName", "Smith").put("firstName", "John")
                .put("email", "john.smith1@gmail.com")
                .put("password", "Password123").toString();

        String employer2 = new JSONObject().put("username", "testUser2")
                .put("lastName", "Smith").put("firstName", "John")
                .put("email", "john@gmail.com")
                .put("password", "Password123").toString();

        requestHandler.employerRegisterRequest(employer1, 201, port);
        requestHandler.employerRegisterRequest(employer2, 201, port);
        requestHandler.userRegisterRequest(employer1, 409, port);
        requestHandler.userRegisterRequest(employer2, 409, port);
    }

    /** Scenario 4: User registers with malformed email addresses */
    @Test
    void testEmployerRegisterWithInvalidEmail() throws JSONException {
        /** Step 1 User submits email with no @ symbol → 400 Bad Request */
        requestHandler.employerRegisterRequest(registrationTestingData.getEmailNoAtSymbol(), 400, port);
        /** Step 2 User submits email missing local part (@example.com) → 400 Bad Request */
        requestHandler.employerRegisterRequest(registrationTestingData.getEmailMissingLocal(), 400, port);
        /** Step 3 User submits email missing domain (user@) → 400 Bad Request */
        requestHandler.employerRegisterRequest(registrationTestingData.getEmailMissingDomain(), 400, port);
        /** Step 4 User submits email with spaces inside (user name@x.com) → 400 Bad Request */
        requestHandler.employerRegisterRequest(registrationTestingData.getEmailWithSpaces(), 400, port);
        /** Step 5 User submits email with double @ signs → 400 Bad Request */
        requestHandler.employerRegisterRequest(registrationTestingData.getEmailDoubleAt(), 400, port);
        /** Step 6 User submits email with leading dot in local part → 400 Bad Request */
        requestHandler.employerRegisterRequest(registrationTestingData.getEmailLeadingDot(), 400, port);
        /** Step 7 User submits email with trailing dot before @ → 400 Bad Request */
        requestHandler.employerRegisterRequest(registrationTestingData.getEmailTrailingDotBeforeAt(), 400, port);
        /** Step 8 User submits email with consecutive dots in local part → 400 Bad Request */
        requestHandler.employerRegisterRequest(registrationTestingData.getEmailConsecutiveDots(), 400, port);
        /** Step 9 User submits email with dot immediately after @ → 400 Bad Request */
        requestHandler.employerRegisterRequest(registrationTestingData.getEmailDotAfterAt(), 400, port);
        /** Step 10 User submits email with no TLD (user@host) → 400 Bad Request */
        requestHandler.employerRegisterRequest(registrationTestingData.getEmailNoTld(), 400, port);
        /** Step 11 User submits email with leading hyphen in domain → 400 Bad Request */
        requestHandler.employerRegisterRequest(registrationTestingData.getEmailLeadingHyphenDomain(), 400, port);
        /** Step 12 User submits email with trailing hyphen in domain → 400 Bad Request */
        requestHandler.employerRegisterRequest(registrationTestingData.getEmailTrailingHyphenDomain(), 400, port);
        /** Step 13 User submits email with HTML/script tags in local part → 400 Bad Request */
        requestHandler.employerRegisterRequest(registrationTestingData.getEmailWithHtmlTags(), 400, port);
        /** Step 15 User submits email with IPv6 literal host → 400 or 201 per policy */
        requestHandler.employerRegisterRequest(registrationTestingData.getEmailIpv6Literal(), 400, port);
        /** Step 16 User submits email with quoted local part containing special chars → 400 or 201 per RFC stance */
        requestHandler.employerRegisterRequest(registrationTestingData.getEmailQuotedLocal(), 400, port);
        /** Step 17 User submits very-short minimal email (a@b) → 400 Bad Request */
        requestHandler.employerRegisterRequest(registrationTestingData.getEmailVeryShort(), 400, port);
        /** Step 18 User submits email with local part exceeding 64 chars (RFC limit) → 400 Bad Request */
        requestHandler.employerRegisterRequest(registrationTestingData.getEmailLocalTooLong(), 400, port);
        /** Step 19 User submits email with TLD exceeding 64 chars → 400 Bad Request */
        requestHandler.employerRegisterRequest(registrationTestingData.getEmailTldTooLong(), 400, port);
        /** Step 20 User submits email with full address > 320 chars → 400 Bad Request */
        requestHandler.employerRegisterRequest(registrationTestingData.getEmailTooLongOverall(), 400, port);
        /** Step 21 User submits email with embedded CRLF then Bcc header → 400 Bad Request, no header smuggling */
        requestHandler.employerRegisterRequest(registrationTestingData.getEmailHeaderInjection(), 400, port);
        /** Step 22 User submits email with null byte then attacker domain → 400 Bad Request, no truncation */
        requestHandler.employerRegisterRequest(registrationTestingData.getEmailNullByteTruncation(), 400, port);
        /** Step 23 User submits email with %00 percent-encoded null → 400 Bad Request */
        requestHandler.employerRegisterRequest(registrationTestingData.getEmailPercentEncodedNull(), 400, port);
        /** Step 24 User submits Unicode email (用户@例子.广告) → 201 or 400 per IDN policy */
        requestHandler.employerRegisterRequest(registrationTestingData.getEmailUnicodeIdn(), 400, port);
        /** Step 25 User submits  email with emoji */
        requestHandler.employerRegisterRequest(registrationTestingData.getEmailWithEmoji(),400, port);
    }

    /** Scenario 5: User registers with weak password and missing required fields */
    @Test
    void testEmployerRegisterWithWeakPasswordAndMissingFields() throws JSONException {
        // Weak password
        String user = new JSONObject().put("username", "testUser1")
                .put("lastName", "Smith").put("firstName", "John")
                .put("email", "john.smith1@gmail.com")
                .put("password", "").toString();
        List<String> userRegistrationData = RegistrationTestingData.getUserDetailsForPasswordTesting();
        for(int i=0;i< userRegistrationData.size(); i++){
            requestHandler.employerRegisterRequest( userRegistrationData.get(i), 400, port);
        }
    }

    /** Scenario 6: User registers with missing required fields */
    @Test
    void testEmployerRegisterWithMissingFields() throws JSONException {
        /** Step 1 Employer submits with no body → 400 Bad Request */
        requestHandler.employerRegisterRequest("", 400, port);
        /** Step 2 Employer submits empty JSON object → 400 Bad Request */
        requestHandler.employerRegisterRequest(new JSONObject().toString(), 400, port);
        /** Step 3 Employer submits with empty username → 400 Bad Request */
        String user = new JSONObject().put("username", "")
                .put("lastName", "Smith").put("firstName", "John")
                .put("email", "john.smith1@gmail.com")
                .put("password", "grgrbtrhrebwhnrwtn").toString();
        requestHandler.employerRegisterRequest(user, 400, port);
        /** Step 4 Employer submits with empty email → 400 Bad Request */
        String user2 = new JSONObject().put("username", "testUserName")
                .put("lastName", "Smith").put("firstName", "John")
                .put("email", "")
                .put("password", "grgrbtrhrebwhnrwtn").toString();
        requestHandler.employerRegisterRequest(user2, 400, port);
        /** Step 5 Employer submits without password field → 400 Bad Request */
        String user3 = new JSONObject().put("username", "testUserName")
                .put("lastName", "Smith").put("firstName", "John")
                .put("email", "")
                .toString();
        requestHandler.employerRegisterRequest(user3, 400, port);
    }

    @Test
    /** Scenario 7: Employer registers with null field values */
    void testEmployerRegisterWithNullFields() throws JSONException {
        /** Step 1 User submits null username → 400 Bad Request */
        String user = new JSONObject().put("username", null)
                .put("lastName", "Smith").put("firstName", "John")
                .put("email", "john.smith1@gmail.com")
                .put("password", "grgrbtrhrebwhnrwtn").toString();
        requestHandler.employerRegisterRequest(user, 400, port);
        /** Step 2 User submits null email → 400 Bad Request */
        String user2 = new JSONObject().put("username", "username1")
                .put("lastName", "Smith").put("firstName", "John")
                .put("email", null)
                .put("password", "grgrbtrhrebwhnrwtn").toString();
        requestHandler.employerRegisterRequest(user2, 400, port);
        /** Step 3 Employer submits null password → 400 Bad Request */
        String user3 = new JSONObject().put("username", "username1")
                .put("lastName", "Smith").put("firstName", "John")
                .put("email", "john.smith1@gmail.com")
                .put("password", "null").toString();
        requestHandler.employerRegisterRequest(user3, 400, port);
        /** Step 4 Employer submits all fields null → 400 Bad Request */
        String user4 = new JSONObject().put("username", null)
                .put("lastName", null).put("firstName", null)
                .put("email", null)
                .put("password",null).toString();
        requestHandler.employerRegisterRequest(user4, 400, port);
    }

    @Test
    /** Scenario 8: Employer registers with whitespace-only or control-char fields */
    void testEmployerRegisterWithInvalidFields() throws JSONException {
        String[] invalidData = registrationTestingData.getUserWithInvalidData();
        for(int i=0;i<invalidData.length; i++){
            requestHandler.employerRegisterRequest(invalidData[i], 400, port);
        }
    }

    /** Scenario 10: Employer registers with length-boundary payloads */
    @Test
    void testEmployerRegisterWithLengthBoundaryFields() throws JSONException {
        List<String> invalidData = registrationTestingData.getUserWithLengthBoundaryData();
        for(int i=0;i<invalidData.size(); i++){
            requestHandler.employerRegisterRequest(invalidData.get(i), 400, port);
        }
    }

    /** Scenario 11: Employer registers with SQL injection payloads */
    @Test
    void testEmployerRegisterWithSQLInjectionPayload() throws JSONException {
        String[] invalidData = registrationTestingData.getUserWithSqlInjectionData();
        for(int i=0;i<invalidData.length; i++){
            requestHandler.employerRegisterRequest(invalidData[i], 400, port);
        }
    }

    /** Scenario 12: Employer registers with NoSQL operator smuggling */
    @Test
    void testUserRegisterWithNoSQLInjectionPayload() throws JSONException {
        String[] invalidData = registrationTestingData.getUserWithNoSqlInjectionData();
        for(int i=0;i<invalidData.length; i++){
            requestHandler.employerRegisterRequest(invalidData[i], 400, port);
        }
    }

    /** Scenario 13: Employer registers with LDAP injection payloads */
    @Test
    void testEmployerRegisterWithLDAPInjection() throws JSONException {
        String[] invalidData = registrationTestingData.getUserWithLdapInjectionData();
        for(int i=0;i<invalidData.length; i++){
            requestHandler.employerRegisterRequest(invalidData[i], 400, port);
        }
    }
    /** Scenario 14: Employer registers with malicious data */
    @Test
    void testEmployerRegisterWithMaliciousData() throws JSONException{
        String[] invalidData = registrationTestingData.getUserWithMaliciousData();
        for(int i=0;i<invalidData.length; i++){
            requestHandler.employerRegisterRequest(invalidData[i], 400, port);
        }
    }

    /** Scenario 14: Employer registers a company successfully, adds employees, and products*/
    @Test
    void testEmployerRegisterCompany() throws JSONException{
        String employer1 = new JSONObject().put("username", "testUser1")
                .put("lastName", "Smith").put("firstName", "John")
                .put("email", "john.smith1@gmail.com")
                .put("password", "Password123").toString();
        String employer1LoginData = new JSONObject().put("username", "testUser1")
                .put("password", "Password123").toString();
        String company1 = new JSONObject().put("name", "GreenLeaf Organics")
                .put("category", "RESTAURANT")
                .put("creatorUsername", "testUser1")
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

        requestHandler.employerRegisterRequest(employer1, 201, port);
        String employerToken1 = requestHandler.employerLoginRequest(employer1LoginData, 200, port);
        requestHandler.registerCompany(employerToken1,company1 ,201, port);
        CompanyDTO companyDetails = requestHandler.getCompanyByEmail("info@greenleaf.com", port);
        assert companyDetails != null;
        System.out.println(companyDetails.getCreatorUsername());
        String[] badCompanyData = companyData.getMaliciousCompanyData();
        /** Test Bad Data */
        for(int i=0;i<badCompanyData.length;i++){
            requestHandler.registerCompany(employerToken1,badCompanyData[i] ,400, port);
        }
        /** CRUD OFFERINGS*/
        String product1 = new JSONObject()
                .put("expirationDate", LocalDate.now().plusYears(1).toString())
                .put("productType", "DIET_SUPPLEMENT")
                .put("name", "Premium Wireless Headphones")
                .put("description", "Noise-cancelling over-ear headphones with 40h battery life.")
                .put("pricePerUnit", 299)
                .put("unitType", "PIECE")
                .put("currency", "USD")
                .put("stock", 150)
                .put("companyId", companyDetails.getId())
                .toString();
        requestHandler.employerAddProductRequest(employerToken1, 201, product1, port);

    }
}
