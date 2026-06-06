package dev.hope.vulcaobackend.Models.Properties;

import dev.hope.vulcaobackend.Models.Common.BaseModelEntities;
import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@DynamoDbBean
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleModel extends BaseModelEntities {
    private String companyId;

}
