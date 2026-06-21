package dev.hope.vulcaobackend.Models.Common;

import dev.hope.vulcaobackend.Models.Properties.CvIslands;
import dev.hope.vulcaobackend.Models.Properties.WeekDays;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
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

