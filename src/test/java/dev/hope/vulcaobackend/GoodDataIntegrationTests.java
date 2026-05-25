package dev.hope.vulcaobackend;



import dev.hope.vulcaobackend.DataTransferObjects.AccountDTOs.EmployerDTO;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
public class GoodDataIntegrationTests {
    @LocalServerPort
    private  int port;
    @Autowired private RestTestClient restTestClient;


    private GoodDummyData goodDummyData = new GoodDummyData();

    private RequestHandler requestHandler;
    @BeforeEach
    void setUp() {
        this.requestHandler = new RequestHandler(restTestClient);
    }


    @Test
    void testUserAuthentication() throws JSONException {
        Map<Integer, String> randomUserRegistrationData = goodDummyData.getRandomUserRegisterData();
        int randomNumber = randomUserRegistrationData.keySet().iterator().next();
        String randomUser = randomUserRegistrationData.values().iterator().next();

        requestHandler.userRegisterRequest(randomUser, 201, port);

        requestHandler.userRegisterRequest(randomUser, 409, port);

        String randomUserLoginData = goodDummyData.getUserLoginData(randomNumber);
        int fakeNum = 0;
        if(randomNumber == fakeNum){
            fakeNum+=1;
        }

        String fakeRandomUserLoginData = goodDummyData.getUserLoginData(fakeNum);

        requestHandler.userLoginRequest(fakeRandomUserLoginData, 401, port);

        String token = requestHandler.userLoginRequest(randomUserLoginData, 200, port);

        System.out.println("Token: " + token);

        requestHandler.userLogoutRequest(token, 200, port);

        requestHandler.userLogoutRequest(token, 401, port);


        /** User tries to register the company*/
        String userToken2 = requestHandler.userLoginRequest(randomUserLoginData, 200, port);
        String randomCompany = goodDummyData.getRandomCompany("TestUser");
        requestHandler.registerCompany(userToken2, randomCompany, 401, port);
        String employerToken2 = requestHandler.employerLoginRequest(randomUserLoginData, 401, port);
        requestHandler.registerCompany(employerToken2, randomCompany, 403, port);

    }
    @Test
    void testEmployerActions() throws JSONException {
        String URL = "http://localhost:%d".formatted(port);

        Map<Integer, String> randomEmployer = goodDummyData.getRandomEmployerRegisterData();
        int randomNumber = randomEmployer.keySet().iterator().next();
        String randomEmployer1 = randomEmployer.values().iterator().next();
        String employer1LoginData = goodDummyData.getEmployerLoginData(randomNumber);
        System.out.println("DEBUG: EMPLOYER LOGIN DATA: " + employer1LoginData);
        //employer registers

        requestHandler.employerRegisterRequest(randomEmployer1, 200, port);
        String employerToken = requestHandler.employerLoginRequest( employer1LoginData, 200, port);

        System.out.println("Employer Token: " + employerToken);
        WebClient webClient = WebClient.create();
        //get all employers
        List<EmployerDTO> res =  webClient.get().uri(URL + "/api/v1/public/employer/get/all").retrieve().bodyToMono(new ParameterizedTypeReference<List<EmployerDTO>>() {} )
                .block();
        String creatorUsername = res.getFirst().getUsername();
        String randomCompany = goodDummyData.getRandomCompany(creatorUsername);

        //create a company
        requestHandler.registerCompany(employerToken, randomCompany, 201, port);
        /** Employer tries to register an employee in a company that does not belong him */
        Map<Integer, String> randomEmployerData2 = goodDummyData.getRandomEmployerRegisterData();
        if(randomEmployerData2.equals(randomEmployer)){randomEmployerData2 = goodDummyData.getRandomEmployerRegisterData();}
        int randomNumber2 = randomEmployerData2.keySet().iterator().next();
        String randomEmployer2 = randomEmployerData2.values().iterator().next();
        String employer2LoginData = goodDummyData.getEmployerLoginData(randomNumber2);
        System.out.println("DEBUG: EMPLOYER LOGIN DATA: " + employer1LoginData);

        requestHandler.employerRegisterRequest(randomEmployer2, 200, port);
        String employer2Token = requestHandler.employerLoginRequest( employer2LoginData, 200, port);


        List<EmployerDTO> res2 =  webClient.get().uri(URL + "/api/v1/public/employer/get/all").retrieve().bodyToMono(new ParameterizedTypeReference<List<EmployerDTO>>() {} )
                .block();
        assert res2 != null;
        String creatorUsername2 = res2.getLast().getUsername();
        String randomCompany2 = goodDummyData.getRandomCompany(creatorUsername2);
        if(randomCompany2.equals(randomCompany)){randomCompany2 = goodDummyData.getRandomCompany(creatorUsername2);}

        requestHandler.registerCompany(employer2Token,randomCompany2, 201, port  );
        /** Employer tries to register an employee in a company that does not belong him */
        //Register Staff
        Map<Integer, String>  randomStaffData = goodDummyData.getRandomStaffRegisterData(res2.getLast().getId());
        int randomStaffNumber = randomStaffData.keySet().iterator().next();
        String randomStaff = randomStaffData.values().iterator().next();
        String randomStaffLoginData = goodDummyData.getStaffLoginData(randomStaffNumber);
        System.out.println("RANDOM EMPLOYEE: " + randomStaff);

        requestHandler.staffRegisterRequest(randomStaff, 400, employerToken, port);

    }

}
