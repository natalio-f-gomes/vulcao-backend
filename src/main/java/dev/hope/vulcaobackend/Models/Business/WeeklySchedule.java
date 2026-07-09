package dev.hope.vulcaobackend.Models.Business;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@DynamoDbBean
@NoArgsConstructor
public class WeeklySchedule {
    private Map<String, TimeRange> daySchedule = new HashMap<>(); // the key must not be enum otherwise it won't compile
    private Boolean open247;
}