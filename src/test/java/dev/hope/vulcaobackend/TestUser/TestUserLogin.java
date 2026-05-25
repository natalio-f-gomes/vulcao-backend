package dev.hope.vulcaobackend.TestUser;


import dev.hope.vulcaobackend.Repositories.UserRepository;
import dev.hope.vulcaobackend.RequestHandler;
import dev.hope.vulcaobackend.TestingData.LoginTestingData;
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


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
public class TestUserLogin {
    @LocalServerPort
    private int port;
    @Autowired
    private RestTestClient restTestClient;

    @Autowired
    private UserRepository repo;

    LoginTestingData userLoginData = new LoginTestingData();


    private RequestHandler requestHandler;

    @BeforeEach
    void setUp() {
        this.requestHandler = new RequestHandler(restTestClient);
    }

    /** Scenario 25: User logs in with valid credentials */
    @Test
    void testUserLoginWithValidData() throws JSONException {
        /** Step 1 User Registers */
        String user1RegistrationData = new JSONObject().put("username", "testUser1")
                .put("lastName", "Smith").put("firstName", "John")
                .put("email", "john.smith1@gmail.com")
                .put("password", "Password123").toString();
        String user1LoginData = new JSONObject().put("username", "testUser1")
                .put("password", "Password123").toString();
        requestHandler.userRegisterRequest(user1RegistrationData, 201, port);
        /** Step 1 User submits all required fields with strong password → 201 Created */
        String userToken = requestHandler.userLoginRequest(user1LoginData, 200, port);
        System.out.println("USER TOKEN " + userToken);
    }

    /** Scenario 25: User logs in with valid credentials */
    @Test
    void testUserLoginWithInvalidData() throws JSONException {
        /** Step 0 User Registers */
        String user1RegistrationData = new JSONObject().put("username", "testUser1")
                .put("lastName", "Smith").put("firstName", "John")
                .put("email", "john.smith1@gmail.com")
                .put("password", "Password123").toString();

        requestHandler.userRegisterRequest(user1RegistrationData, 201, port);
        /** Step 1 User submits valid username with wrong password → 401 Unauthorized */
        String user1LoginData = new JSONObject().put("username", "testUser1")
                .put("password", "Password23").toString();
        requestHandler.userLoginRequest(user1LoginData, 401, port);
        /** Step 2 User submits non-existent username → 401 Unauthorized (same response time) */
        String user2LoginData = new JSONObject().put("username", "testUser12")
                .put("password", "Password123").toString();
        requestHandler.userLoginRequest(user2LoginData, 401, port);
        /** Step 5 User submits empty username with valid password → 400 Bad Request */
        String user3LoginData = new JSONObject().put("username", "")
                .put("password", "Password123").toString();
        requestHandler.userLoginRequest(user3LoginData, 400, port);
        /** Step 6 User submits both fields empty → 400 Bad Request */
        String user4LoginData = new JSONObject().put("username", "")
                .put("password", "").toString();
        requestHandler.userLoginRequest(user4LoginData, 400, port);
        /** Step 7 User submits both fields null → 400 Bad Request */
        String user5LoginData = new JSONObject().put("username", null)
                .put("password", null).toString();
        requestHandler.userLoginRequest(user5LoginData, 400, port);
        /** Step 8 Response time for valid vs invalid username differs by < 50ms (no enumeration) */
    }

