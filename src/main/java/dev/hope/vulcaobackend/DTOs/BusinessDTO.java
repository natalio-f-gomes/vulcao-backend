package dev.hope.vulcaobackend.DTOs;

import dev.hope.vulcaobackend.Models.BaseModel;
import dev.hope.vulcaobackend.Models.Business.WeeklySchedule;
import dev.hope.vulcaobackend.Properties.Address;
import dev.hope.vulcaobackend.Properties.BusinessStatus;
import dev.hope.vulcaobackend.Properties.BusinessType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
public class BusinessDTO extends BaseModel {
    private String name;
    private BusinessType type;
    private String description;
    private String phoneNumber;
    private Double revenue;
    private String email;
    private List<String> imgS3UrlKeys = new ArrayList<>();
    private List<ImageMetadata> imagesMetadata = new ArrayList<>();
    private Address address;
    private BusinessStatus status;
    private WeeklySchedule weeklySchedule;
    private String creatorId;
}
