package dev.hope.vulcaobackend.TestEmployerAndStaff;

import dev.hope.vulcaobackend.DataTransferObjects.AccountDTOs.EmployerDTO;
import dev.hope.vulcaobackend.DataTransferObjects.AccountDTOs.StaffDTO;
import dev.hope.vulcaobackend.DataTransferObjects.CompanyDTO;
import dev.hope.vulcaobackend.DataTransferObjects.Offerings.ProductDTO;
import dev.hope.vulcaobackend.Models.Properties.Permissions;
import dev.hope.vulcaobackend.RequestHandler;
import dev.hope.vulcaobackend.TestingData.LoginTestingData;
import dev.hope.vulcaobackend.TestingData.ProductTestingData;
import dev.hope.vulcaobackend.TestingData.RegistrationTestingData;
import dev.hope.vulcaobackend.TestingData.StaffRegistrationData;
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
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
public class TestStaffRegistration  {
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


    /** Scenario 1: Employer registers with valid data */
    @Test
    void testStaffRegistrationWithValidData() throws JSONException {
        String employer1 = new JSONObject().put("username", "testEmployer1")
                .put("lastName", "Smith").put("firstName", "John")
                .put("email", "john.smith1@gmail.com")
                .put("password", "Password123").toString();
        String employer1LoginData = new JSONObject().put("username", "testEmployer1")
                .put("password", "Password123").toString();
        String company1 = new JSONObject().put("name", "GreenLeaf Organics")
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

        requestHandler.employerRegisterRequest(employer1, 201, port);
        String employerToken1 = requestHandler.employerLoginRequest(employer1LoginData, 200, port);
        requestHandler.registerCompany(employerToken1,company1 ,201, port);
        CompanyDTO companyDetails = requestHandler.getCompanyByEmail("info@greenleaf.com", port);
        assert companyDetails != null;
        System.out.println(companyDetails.getCreatorUsername());

        String user1RegistrationData = new JSONObject().put("username", "testStaff1")
                .put("lastName", "Smith").put("firstName", "John")
                .put("email", "john.smith2@gmail.com")
                .put("companyId", companyDetails.getId())
                .put("password", "Password123").toString();

        String user1LoginData = new JSONObject().put("username", "testStaff1")
                .put("password", "Password123").toString();
        String user2LoginData = new JSONObject().put("username", "testUser2")
                .put("password", "Password123").toString();

        /** Step 1 Employer submits all required fields with strong password → 201 Created */
        requestHandler.staffRegisterRequest(user1RegistrationData, 201,employerToken1, port);

        requestHandler.staffRegisterRequest(user1RegistrationData, 409, employerToken1, port);

        /** Step 1 Staff submits valid username with wrong password → 401 Unauthorized */
        String staff1LoginData = new JSONObject().put("username", "testStaff1")
                .put("password", "Password23").toString();
        requestHandler.staffLoginRequest(staff1LoginData, 401, port);

        requestHandler.staffLoginRequest(user2LoginData, 401, port);

        /** Step 2 User submits non-existent username → 401 Unauthorized (same response time) */
        String staff2LoginData = new JSONObject().put("username", "testUser12")
                .put("password", "Password123").toString();
        requestHandler.staffLoginRequest(user2LoginData, 401, port);
        /** Step 5 User submits empty username with valid password → 400 Bad Request */
        String user3LoginData = new JSONObject().put("username", "")
                .put("password", "Password123").toString();
        requestHandler.staffLoginRequest(user3LoginData, 400, port);
        /** Step 6 User submits both fields empty → 400 Bad Request */
        String user4LoginData = new JSONObject().put("username", "")
                .put("password", "").toString();
        requestHandler.staffLoginRequest(user4LoginData, 400, port);
        /** Step 7 User submits both fields null → 400 Bad Request */
        String user5LoginData = new JSONObject().put("username", null)
                .put("password", null).toString();
        requestHandler.staffLoginRequest(user5LoginData, 400, port);


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

        requestHandler.staffLoginRequest(user1RegistrationData, 200, port);
        /** Scenario 28: User login with SQL injection auth bypass */
        for(int i=0;i<loginDataArray.length;i++){
            requestHandler.staffLoginRequest(loginDataArray[i], 400, port);
        }
        /** Scenario 29: User login with NoSQL operator bypass */
        for(int i=0;i<loginDataArray2.length;i++){
            requestHandler.staffLoginRequest(loginDataArray2[i], 400, port);
        }
        /** Scenario 30: User login with credential-stuffing list */
        for(int i=0;i<loginDataArray3.length;i++){
            requestHandler.staffLoginRequest(loginDataArray3[i], 400, port);
        }
        /** Scenario 31: User login length attacks */
        for(int i=0;i<loginDataArray4.length;i++){
            requestHandler.staffLoginRequest(loginDataArray4[i], 400, port);
        }
        /** Scenario 32: User login with Unicode normalization quirks */
        for(int i=0;i<loginDataArray5.length;i++){
            requestHandler.staffLoginRequest(loginDataArray5[i], 400, port);
        }
        /** Scenario 35: User login with CRLF/header injection */
        for(int i=0;i<loginDataArray6.length;i++){
            requestHandler.staffLoginRequest(loginDataArray6[i], 400, port);
        }
        /** Scenario 36: User login ReDoS bait */
        for(int i=0;i<loginDataArray7.length;i++){
            requestHandler.staffLoginRequest(loginDataArray7[i], 400, port);
        }
        /** Scenario 37: User login  rate limiting */
        for(int i=0;i<loginDataArray8.length;i++){
            requestHandler.staffLoginRequest(loginDataArray8[i], 400, port);
        }
        /** Scenario 38: User login mass-assignment */
        for(int i=0;i<loginDataArray9.length;i++){
            System.out.println("DATA: " + loginDataArray9[i]);
            requestHandler.staffLoginRequest(loginDataArray9[i], 400, port);
        }

        /** Scenario 38: User login with brute-force*/
        for(int i=0;i<loginDataArray9.length;i++){
            requestHandler.staffLoginRequest(loginDataArray10[i], 400, port);
        }


        String staffToken = requestHandler.staffLoginRequest(user1LoginData, 200, port);

        System.out.println("Token: " + staffToken);

        requestHandler.staffLogoutRequest(staffToken, 200, port);

        requestHandler.staffLogoutRequest(staffToken, 401, port);



        /** Step 2 Employer submits with optional name fields populated → 201 Created */
        String user2RegistrationData = new JSONObject().put("username", "testUser3")
                .put("lastName", " bgdhdghgdgd").put("firstName", "gngnfgngfn")
                .put("email", "user3-test.smith2@gmail.com")
                .put("password", "Password123").toString();
        requestHandler.userRegisterRequest(user2RegistrationData, 201, port);

        /** Step 3 User submits with international name characters (Pelé, अजय, 用户) → 201 Created */

        String[] userRegistrationData = registrationTestingData.getGoodUsernameData();
        for(int i=0;i<userRegistrationData.length;i++){
            requestHandler.employerRegisterRequest(userRegistrationData[i], 201, port);
        }
        /** Step 4 User submits with plus-tag email (user+tag@domain) → 201 Created */
        String[] userRegistrationData2 = registrationTestingData.getPlusTagEmailData();
        for(int i=0;i<userRegistrationData2.length;i++){
            requestHandler.userRegisterRequest(userRegistrationData2[i], 201, port);
        }

        /** Step 5 User submits with subdomain-deep email → 201 Created */
        String[] userRegistrationData3 = registrationTestingData.getDeepSubdomainEmailData();
        for(int i=0;i<userRegistrationData3.length;i++){
            requestHandler.userRegisterRequest(userRegistrationData3[i], 201, port);
        }

    }



