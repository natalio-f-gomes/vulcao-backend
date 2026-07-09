package dev.hope.vulcaobackend.Services;

import dev.hope.vulcaobackend.DTOs.ImageMetadata;
import dev.hope.vulcaobackend.Properties.EntityType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ImageUploadService {
    private final S3Presigner presigner;
    public static final int MAX_IMAGES = 5;

    @Value("${aws.s3.bucket}")
    private String UPLOAD_BUCKET;

    private String READ_BUCKET; // this is for everyone to access the image.

    public ImageUploadService(S3Presigner presigner){
        this.presigner = presigner;
    }

    public PresignResult generateUrlAndSignTheKey(String key,String contentType){
        // 1. The backend OWNS the key — the client does sot choose this
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(UPLOAD_BUCKET)
                .key(key)
                .contentType(contentType)
                .build();
        // 3. How long the URL stays valid.
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5))
                .putObjectRequest(objectRequest)
                .build();

        // 4. Sign it.
        PresignedPutObjectRequest presigned = presigner.presignPutObject(presignRequest);
        return new PresignResult(presigned.url().toString(), key);
    }


    public PresignResult createPresignedURL(EntityType entityType,  String id, String contentType) {
        // 1. The backend OWNS the key — never let the client choose it.
        String key = entityType + "/" + id + "/" + UUID.randomUUID();

        return generateUrlAndSignTheKey(key, contentType);
    }

    // THIS IS THE ENDPOINT THAT WILL BE CALLED
    public ResponseEntity<?> requestPresignedUrls( EntityType type,  String id, List<ImageMetadata> images) {
        System.out.println("Request image url endpoint: ");
        for(ImageMetadata meta: images){
            System.out.println("Metadata Name: " + meta.getName());
            System.out.println("Metadata Size: " + meta.getByteSize());
            System.out.println("Metadata Type: " + meta.getExtension());
        }
        // ---- enforce the limit server-side ----
        if (images.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        if (images.size() > MAX_IMAGES) {
            return ResponseEntity.badRequest()
                    .body("Too many images. Maximum allowed is " + MAX_IMAGES + ".");
        }
        // ---- one presigned PUT per image ----
        List<PresignResult> results = new ArrayList<>();
        for (ImageMetadata meta : images) {
            PresignResult presignUrl = createPresignedURL(type, id,  meta.getExtension());
            System.out.println("Presign URL: "+ presignUrl.url() + " Presign Key" + presignUrl.key());
            results.add(presignUrl);
        }
        return ResponseEntity.ok(Map.of(
                "entityId",  id,
                "entityTye", type,
                "uploadResult", results
        ));
    }

    public record PresignResult(String url, String key) {}



}
