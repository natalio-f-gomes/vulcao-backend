package dev.hope.vulcaobackend.Configuration;

import dev.hope.vulcaobackend.Models.AccountEntities.UserModel;
import dev.hope.vulcaobackend.Models.Common.Address;
import dev.hope.vulcaobackend.Models.CompanyModel;
import dev.hope.vulcaobackend.Models.Offerings.ProductModel;
import dev.hope.vulcaobackend.Models.Offerings.WearableModel;
import dev.hope.vulcaobackend.Models.Properties.OfferingType;
import dev.hope.vulcaobackend.Models.Properties.OrderStatus;
import dev.hope.vulcaobackend.Models.Shopping.OrderItemModel;
import dev.hope.vulcaobackend.Models.Shopping.OrderModel;
import dev.hope.vulcaobackend.Repositories.CompanyRepository;
import dev.hope.vulcaobackend.Repositories.Offerings.ProductRepository;
import dev.hope.vulcaobackend.Repositories.Offerings.WearableRepository;
import dev.hope.vulcaobackend.Repositories.UserRepository;
import dev.hope.vulcaobackend.Services.Offerings.WearableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.mapper.Order;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class AWSSESConfig {

    @Autowired CompanyRepository companyRepository;
    @Autowired ProductRepository productRepository;
    @Autowired  WearableRepository wearableRepository;
    @Autowired  UserRepository userRepository;
    SesV2Client ses;
    private WearableService wearableService;


    public AWSSESConfig(AwsCredentialsProvider credentialsProvider, @Value("${ses.endpoint:}") String endpoint,
                        CompanyRepository companyRepository,
                        ProductRepository productRepository,
                        WearableRepository wearableRepository,
                        UserRepository userRepository,
                        WearableService wearableService) {
        System.out.println("SES ENDPOINT: " + endpoint);
        var builder = SesV2Client.builder()
                .credentialsProvider(credentialsProvider)
                .region(Region.US_EAST_1);
        if(!endpoint.isBlank()){
            builder.endpointOverride(URI.create(endpoint)); // tests only
        }
        this.ses = builder.build();
        this.companyRepository = companyRepository;
        this.productRepository = productRepository;
        this.wearableRepository = wearableRepository;
        this.userRepository = userRepository;
        this.wearableService = wearableService;
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