    /** Scenario 14: Employer registers a company successfully, adds employees, and products*/
    @Test
    void testStaffRegistration() throws JSONException{
        String employer1 = new JSONObject().put("username", "testEmployer1")
                .put("lastName", "Smith").put("firstName", "John")
                .put("email", "john.smith1@gmail.com")
                .put("password", "Password123").toString();
        String employer1LoginData = new JSONObject().put("username", "testEmployer1")
                .put("password", "Password123").toString();
        String company1 = new JSONObject().put("name", "GreenLeaf Organics")
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

        requestHandler.employerRegisterRequest(employer1, 201, port);
        String employerToken1 = requestHandler.employerLoginRequest(employer1LoginData, 200, port);
        requestHandler.registerCompany(employerToken1,company1 ,201, port);
        CompanyDTO companyDetails = requestHandler.getCompanyByEmail("info@greenleaf.com", port);
        assert companyDetails != null;
        System.out.println(companyDetails.getCreatorUsername());
        StaffRegistrationData data = new StaffRegistrationData(companyDetails.getId());

        /** Register Staff with good username */
        String[] goodUsernameData = data.getGoodUsernameData();
        for(int i=0;i< goodUsernameData.length; i++){
            requestHandler.staffRegisterRequest(goodUsernameData[i],201, employerToken1, port);
        }

        /** Register Staff with plus email tag */
        String[] plusEmailTag = data.getPlusTagEmailData();
        for(int i=0;i< plusEmailTag.length; i++){
            requestHandler.staffRegisterRequest(plusEmailTag[i],201, employerToken1, port);
        }

        /** Register Staff with deep subdomain email data*/
        String[] subdomainData = data.getDeepSubdomainEmailData();
        for(int i=0;i< subdomainData.length; i++){
            requestHandler.staffRegisterRequest(subdomainData[i],201, employerToken1, port);
        }

        /** Register Staff with bad username */
        String[] badUsernameData = data.getBadUsernameData();
        for(int i=0;i< badUsernameData.length; i++){
            requestHandler.staffRegisterRequest(badUsernameData[i],400, employerToken1, port);
        }

        /** Register Staff with duplicates username */
        String staff1 = data.getDuplicateUsername();
        requestHandler.staffRegisterRequest(staff1,201, employerToken1, port);
        requestHandler.staffRegisterRequest(staff1,409, employerToken1, port);


        requestHandler.staffRegisterRequest(data.getWhitespacePaddedEmail(),400, employerToken1, port);


        requestHandler.staffRegisterRequest(data.getNfcVariantEmail(),400, employerToken1, port);


        requestHandler.staffRegisterRequest(data.getHomographUsername(),400, employerToken1, port);


        requestHandler.staffRegisterRequest(data.getTrailingNewlineUsername(),400, employerToken1, port);


        requestHandler.staffRegisterRequest(data.getZeroWidthSpaceUsername(),400, employerToken1, port);

        /** Step 10: BOM prefix on existing username */
        requestHandler.staffRegisterRequest(data.getBomPrefixUsername(),400, employerToken1, port);
        /** Step 11: Null-byte truncation matching existing username */
        requestHandler.staffRegisterRequest(data.getNullByteTruncatedUsername(),400, employerToken1, port);
        /** Step 1: Email with no @ symbol */
        requestHandler.staffRegisterRequest(data.getEmailNoAtSymbol(),400, employerToken1, port);
        requestHandler.staffRegisterRequest(data.getEmailMissingLocal(),400, employerToken1, port);

        requestHandler.staffRegisterRequest(data.getEmailMissingDomain(),400, employerToken1, port);
        requestHandler.staffRegisterRequest(data.getEmailWithSpaces(),400, employerToken1, port);
        requestHandler.staffRegisterRequest(data.getEmailDoubleAt(),400, employerToken1, port);
        requestHandler.staffRegisterRequest(data.getEmailLeadingDot(),400, employerToken1, port);
        requestHandler.staffRegisterRequest(data.getEmailTrailingDotBeforeAt(),400, employerToken1, port);
        requestHandler.staffRegisterRequest(data.getEmailConsecutiveDots(),400, employerToken1, port);
        requestHandler.staffRegisterRequest(data.getEmailNoTld(),400, employerToken1, port);
        requestHandler.staffRegisterRequest(data.getEmailDotAfterAt(),400, employerToken1, port);
        requestHandler.staffRegisterRequest(data.getEmailLeadingHyphenDomain(),400, employerToken1, port);
        requestHandler.staffRegisterRequest(data.getEmailTrailingHyphenDomain(),400, employerToken1, port);
        requestHandler.staffRegisterRequest(data.getEmailWithHtmlTags(),400, employerToken1, port);
        requestHandler.staffRegisterRequest(data.getEmailIpv4Literal(),400, employerToken1, port);
        requestHandler.staffRegisterRequest(data.getEmailIpv6Literal(),400, employerToken1, port);
        requestHandler.staffRegisterRequest(data.getEmailQuotedLocal(),400, employerToken1, port);
        requestHandler.staffRegisterRequest(data.getEmailVeryShort(),400, employerToken1, port);

        requestHandler.staffRegisterRequest(data.getEmailLocalTooLong(),400, employerToken1, port);
        requestHandler.staffRegisterRequest(data.getEmailTldTooLong(),400, employerToken1, port);
        requestHandler.staffRegisterRequest(data.getEmailTooLongOverall(),400, employerToken1, port);
        requestHandler.staffRegisterRequest(data.getEmailHeaderInjection(),400, employerToken1, port);
        requestHandler.staffRegisterRequest(data.getEmailNullByteTruncation(),400, employerToken1, port);
        requestHandler.staffRegisterRequest(data.getEmailPercentEncodedNull(),400, employerToken1, port);
        requestHandler.staffRegisterRequest(data.getEmailUnicodeIdn(),400, employerToken1, port);
        requestHandler.staffRegisterRequest(data.getEmailWithEmoji(),400, employerToken1, port);
        requestHandler.staffRegisterRequest(data.getPasswordEmpty(),400, employerToken1, port);
        requestHandler.staffRegisterRequest(data.getPasswordSingleChar(),400, employerToken1, port);
        requestHandler.staffRegisterRequest(data.getPasswordJustBelowMin(),400, employerToken1, port);
        requestHandler.staffRegisterRequest(data.getPasswordWhitespaceOnly(),400, employerToken1, port);
        requestHandler.staffRegisterRequest(data.getPasswordExtremelyLong(),400, employerToken1, port);
        requestHandler.staffRegisterRequest(data.getPasswordNullBytesOnly(),400, employerToken1, port);
        requestHandler.staffRegisterRequest(data.getPasswordNullByteTruncation(),400, employerToken1, port);

        requestHandler.staffRegisterRequest(data.getPasswordWithDiacritics(),201, employerToken1, port);
        requestHandler.staffRegisterRequest(data.getPasswordWithBom(),400, employerToken1, port);
        requestHandler.staffRegisterRequest(data.getPasswordWithZeroWidthSpace(),400, employerToken1, port);
        requestHandler.staffRegisterRequest(data.getPasswordEmojiOnly(),400, employerToken1, port);
        requestHandler.staffRegisterRequest(data.getPasswordPathTraversal(),400, employerToken1, port);
        requestHandler.staffRegisterRequest(data.getPasswordScriptTag(),400, employerToken1, port);
        requestHandler.staffRegisterRequest(data.getPasswordSqlInjection(),400, employerToken1, port);

        String[] staffData = data.getUserWithInvalidData();
        for(int i=0;i<staffData.length;i++){
            requestHandler.staffRegisterRequest(staffData[i], 400, employerToken1, port);
        }

       List<String> staffData2 = data.getUserWithLengthBoundaryData();
        for(int i=0;i<staffData2.size();i++){
            requestHandler.staffRegisterRequest(staffData2.get(i), 400, employerToken1, port);
        }

        String[] staffData3 = data.getUserWithSqlInjectionData();
        for(int i=0;i<staffData3.length;i++){
            requestHandler.staffRegisterRequest(staffData3[i], 400, employerToken1, port);
        }

        String[] staffData4 = data.getUserWithNoSqlInjectionData();
        for(int i=0;i<staffData4.length;i++){
            requestHandler.staffRegisterRequest(staffData4[i], 400, employerToken1, port);
        }

        String[] staffData5 = data.getUserWithLdapInjectionData();
        for(int i=0;i<staffData5.length;i++){
            requestHandler.staffRegisterRequest(staffData5[i], 400, employerToken1, port);
        }

        String[] staffData6 = data.getUserWithMaliciousData();
        for(int i=0;i<staffData6.length;i++){
            requestHandler.staffRegisterRequest(staffData6[i], 400, employerToken1, port);
        }

        List<String> staffData7 = data.getUserDetailsForPasswordTesting();
        for(int i=0;i<staffData7.size();i++){
            requestHandler.staffRegisterRequest(staffData7.get(i), 400, employerToken1, port);
        }

    }

