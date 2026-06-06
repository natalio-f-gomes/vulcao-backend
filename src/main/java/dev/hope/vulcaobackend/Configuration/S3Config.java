package dev.hope.vulcaobackend.Configuration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {

    @Value("${aws.s3.region}")
    private String region;

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

    @Bean
    public S3Presigner s3Presigner(AwsCredentialsProvider creds) {
        return S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(creds)
                .build();
    }

    @Bean
    public S3Client s3Client(AwsCredentialsProvider creds) {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(creds)
                .build();
    }
}
