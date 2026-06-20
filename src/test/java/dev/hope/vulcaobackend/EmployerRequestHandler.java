package dev.hope.vulcaobackend;

import org.springframework.test.web.servlet.client.RestTestClient;

public class EmployerRequestHandler {

    private static final String employerRegisterRequestEndpoint ="/api/v1/auth/employer/request/register/";
    private static final String employerRegisterConfirmEndpoint ="/api/v1/auth/employer/confirm/register/";
    private static final String employerLoginRequestEndpoint ="/api/v1/auth/employer/request/login/";
    private static final String employerLoginConfirmEndpoint ="/api/v1/auth/employer/confirm/login/";
    private static final String companyRegisterEndpoint ="/api/v1/company/register";



    private RestTestClient restTestClient;
    public EmployerRequestHandler(RestTestClient restTestClient) {
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

    public void employerLoginRequest(String username, int expectedStatus, int port){
        String URL = "http://localhost:%d".formatted(port);
        restTestClient.post().uri(URL + employerLoginRequestEndpoint + "/" + username)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isEqualTo(expectedStatus);
    }

    public String employerLoginConfirm(String mfaCode, String employer,  int expectedStatus, int port) {
        String URL = "http://localhost:%d".formatted(port);
        var cookies = restTestClient.post().uri(URL + employerLoginConfirmEndpoint + mfaCode)
                .header("Content-Type", "application/json")
                .body(employer)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .returnResult(String.class)
                .getResponseCookies();

        var authCookie = cookies.getFirst("AUTH_TOKEN");
        return authCookie != null ? authCookie.getValue() : null;
    }

    public int companyRegisterRequest(String token, String company,int expectedStatus,  int port) {
        String url = "http://localhost:%d".formatted(port);
        return restTestClient.post()
                .uri(url + companyRegisterEndpoint)
                .header("Content-Type", "application/json")
                .cookie("AUTH_TOKEN", token)
                .body(company)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .returnResult(String.class)   // or byte[].class — body type doesn't matter here
                .getStatus()
                .value();                     // HttpStatusCode -> int
    }


}
