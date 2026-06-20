package dev.hope.vulcaobackend.AuthTesting;


import dev.hope.vulcaobackend.CleanTable;
import dev.hope.vulcaobackend.EmployerRequestHandler;
import dev.hope.vulcaobackend.TestData.CompanyTestData;
import dev.hope.vulcaobackend.TestData.InvalidEmployerTestData;
import dev.hope.vulcaobackend.TestData.ValidAuthTestData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.client.RestTestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
@ActiveProfiles("local")
public class TestEmployerAuth extends CleanTable {
    @LocalServerPort
    private int port;
    @Autowired
    private RestTestClient restTestClient;

    private EmployerRequestHandler employerRequestHandler;

    private ValidAuthTestData employerTestData = new ValidAuthTestData();
    private InvalidEmployerTestData invalidEmployerTestData =new InvalidEmployerTestData();

    private CompanyTestData companyTestData = new CompanyTestData();

    @BeforeEach
    void setUp() {
        this.employerRequestHandler = new EmployerRequestHandler(restTestClient);
    }

    @BeforeEach
    void clearMailbox() throws IOException, InterruptedException {
        HttpClient.newHttpClient().send(
                HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8005/clear-store"))
                        .POST(HttpRequest.BodyPublishers.noBody())
                        .build(),
                HttpResponse.BodyHandlers.discarding());
    }

    @Test
    void testEmployerAuthWithValidData() throws JSONException, IOException, InterruptedException {
        System.out.println("=== Starting test ===");

        List<JSONObject> employerTestingData = employerTestData.validEmployers();
        for(JSONObject employer: employerTestingData){
            String email = employer.get("email").toString();
            String username = employer.get("username").toString();
            String password = employer.get("password").toString();
            registerEmployer(email, username, employer, 202, 201);
            JSONObject employerLoginData = new JSONObject();
            employerLoginData.put("username", username);
            employerLoginData.put("password", password);
            String token = loginEmployer(email, username, employerLoginData,202, 200);
        }

    }

    @Test
    void testCompanyRegistrationWithValidData() throws JSONException, IOException, InterruptedException {
        System.out.println("=== Starting test ===");
        JSONObject employer1 = new JSONObject().put("username", "nataliogomes")
                .put("firstName", "Natalio").put("lastName", "Gomes")
                .put("email", "natalio@gmail.com").put("password", "password");
        List<JSONObject> companies = companyTestData.validCompanies();
        String email = employer1.get("email").toString();
        String username = employer1.get("username").toString();
        registerEmployer(email, username, employer1, 202, 201);
        JSONObject employerLoginData = new JSONObject();
        employerLoginData.put("username", username);
        employerLoginData.put("password", "password");
        String token = loginEmployer(email, username, employerLoginData,202, 200);
        for(JSONObject company: companies){
            company.remove("creatorUsername");
            company.put("creatorUsername", username);
            company.put("imagesMetadata", new JSONArray());
            employerRequestHandler.companyRegisterRequest(token, company.toString(), 204, port);
        }
    }

    //@Test TODO later
    void testEmployerAuthWithInvalidData() throws JSONException, IOException, InterruptedException {
        System.out.println("=== Starting test ===");

        List<JSONObject> employerTestingData = invalidEmployerTestData.invalidEmployers();
        for(JSONObject employer: employerTestingData){
            try{
                String email = employer.get("email").toString();
                String username = employer.get("username").toString();
                String password = employer.get("password").toString();
                employerRequestHandler.employerRegisterRequest(email, username, 400, port);
                employerRequestHandler.employerLoginRequest(username, 400, port);
            }catch (JSONException e){
                //
            }

        }

    }

