package dev.hope.vulcaobackend.TestUser;


import dev.hope.vulcaobackend.GoodDummyData;
import dev.hope.vulcaobackend.Repositories.UserRepository;
import dev.hope.vulcaobackend.RequestHandler;
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

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
public class TestUserRegistration {
    @LocalServerPort
    private  int port;
    @Autowired
    private RestTestClient restTestClient;

    @Autowired
    private UserRepository repo;

    RegistrationTestingData userData = new RegistrationTestingData();

    private GoodDummyData goodDummyData = new GoodDummyData();

    private RequestHandler requestHandler;
    @BeforeEach
    void setUp() {
        this.requestHandler = new RequestHandler(restTestClient);
    }

    @Test
    /** Scenario 1: User registers with valid data */
    void testUserRegistrationWithValidData() throws JSONException {
        String user1RegistrationData = new JSONObject().put("username", "testUser1")
                .put("lastName", "Smith").put("firstName", "John")
                .put("email", "john.smith1@gmail.com")
                .put("password", "Password123").toString();
        String user1LoginData = new JSONObject().put("username", "testUser1")
                .put("password", "Password123").toString();
        String user2LoginData = new JSONObject().put("username", "testUser2")
                .put("password", "Password123").toString();

        /** Step 1 User submits all required fields with strong password → 201 Created */
        requestHandler.userRegisterRequest(user1RegistrationData, 201, port);

        requestHandler.userRegisterRequest(user1RegistrationData, 409, port);

        requestHandler.userLoginRequest(user2LoginData, 401, port);

        String token = requestHandler.userLoginRequest(user1LoginData, 200, port);

        System.out.println("Token: " + token);

        requestHandler.userLogoutRequest(token, 200, port);

        requestHandler.userLogoutRequest(token, 401, port);

        /** Step 2 User submits with optional name fields populated → 201 Created */
        String user2RegistrationData = new JSONObject().put("username", "testUser3")
                .put("lastName", " bgdhdghgdgd").put("firstName", "gngnfgngfn")
                .put("email", "john.smith2@gmail.com")
                .put("password", "Password123").toString();
        requestHandler.userRegisterRequest(user2RegistrationData, 201, port);

        /** Step 3 User submits with international name characters (Pelé, अजय, 用户) → 201 Created */

        String[] userRegistrationData = userData.getGoodUsernameData();
        for(int i=0;i<userRegistrationData.length;i++){
            requestHandler.userRegisterRequest(userRegistrationData[i], 201, port);
        }
        /** Step 4 User submits with plus-tag email (user+tag@domain) → 201 Created */
        String[] userRegistrationData2 = userData.getPlusTagEmailData();
        for(int i=0;i<userRegistrationData2.length;i++){
            requestHandler.userRegisterRequest(userRegistrationData2[i], 201, port);
        }

        /** Step 5 User submits with subdomain-deep email → 201 Created */
        String[] userRegistrationData3 = userData.getDeepSubdomainEmailData();
        for(int i=0;i<userRegistrationData3.length;i++){
            requestHandler.userRegisterRequest(userRegistrationData3[i], 201, port);
        }
    }

