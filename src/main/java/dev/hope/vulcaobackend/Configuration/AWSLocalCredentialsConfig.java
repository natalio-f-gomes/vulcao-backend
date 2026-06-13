package dev.hope.vulcaobackend.Configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

@Configuration
public class AWSLocalCredentialsConfig {
    /* ---------- LOCAL: static keys from secrets.properties ---------- */
    @Profile("local")
    @Bean
    public AwsCredentialsProvider localCredentials(
            @Value("${aws.s3.access-key}") String accessKey,
            @Value("${aws.s3.secret-key}") String secretKey) {
        return StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey));
    }

    /* ---------- PROD: instance profile via IMDS — no keys anywhere ---------- */
    @Profile("!local")
    @Bean
    public AwsCredentialsProvider prodCredentials() {
        return DefaultCredentialsProvider.builder().build();
    }

}