    public void registerEmployer(String employerEmail, String username, JSONObject employer, int expectedRequestStatus, int expectedConfirmStatus) throws InterruptedException, IOException {
        System.out.println("Sending registration request...");
        employerRequestHandler.employerRegisterRequest(employerEmail, username, expectedRequestStatus, port);

        Thread.sleep(250);
        System.out.println("After sleep, checking /store...");

        HttpClient client = HttpClient.newHttpClient();
        String code = null;
        int maxAttempts = 10;
        int attempt = 0;

        while (attempt < maxAttempts && code == null) {
            System.out.println("Attempt " + (attempt + 1) + " of " + maxAttempts);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8005/store"))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response status: " + response.statusCode());
            System.out.println("Response body: " + response.body());

            JsonNode root = new ObjectMapper().readTree(response.body());
            JsonNode emails = root.get("emails");

            System.out.println("Number of emails in store: " + (emails != null ? emails.size() : 0));

            if (emails != null) {
                for (JsonNode email : emails) {
                    String to = email.path("destination").path("to").path(0).asText("");
                    System.out.println("Found email to: " + to);

                    if (!to.equals(employerEmail)) {
                        System.out.println("Skipping - wrong recipient");
                        continue;
                    }

                    String body = email.path("body").path("text").asText("");
                    System.out.println("Email body: " + body);

                    Matcher m = Pattern.compile("\\d{4,8}").matcher(body);
                    if (m.find()) {
                        code = m.group();
                        System.out.println("Found code: " + code);
                        break;
                    } else {
                        System.out.println("No code pattern found in body");
                    }
                }
            }

            if (code == null) {
                attempt++;
                System.out.println("Code not found, waiting 1 second...");
                Thread.sleep(250);
            }
        }

        System.out.println("Final verification code: " + code);
        System.out.println("=== Test completed ===");

        // Change this to fail the test if no code found
        assertNotNull(code, "No verification code found in any email after " + maxAttempts + " attempts");

        employerRequestHandler.employerRegisterConfirm(code, employer.toString(), expectedConfirmStatus, port);
    }

    public String loginEmployer(String employerEmail, String username, JSONObject employer, int expectedRequestStatus, int expectedConfirmStatus) throws InterruptedException, IOException {
        System.out.println("Sending login request...");
        employerRequestHandler.employerLoginRequest(username, expectedRequestStatus, port);

        Thread.sleep(250);
        System.out.println("After sleep, checking /store...");

        HttpClient client = HttpClient.newHttpClient();
        String code = null;
        int maxAttempts = 10;
        int attempt = 0;

        while (attempt < maxAttempts && code == null) {
            System.out.println("Attempt " + (attempt + 1) + " of " + maxAttempts);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8005/store"))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response status: " + response.statusCode());
            System.out.println("Response body: " + response.body());

            JsonNode root = new ObjectMapper().readTree(response.body());
            JsonNode emails = root.get("emails");

            System.out.println("Number of emails in store: " + (emails != null ? emails.size() : 0));

            if (emails != null) {
                // Check emails in REVERSE order (newest first)
                for (int i = emails.size() - 1; i >= 0; i--) {
                    JsonNode email = emails.get(i);
                    String to = email.path("destination").path("to").path(0).asText("");
                    System.out.println("Found email to: " + to);

                    if (!to.equals(employerEmail)) {
                        System.out.println("Skipping - wrong recipient");
                        continue;
                    }

                    String body = email.path("body").path("text").asText("");
                    System.out.println("Email body: " + body);

                    Matcher m = Pattern.compile("\\d{4,8}").matcher(body);
                    if (m.find()) {
                        code = m.group();
                        System.out.println("Found code: " + code);
                        break;
                    } else {
                        System.out.println("No code pattern found in body");
                    }
                }
            }

            if (code == null) {
                attempt++;
                System.out.println("Code not found, waiting 1 second...");
                Thread.sleep(250);
            }
        }

        System.out.println("Final verification code: " + code);
        System.out.println("=== Test completed ===");

        assertNotNull(code, "No verification code found in any email after " + maxAttempts + " attempts");

        return employerRequestHandler.employerLoginConfirm(code, employer.toString(), expectedConfirmStatus, port);
    }




}