    @Test
    /** Scenario 2: User registers with duplicate credentials within same entity */
    void testUserRegistrationWitDuplicateCredentials() throws JSONException {
        /** Step 1 User registers with email already taken by another user → 409 Conflict */
        /** Step 2 User registers with username already taken by another user → 409 Conflict */
        String[] userRegistrationData = userData.getGoodUsernameData();
        for(int i=0;i<userRegistrationData.length;i++){
            requestHandler.userRegisterRequest(userRegistrationData[i], 201, port);
            requestHandler.userRegisterRequest(userRegistrationData[i], 409, port);
        }
        /** Step 3 User registers with case-variant of existing email (UPPER vs lower) → 409 Conflict */
        String userRegistrationData3 = userData.getCaseVariantEmail();
        requestHandler.userRegisterRequest(userRegistrationData3, 201, port);
        requestHandler.userRegisterRequest(userRegistrationData3, 409, port);
        /** Step 4 User registers with case-variant of existing username → 409 Conflict */
        String userRegistrationData4 = userData.getCaseVariantUsername();
        requestHandler.userRegisterRequest(userRegistrationData4, 201, port);
        requestHandler.userRegisterRequest(userRegistrationData4, 409, port);
        /** Step 5 User registers with whitespace-padded existing email → 409 Conflict */
        String userRegistrationData5 = userData.getWhitespacePaddedEmail();
        requestHandler.userRegisterRequest(userRegistrationData5, 400, port);
        requestHandler.userRegisterRequest(userRegistrationData4, 409, port);
        /** Step 6 User registers with trailing newline on existing username → 400 Conflict */
        String userRegistrationData6 = userData.getTrailingNewlineUsername();
        requestHandler.userRegisterRequest(userRegistrationData6, 400, port);
        /** Step 7 User registers with Unicode NFC variant of NFD-stored email → 201  */
        String userRegistrationData7 = userData.getNfcVariantEmail();
        requestHandler.userRegisterRequest(userRegistrationData7, 400, port);
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
        requestHandler.userRegisterRequest(user1, 400, port);
        String userRegistrationData8 = userData.getHomographUsername();
        requestHandler.userRegisterRequest(user2, 201, port);
        /** Step 9 User registers with zero-width-space appended to existing username → 201 Conflict */
        requestHandler.userRegisterRequest(userData.getZeroWidthSpaceUsername(), 400, port);

        /** Step 10 User registers with BOM prefix on existing username → 409 Conflict */
        requestHandler.userRegisterRequest(userData.getBomPrefixUsername(), 400, port);
        /** Step 11 User registers with null-byte truncation matching an existing username → 409 Conflict */
        requestHandler.userRegisterRequest(userData.getNullByteTruncatedUsername(),400, port);
    }

    /** Scenario 3: User registers with credentials owned by other entity types */
    @Test
    void testUserWithOthersCredentials() throws JSONException {
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

        requestHandler.employerRegisterRequest(employer1, 200, port);
        requestHandler.employerRegisterRequest(employer2, 200, port);
        requestHandler.userRegisterRequest(employer1, 409, port);
        requestHandler.userRegisterRequest(employer2, 409, port);
    }

