package dev.hope.vulcaobackend.DataTransferObjects;


import dev.hope.vulcaobackend.DataTransferObjects.Common.BaseModelDTO;
import dev.hope.vulcaobackend.Models.Common.Address;
import dev.hope.vulcaobackend.Models.Properties.BusinessCategory;
import dev.hope.vulcaobackend.Models.Properties.CompanyStatuses;
import dev.hope.vulcaobackend.Models.Shopping.OrderModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean // since I am creating a list of companies Registered dto in employer model, I need this annotation
public class CompanyDTO extends BaseModelDTO {
    private String name;
    private BusinessCategory category;
    private String taxIdNumber;
    private String description;
    private String phoneNumber;
    private String faxNumber;
    private Double revenue;
    private String email;
    private Address address;
    private CompanyStatuses status;
    private String creatorUsername;
    private List<String> imgS3UrlKeys;
    private List<OrderModel> orderList;

}
