package dev.hope.vulcaobackend.AuthTesting;


import dev.hope.vulcaobackend.CleanTable;
import dev.hope.vulcaobackend.RequestHandler;
import dev.hope.vulcaobackend.VulcaoBackendApplication;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
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
public class EmployerAuthTesting extends CleanTable {
    @LocalServerPort
    private int port;
    @Autowired
    private RestTestClient restTestClient;

    private RequestHandler requestHandler;

    private EmployerTestData employerTestData = new EmployerTestData();

    @BeforeEach
    void setUp() {
        this.requestHandler = new RequestHandler(restTestClient);
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
    void testRegisterEmployerWithValidData() throws JSONException, IOException, InterruptedException {
        System.out.println("=== Starting test ===");
        JSONObject emp1 = new JSONObject().put("username", "nataliog")
                .put("lastName", "gomes").put("firstName", "natalio")
                .put("email", "natalio.f.gomes0009@proton.me")
                .put("password", "Password123");



        List<JSONObject> employerTestingData = employerTestData.validEmployers();
        for(JSONObject employer: employerTestingData){
            String email = employer.get("email").toString();
            String username = employer.get("username").toString();
            registerEmployer(email, username, employer);
        }


    }

    public void registerEmployer(String employerEmail, String username, JSONObject employer) throws InterruptedException, IOException {
        System.out.println("Sending registration request...");
        requestHandler.employerRegisterRequest(employerEmail, username, 202, port);

        Thread.sleep(2000);
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
                Thread.sleep(1000);
            }
        }

        System.out.println("Final verification code: " + code);
        System.out.println("=== Test completed ===");

        // Change this to fail the test if no code found
        assertNotNull(code, "No verification code found in any email after " + maxAttempts + " attempts");

        requestHandler.employerRegisterConfirm(code, employer.toString(), 201, port);
    }
}