    /** Scenario 4: User registers with malformed email addresses */
    @Test
    void testUserRegisterWithInvalidEmail() throws JSONException {
        /** Step 1 User submits email with no @ symbol → 400 Bad Request */
        requestHandler.userRegisterRequest(userData.getEmailNoAtSymbol(), 400, port);
        /** Step 2 User submits email missing local part (@example.com) → 400 Bad Request */
        requestHandler.userRegisterRequest(userData.getEmailMissingLocal(), 400, port);
        /** Step 3 User submits email missing domain (user@) → 400 Bad Request */
        requestHandler.userRegisterRequest(userData.getEmailMissingDomain(), 400, port);
        /** Step 4 User submits email with spaces inside (user name@x.com) → 400 Bad Request */
        requestHandler.userRegisterRequest(userData.getEmailWithSpaces(), 400, port);
        /** Step 5 User submits email with double @ signs → 400 Bad Request */
        requestHandler.userRegisterRequest(userData.getEmailDoubleAt(), 400, port);
        /** Step 6 User submits email with leading dot in local part → 400 Bad Request */
        requestHandler.userRegisterRequest(userData.getEmailLeadingDot(), 400, port);
        /** Step 7 User submits email with trailing dot before @ → 400 Bad Request */
        requestHandler.userRegisterRequest(userData.getEmailTrailingDotBeforeAt(), 400, port);
        /** Step 8 User submits email with consecutive dots in local part → 400 Bad Request */
        requestHandler.userRegisterRequest(userData.getEmailConsecutiveDots(), 400, port);
        /** Step 9 User submits email with dot immediately after @ → 400 Bad Request */
        requestHandler.userRegisterRequest(userData.getEmailDotAfterAt(), 400, port);
        /** Step 10 User submits email with no TLD (user@host) → 400 Bad Request */
        requestHandler.userRegisterRequest(userData.getEmailNoTld(), 400, port);
        /** Step 11 User submits email with leading hyphen in domain → 400 Bad Request */
        requestHandler.userRegisterRequest(userData.getEmailLeadingHyphenDomain(), 400, port);
        /** Step 12 User submits email with trailing hyphen in domain → 400 Bad Request */
        requestHandler.userRegisterRequest(userData.getEmailTrailingHyphenDomain(), 400, port);
        /** Step 13 User submits email with HTML/script tags in local part → 400 Bad Request */
        requestHandler.userRegisterRequest(userData.getEmailWithHtmlTags(), 400, port);
        /** Step 15 User submits email with IPv6 literal host → 400 or 201 per policy */
        requestHandler.userRegisterRequest(userData.getEmailIpv6Literal(), 400, port);
        /** Step 16 User submits email with quoted local part containing special chars → 400 or 201 per RFC stance */
        requestHandler.userRegisterRequest(userData.getEmailQuotedLocal(), 400, port);
        /** Step 17 User submits very-short minimal email (a@b) → 400 Bad Request */
        requestHandler.userRegisterRequest(userData.getEmailVeryShort(), 400, port);
        /** Step 18 User submits email with local part exceeding 64 chars (RFC limit) → 400 Bad Request */
        requestHandler.userRegisterRequest(userData.getEmailLocalTooLong(), 400, port);
        /** Step 19 User submits email with TLD exceeding 64 chars → 400 Bad Request */
        requestHandler.userRegisterRequest(userData.getEmailTldTooLong(), 400, port);
        /** Step 20 User submits email with full address > 320 chars → 400 Bad Request */
        requestHandler.userRegisterRequest(userData.getEmailTooLongOverall(), 400, port);
        /** Step 21 User submits email with embedded CRLF then Bcc header → 400 Bad Request, no header smuggling */
        requestHandler.userRegisterRequest(userData.getEmailHeaderInjection(), 400, port);
        /** Step 22 User submits email with null byte then attacker domain → 400 Bad Request, no truncation */
        requestHandler.userRegisterRequest(userData.getEmailNullByteTruncation(), 400, port);
        /** Step 23 User submits email with %00 percent-encoded null → 400 Bad Request */
        requestHandler.userRegisterRequest(userData.getEmailPercentEncodedNull(), 400, port);
        /** Step 24 User submits Unicode email (用户@例子.广告) → 201 or 400 per IDN policy */
        requestHandler.userRegisterRequest(userData.getEmailUnicodeIdn(), 400, port);
        /** Step 25 User submits  email with emoji */
        requestHandler.userRegisterRequest(userData.getEmailWithEmoji(),400, port);
    }

    /** Scenario 5: User registers with weak password and missing required fields */
    @Test
    void testUserRegisterWithWeakPasswordAndMissingFields() throws JSONException {
        // Weak password
        String user = new JSONObject().put("username", "testUser1")
                .put("lastName", "Smith").put("firstName", "John")
                .put("email", "john.smith1@gmail.com")
                .put("password", "").toString();
        List<String> userRegistrationData = RegistrationTestingData.getUserDetailsForPasswordTesting();
        for(int i=0;i< userRegistrationData.size(); i++){
            requestHandler.userRegisterRequest( userRegistrationData.get(i), 400, port);
        }
    }

    /** Scenario 6: User registers with missing required fields */
    @Test
    void testUserRegisterWithMissingFields() throws JSONException {
        /** Step 1 User submits with no body → 400 Bad Request */
        requestHandler.userRegisterRequest("", 400, port);
        /** Step 2 User submits empty JSON object → 400 Bad Request */
        requestHandler.userRegisterRequest(new JSONObject().toString(), 400, port);
        /** Step 3 User submits with empty username → 400 Bad Request */
        String user = new JSONObject().put("username", "")
                .put("lastName", "Smith").put("firstName", "John")
                .put("email", "john.smith1@gmail.com")
                .put("password", "grgrbtrhrebwhnrwtn").toString();
        requestHandler.userRegisterRequest(user, 400, port);
        /** Step 4 User submits with empty email → 400 Bad Request */
        String user2 = new JSONObject().put("username", "testUserName")
                .put("lastName", "Smith").put("firstName", "John")
                .put("email", "")
                .put("password", "grgrbtrhrebwhnrwtn").toString();
        requestHandler.userRegisterRequest(user2, 400, port);
        /** Step 5 User submits without password field → 400 Bad Request */
        String user3 = new JSONObject().put("username", "testUserName")
                .put("lastName", "Smith").put("firstName", "John")
                .put("email", "")
                .toString();
        requestHandler.userRegisterRequest(user3, 400, port);
    }

