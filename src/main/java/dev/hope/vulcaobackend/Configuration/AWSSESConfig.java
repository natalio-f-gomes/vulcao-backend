package dev.hope.vulcaobackend.Configuration;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.*;

import java.net.URI;

//TODO  wire the other repos
@Configuration
public class AWSSESConfig {


    SesV2Client ses;


    public AWSSESConfig(AwsCredentialsProvider credentialsProvider, @Value("${ses.endpoint:}") String endpoint)
                         {
        System.out.println("SES ENDPOINT: " + endpoint);
        var builder = SesV2Client.builder()
                .credentialsProvider(credentialsProvider)
                .region(Region.US_EAST_1);
        if (!endpoint.isBlank()) {
            builder.endpointOverride(URI.create(endpoint)); // tests only
        }
        this.ses = builder.build();

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
    public void sendOrderNotification(String receiver, String htmlBody, String subject) {
        System.out.println("Receiver: " + receiver);

        ses.sendEmail(SendEmailRequest.builder()
                .fromEmailAddress("notification@vulcao.net")
                .destination(Destination.builder().toAddresses(receiver).build())
                .content(EmailContent.builder().simple(Message.builder()
                        .subject(Content.builder().data(subject).build())
                        .body(Body.builder().text(Content.builder()
                                .data(htmlBody).build()).build())
                        .build()).build())
                .build());
    }


}
