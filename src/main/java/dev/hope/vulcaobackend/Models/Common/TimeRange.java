package dev.hope.vulcaobackend.Models.Common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.time.LocalTime;


@DynamoDbBean
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeRange {
    private LocalTime start;
    private LocalTime end;
    private Boolean isClosed;
}