    @Test
    /** Scenario 7: User registers with null field values */
    void testUserRegisterWithNullFields() throws JSONException {
        /** Step 1 User submits null username → 400 Bad Request */
        String user = new JSONObject().put("username", null)
                .put("lastName", "Smith").put("firstName", "John")
                .put("email", "john.smith1@gmail.com")
                .put("password", "grgrbtrhrebwhnrwtn").toString();
        requestHandler.userRegisterRequest(user, 400, port);
        /** Step 2 User submits null email → 400 Bad Request */
        String user2 = new JSONObject().put("username", "username1")
                .put("lastName", "Smith").put("firstName", "John")
                .put("email", null)
                .put("password", "grgrbtrhrebwhnrwtn").toString();
        requestHandler.userRegisterRequest(user2, 400, port);
        /** Step 3 User submits null password → 400 Bad Request */
        String user3 = new JSONObject().put("username", "username1")
                .put("lastName", "Smith").put("firstName", "John")
                .put("email", "john.smith1@gmail.com")
                .put("password", "null").toString();
        requestHandler.userRegisterRequest(user3, 400, port);
        /** Step 4 User submits all fields null → 400 Bad Request */
        String user4 = new JSONObject().put("username", null)
                .put("lastName", null).put("firstName", null)
                .put("email", null)
                .put("password",null).toString();
        requestHandler.userRegisterRequest(user4, 400, port);
    }

    @Test
    /** Scenario 8: User registers with whitespace-only or control-char fields */
    void testUserRegisterWithInvalidFields() throws JSONException {
        String[] invalidData = userData.getUserWithInvalidData();
        for(int i=0;i<invalidData.length; i++){
            requestHandler.userRegisterRequest(invalidData[i], 400, port);
        }
    }

    /** Scenario 10: User registers with length-boundary payloads */
    @Test
    void testUserRegisterWithLengthBoundaryFields() throws JSONException {
        List<String> invalidData = userData.getUserWithLengthBoundaryData();
        for(int i=0;i<invalidData.size(); i++){
            requestHandler.userRegisterRequest(invalidData.get(i), 400, port);
        }
    }

    /** Scenario 11: User registers with SQL injection payloads */
    @Test
    void testUserRegisterWithSQLInjectionPayload() throws JSONException {
        String[] invalidData = userData.getUserWithSqlInjectionData();
        for(int i=0;i<invalidData.length; i++){
            requestHandler.userRegisterRequest(invalidData[i], 400, port);
        }
    }

    /** Scenario 12: User registers with NoSQL operator smuggling */
    @Test
    void testUserRegisterWithNoSQLInjectionPayload() throws JSONException {
        String[] invalidData = userData.getUserWithNoSqlInjectionData();
        for(int i=0;i<invalidData.length; i++){
            requestHandler.userRegisterRequest(invalidData[i], 400, port);
        }
    }

    /** Scenario 13: User registers with LDAP injection payloads */
    @Test
    void testUserRegisterWithLDAPInjection() throws JSONException {
        String[] invalidData = userData.getUserWithLdapInjectionData();
        for(int i=0;i<invalidData.length; i++){
            requestHandler.userRegisterRequest(invalidData[i], 400, port);
        }
    }

    @Test
    void testUserRegisterWithMaliciousData() throws JSONException{
        String[] invalidData = userData.getUserWithMaliciousData();
        for(int i=0;i<invalidData.length; i++){
            requestHandler.userRegisterRequest(invalidData[i], 400, port);
        }
    }
}
