package dev.hope.vulcaobackend.Configuration;

import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.*;
import software.amazon.awssdk.services.ssm.SsmClient;

@Configuration
public class AWSSESConfig {

    SesV2Client ses;


    public AWSSESConfig(AwsCredentialsProvider credentialsProvider) {
        this.ses = SesV2Client.builder()
                .credentialsProvider(credentialsProvider)
                .region(Region.US_EAST_1) // Replace with your region
                .build();
    }


    public void sendCode(String receiver, String code) {
        System.out.println("Receiver: " + receiver);
        System.out.println("Code: " + code);
        ses.sendEmail(SendEmailRequest.builder()
                .fromEmailAddress("verification@vulcao.net")
                .destination(Destination.builder().toAddresses(receiver).build())
                .content(EmailContent.builder().simple(Message.builder()
                        .subject(Content.builder().data("Your verification code").build())
                        .body(Body.builder().text(Content.builder()
                                .data("Your code is " + code + ". Expires in 10 minutes.").build()).build())
                        .build()).build())
                .build());
    }




}
