package dev.hope.vulcaobackend.Configuration.SSMParameterStore;

import lombok.Getter;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;

import java.util.List;
import java.util.Map;

@Component
@Getter
public class GetCredentialsConfig {
    private final ParameterStoreService parameterStore;
    private final AwsCredentialsProvider localCredentials;
    private String jwtSecret;

    public GetCredentialsConfig(AwsCredentialsProvider localCredentials) {
        this.parameterStore = new ParameterStoreService(localCredentials);
        loadCredentials();
        this.localCredentials = localCredentials;
    }

    private void loadCredentials() {
        List<String> parameterNames = List.of(
               "/vulcao/jwt/secret"
        );

        Map<String, String> parameters = parameterStore.getParameters(parameterNames);

        this.jwtSecret = parameters.get(parameterNames.getFirst());
    }


}
