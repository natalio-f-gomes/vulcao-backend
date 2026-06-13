package dev.hope.vulcaobackend.Configuration.SSMParameterStore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class ParameterStoreService {
    private final SsmClient ssmClient;


    public ParameterStoreService(AwsCredentialsProvider credentialsProvider) {
        this.ssmClient = SsmClient.builder()
                .credentialsProvider(credentialsProvider)
                .region(Region.EU_SOUTH_2) // Replace with your region
                .build();
    }

    @Cacheable("parameters")
    public String getParameter(String parameterName) {
        try {
            GetParameterRequest request = GetParameterRequest.builder()
                    .name(parameterName)
                    .withDecryption(true) // Important for SecureString parameters
                    .build();

            GetParameterResponse response = ssmClient.getParameter(request);
            return response.parameter().value();

        } catch (SsmException e) {
            System.err.println("Error retrieving parameter: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve parameter: " + parameterName, e);
        }
    }

    // Get multiple parameters at once (more efficient)
    public Map<String, String> getParameters(List<String> parameterNames) {
        try {
            GetParametersRequest request = GetParametersRequest.builder()
                    .names(parameterNames)
                    .withDecryption(true)
                    .build();

            GetParametersResponse response = ssmClient.getParameters(request);
            Map<String, String> parameters = new HashMap<>();

            response.parameters().forEach(param ->
                    parameters.put(param.name(), param.value())
            );

            return parameters;

        } catch (SsmException e) {
            System.err.println("Error retrieving parameters: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve parameters", e);
        }
    }

    public void close() {
        ssmClient.close();
    }


}
