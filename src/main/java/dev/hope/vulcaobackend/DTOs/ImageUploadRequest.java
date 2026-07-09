package dev.hope.vulcaobackend.DTOs;

import dev.hope.vulcaobackend.Properties.EntityType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
public class ImageUploadRequest {
    private String entityId;
    private List<String> s3PresignKeys;
    private EntityType entityType;
}
