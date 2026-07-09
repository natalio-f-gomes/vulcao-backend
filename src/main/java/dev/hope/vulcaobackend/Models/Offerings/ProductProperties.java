package dev.hope.vulcaobackend.Models.Offerings;

import dev.hope.vulcaobackend.Properties.ProductType;
import jakarta.validation.constraints.Future;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.time.LocalDate;

@DynamoDbBean
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductProperties {
    @Future
    private LocalDate expirationDate;
    private ProductType productType;
}
