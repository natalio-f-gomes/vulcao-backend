package dev.hope.vulcaobackend.Models.Offerings;


import dev.hope.vulcaobackend.DataTransferObjects.ImageMetadata;
import dev.hope.vulcaobackend.Models.Common.BaseOfferingModel;
import dev.hope.vulcaobackend.Models.Properties.ProductType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
public class ProductModel extends BaseOfferingModel {
    @FutureOrPresent
    private LocalDate expirationDate;
    private ProductType productType;



    public String getSummarization() {
        return "Product {" +
                "id='" + id + '\'' +
                "CreatedAt='" + createdAt + '\'' +
                "CreatedAt='" + updatedAt + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", pricePerUnit=" + pricePerUnit +
                ", unitType=" + unitType +
                ", currency=" + currency +
                ", stock=" + stock +
                ", creatorUsername='" + creatorUsername + '\'' +
                ", creatorId='" + creatorId + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", companyId='" + companyId + '\'' +
                ", companyName='" + companyName + '\'' +
                '}';
    }


    //private ArrayList<ReviewModel> reviews = new ArrayList<>();

}