    /** Scenario 28: User login with SQL injection auth bypass */
    @Test
    void testUserLoginWithSQLInjectionData() throws JSONException {
        /** Step 1 User Registers */
        String user1RegistrationData = new JSONObject().put("username", "testUser1")
                .put("lastName", "Smith").put("firstName", "John")
                .put("email", "john.smith1@gmail.com")
                .put("password", "Password123").toString();
        String[] loginDataArray = userLoginData.getInvalidLoginData();
        String[] loginDataArray2 = userLoginData.getLoginWithNoSqlInjectionData();
        String[] loginDataArray3 = userLoginData.getLoginWithDefaultCredentialsData();
        String[] loginDataArray4 = userLoginData.getLoginWithOversizedFieldsData();
        String[] loginDataArray5 = userLoginData.getLoginWithUnicodeNormalizationData();
        String[] loginDataArray6 = userLoginData.getLoginWithOversizedFieldsData();
        String[] loginDataArray7 = userLoginData.getLoginWithUnicodeNormalizationData();
        String[] loginDataArray8 = userLoginData.getLoginWithCrlfInjectionData();
        String[] loginDataArray9 = userLoginData.getLoginWithRedosBaitData();
        String[] loginDataArray10 = userLoginData.getLoginWithBruteForceData();

        requestHandler.userRegisterRequest(user1RegistrationData, 201, port);
        /** Scenario 28: User login with SQL injection auth bypass */
        for(int i=0;i<loginDataArray.length;i++){
            requestHandler.userLoginRequest(loginDataArray[i], 400, port);
        }
        /** Scenario 29: User login with NoSQL operator bypass */
        for(int i=0;i<loginDataArray2.length;i++){
            requestHandler.userLoginRequest(loginDataArray2[i], 400, port);
        }
        /** Scenario 30: User login with credential-stuffing list */
        for(int i=0;i<loginDataArray3.length;i++){
            requestHandler.userLoginRequest(loginDataArray3[i], 400, port);
        }
        /** Scenario 31: User login length attacks */
        for(int i=0;i<loginDataArray4.length;i++){
            requestHandler.userLoginRequest(loginDataArray4[i], 400, port);
        }
        /** Scenario 32: User login with Unicode normalization quirks */
        for(int i=0;i<loginDataArray5.length;i++){
            requestHandler.userLoginRequest(loginDataArray5[i], 400, port);
        }
        /** Scenario 35: User login with CRLF/header injection */
        for(int i=0;i<loginDataArray6.length;i++){
            requestHandler.userLoginRequest(loginDataArray6[i], 400, port);
        }
        /** Scenario 36: User login ReDoS bait */
        for(int i=0;i<loginDataArray7.length;i++){
            requestHandler.userLoginRequest(loginDataArray7[i], 400, port);
        }
        /** Scenario 37: User login  rate limiting */
        for(int i=0;i<loginDataArray8.length;i++){
            requestHandler.userLoginRequest(loginDataArray8[i], 400, port);
        }
        /** Scenario 38: User login mass-assignment */
        for(int i=0;i<loginDataArray9.length;i++){
            System.out.println("DATA: " + loginDataArray9[i]);
            requestHandler.userLoginRequest(loginDataArray9[i], 400, port);
        }

        /** Scenario 38: User login with brute-force*/
        for(int i=0;i<loginDataArray9.length;i++){
            requestHandler.userLoginRequest(loginDataArray10[i], 400, port);
        }
    }
    /** Scenario 39: User logs out */
    @Test
    void testUserLogout() throws JSONException {
        /** Step 1 User Registers */
        String user1RegistrationData = new JSONObject().put("username", "testUser1")
                .put("lastName", "Smith").put("firstName", "John")
                .put("email", "john.smith1@gmail.com")
                .put("password", "Password123").toString();
        String user1LoginData = new JSONObject().put("username", "testUser1")
                .put("password", "Password123").toString();
        requestHandler.userRegisterRequest(user1RegistrationData, 201, port);
        String user1Token = requestHandler.userLoginRequest(user1LoginData,200, port);
        /** Step 1 User logs out with valid token → 200 OK or 204 No Content */
        requestHandler.userLogoutRequest(user1Token, 200, port);
        /** Step 2 User logs out twice (second call after first) → 401 or 204 (idempotent) */
        requestHandler.userLogoutRequest(user1Token, 401, port);

        String user2RegistrationData = new JSONObject().put("username", "testUser2")
                .put("lastName", "Smith").put("firstName", "John")
                .put("email", "john.smith2@gmail.com")
                .put("password", "Password123").toString();
        String user2LoginData = new JSONObject().put("username", "testUser2")
                .put("password", "Password123").toString();
        requestHandler.userRegisterRequest(user2RegistrationData, 201, port);
        String user2Token = requestHandler.userLoginRequest(user2LoginData,200, port);
        /** Step 3 User logs out with a token that does not belong to the user */
        requestHandler.userLogoutRequest(user1Token, 401, port);
    }

}


/** Step 3 User logs out with expired token → 401 Unauthorized */
/** Step 4 User logs out with no token → 401 Unauthorized */
/** Step 5 User logs out with malformed token → 401 Unauthorized */
/** Step 6 After logout, the same token used on protected route → 401 if blacklist enabled */