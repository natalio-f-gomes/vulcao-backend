package dev.hope.vulcaobackend.Configuration;

import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.*;

@Configuration
public class AWSSESConfig {

    SesV2Client ses;


    public AWSSESConfig(AwsCredentialsProvider credentialsProvider) {
        this.ses = SesV2Client.builder()
                .credentialsProvider(credentialsProvider)
                .region(Region.US_EAST_1) // SES does not work in EU_SOUTH_2
                .build();
    }


    public void sendCode(String receiver, String code) {
        System.out.println("Receiver: " + receiver);
        System.out.println("Code: " + code);
        ses.sendEmail(SendEmailRequest.builder()
                .fromEmailAddress("verification@vulcao.net")
                .destination(Destination.builder().toAddresses(receiver).build())
                .content(EmailContent.builder().simple(Message.builder()
                        .subject(Content.builder().data("O seu código de verificação").build())
                        .body(Body.builder().text(Content.builder()
                                .data("O seu código é " + code + ". Expira em 15 minutos. Nunca partilhe este código com ninguém. Este código serve para proteger a sua conta e impedir que terceiros ajam em seu nome.").build()).build())
                        .build()).build())
                .build());
    }

    //TODOS CUSTOMIZE THE NOTIFICATION
    public void sendNotification(String sender, String receiver, String code) {
        System.out.println("Receiver: " + sender);
        System.out.println("Receiver: " + receiver);
        System.out.println("Code: " + code);
        ses.sendEmail(SendEmailRequest.builder()
                .fromEmailAddress("notification@vulcao.net")
                .destination(Destination.builder().toAddresses(receiver).build())
                .content(EmailContent.builder().simple(Message.builder()
                        .subject(Content.builder().data("Notification from " + sender).build())
                        .body(Body.builder().text(Content.builder()
                                .data("Your code is " + code + ". Expires in 10 minutes.").build()).build())
                        .build()).build())
                .build());
    }




}
