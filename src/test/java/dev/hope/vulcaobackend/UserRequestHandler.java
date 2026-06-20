package dev.hope.vulcaobackend;

import org.springframework.test.web.servlet.client.RestTestClient;

public class UserRequestHandler {

    private static final String userRegisterRequestEndpoint ="/api/v1/auth/user/register/request/";
    private static final String userRegisterConfirmEndpoint ="/api/v1/auth/user/register/confirm/";
    private static final String userLoginRequestEndpoint ="/api/v1/auth/user/login/request/";
    private static final String userLoginConfirmEndpoint ="/api/v1/auth/user/login/confirm/";



    private RestTestClient restTestClient;
    public UserRequestHandler(RestTestClient restTestClient) {
        this.restTestClient = restTestClient;
    }

    public void userRegisterRequest(String email, String username, int expectedStatus, int port){
        String URL = "http://localhost:%d".formatted(port);
        restTestClient.post().uri(URL + userRegisterRequestEndpoint + email + "/" + username)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isEqualTo(expectedStatus);
    }

    public void userRegisterConfirm(String mfaCode, String employer,  int expectedStatus, int port){
        String URL = "http://localhost:%d".formatted(port);
        restTestClient.post().uri(URL + userRegisterConfirmEndpoint + mfaCode)
                .header("Content-Type", "application/json")
                .body(employer).exchange()
                .expectStatus().isEqualTo(expectedStatus);
    }

    public void userLoginRequest(String username, int expectedStatus, int port){
        String URL = "http://localhost:%d".formatted(port);
        restTestClient.post().uri(URL + userLoginRequestEndpoint + "/" + username)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isEqualTo(expectedStatus);
    }

    public String userLoginConfirm(String mfaCode, String employer,  int expectedStatus, int port) {
        String URL = "http://localhost:%d".formatted(port);
        var cookies = restTestClient.post().uri(URL + userLoginConfirmEndpoint + mfaCode)
                .header("Content-Type", "application/json")
                .body(employer)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .returnResult(String.class)
                .getResponseCookies();

        var authCookie = cookies.getFirst("AUTH_TOKEN");
        return authCookie != null ? authCookie.getValue() : null;
    }

}
