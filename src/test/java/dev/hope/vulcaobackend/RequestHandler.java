package dev.hope.vulcaobackend;

import org.json.JSONObject;
import org.springframework.test.web.servlet.client.RestTestClient;

public class RequestHandler {

    private static final String employerRegisterRequestEndpoint ="/api/v1/auth/employer/request/register/";
    private static final String employerRegisterConfirmEndpoint ="/api/v1/auth/employer/confirm/register/";
    private static final String employerLoginEndpoint ="/api/v1/auth/employer/login";
    private static final String employerLogoutEndpoint ="/api/v1/auth/employer/logout";

    private RestTestClient restTestClient;
    public RequestHandler(RestTestClient restTestClient) {
        this.restTestClient = restTestClient;
    }

    public void employerRegisterRequest(String email, String username, int expectedStatus, int port){
        String URL = "http://localhost:%d".formatted(port);
        restTestClient.post().uri(URL + employerRegisterRequestEndpoint + email + "/" + username)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isEqualTo(expectedStatus);
    }

    public void employerRegisterConfirm(String mfaCode, String employer,  int expectedStatus, int port){
        String URL = "http://localhost:%d".formatted(port);
        restTestClient.post().uri(URL + employerRegisterConfirmEndpoint + mfaCode)
                .header("Content-Type", "application/json")
                .body(employer).exchange()
                .expectStatus().isEqualTo(expectedStatus);
    }

}
