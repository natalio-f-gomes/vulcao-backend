package dev.hope.vulcaobackend.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
public class BaseModel {
    protected String createdAt;
    protected String updatedAt;
    protected String id;

    public void onUpdate() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Atlantic/Cape_Verde"));
        this.updatedAt = now.toString();
    }

    public void onCreate() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Atlantic/Cape_Verde"));
        this.updatedAt = now.toString();
        this.createdAt = now.toString();
    }

}