    /**Staff Updates their account*/
    @Test
    void testStaffUpdateAccount() throws JSONException {
        String employer1 = new JSONObject().put("username", "testEmployer1")
                .put("lastName", "Smith").put("firstName", "John")
                .put("email", "john.smith1@gmail.com")
                .put("password", "Password123").toString();
        String employer1LoginData = new JSONObject().put("username", "testEmployer1")
                .put("password", "Password123").toString();
        String company1 = new JSONObject().put("name", "GreenLeaf Organics")
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

        requestHandler.employerRegisterRequest(employer1, 201, port);
        String employerToken1 = requestHandler.employerLoginRequest(employer1LoginData, 200, port);
        requestHandler.registerCompany(employerToken1, company1, 201, port);
        CompanyDTO companyDetails = requestHandler.getCompanyByEmail("info@greenleaf.com", port);
        assert companyDetails != null;
        System.out.println(companyDetails.getCreatorUsername());
        StaffRegistrationData data = new StaffRegistrationData(companyDetails.getId());

        /** Register Staff with good username */
        JSONObject staffRegistrationData1 =  new JSONObject().put("username", "johnsmith")
                .put("firstName", "John").put("lastName", "Smith")
                .put("email", "john@example.com")
                .put("password", "Password123").put("companyId", companyDetails.getId());
        JSONObject staffLoginData1 =  new JSONObject().put("username", "johnsmith")
                .put("password", "Password123");
        Set<Permissions> staffPermissions = new HashSet<>();
        staffPermissions.add(Permissions.ADD_PRODUCT);
        JSONArray array = new JSONArray();
        for(Permissions p: staffPermissions){
            array.put(p.name());
        }
        String staff1Permissions = array.toString();

        String staffAddressInfo =  new JSONObject().put("island", "Santiago")
                .put("city", "Praia")
                .put("zone", "Terra Brance")
                .put("postalCode", "29834242")
                .put("street","rua das luz").toString();

        requestHandler.staffRegisterRequest(staffRegistrationData1.toString(), 201, employerToken1, port);
        StaffDTO staff = requestHandler.getStaffByUsername("johnsmith", port);
        requestHandler.employerUpdateStaffPermissions(employerToken1, staff1Permissions,staff.getId(),200, port );
        String staffToken = requestHandler.staffLoginRequest(staffLoginData1.toString(), 200, port);
        /**  Update Staff address*/
        requestHandler.updateStaffAddress(staffToken, staffAddressInfo, "johnsmith", 200, port  );
        /**  Update Staff profile*/
        String staffProfileInfo = new JSONObject().put("firstName", "Jared").put("lastName", "Padaleck").toString();
        requestHandler.updateStaffProfile(staffToken, "johnsmith", staffProfileInfo, 200, port  );
        /**  Update Staff contact*/
        String staffContactInfo = new JSONObject().put("email", "smith.01@gmail.com").put("phoneNumber", "1307130137").toString();
        requestHandler.updateStaffContact(staffToken, staffContactInfo, "johnsmith", 200, port  );
        /**  Update Staff credentials*/
        String staffCredentialsInfo = new JSONObject().put("username", "new_smith").put("password", "newPassword").toString();
        //@TODO requestHandler.updateStaffCredentials(staffToken, staffCredentialsInfo, "johnsmith", 200, port  );
        /**  Update Staff permissions*/
        requestHandler.updateStaffAddress(staffToken, staffAddressInfo, "johnsmith", 403, port  );
        requestHandler.updateStaffProfile(staffToken, "johnsmith", staffProfileInfo, 403, port  );
        requestHandler.updateStaffContact(staffToken, staffContactInfo, "johnsmith", 403, port  );

        //@TODO: EMPLOYER CRUD STAFF PROFILE OF ITS OWN COMPANY AND TRIES TO CRUD PROFILE OF STAFFS THAT DOES NOT BELONG TO NEITHER OF HIS COMPANY
    }
    }
