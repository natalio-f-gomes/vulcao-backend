package dev.hope.vulcaobackend.Models.Offerings;

import dev.hope.vulcaobackend.Models.Common.BaseOfferingModel;
import dev.hope.vulcaobackend.Models.Properties.DeviceResolution;
import dev.hope.vulcaobackend.Models.Properties.ElectronicDeviceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
public class ElectronicModel extends BaseOfferingModel {
    private String brand;
    private String modelName;
    private LocalDate releaseDate;
    private String color;
    private Double weight;
    private Integer height;
    private Integer length;
    private Integer depth;
    private Double screenSize;
    private DeviceResolution resolution;
    private Integer ram;
    private Integer storageCapacity;
    private Integer batteryCapacity;
    private String operatingSystem;
    private Integer slimSlots;
    private boolean eSimSupport;
    private ElectronicDeviceType electronicType;

    @DynamoDbSecondaryPartitionKey(indexNames = "brand-index")
    public String getBrand(){return this.brand;}

    @DynamoDbSecondaryPartitionKey(indexNames = "model-name-index")
    public String getModelName(){return this.modelName;}

    @DynamoDbSecondaryPartitionKey(indexNames = "screen-size-index")
    public Double getScreenSize(){return this.screenSize;}

    @DynamoDbSecondaryPartitionKey(indexNames = "ram-index")
    public Integer getRam(){return this.ram;}

    @DynamoDbSecondaryPartitionKey(indexNames = "storage-index")
    public Integer getStorageCapacity(){return this.storageCapacity;}

    //private ArrayList<ReviewModel> reviews = new ArrayList<>();
}
