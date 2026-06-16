package dev.hope.vulcaobackend.Services;

import dev.hope.vulcaobackend.DataTransferObjects.ImageMetadata;
import dev.hope.vulcaobackend.Models.CompanyModel;
import dev.hope.vulcaobackend.Models.Offerings.ProductModel;
import dev.hope.vulcaobackend.Models.Offerings.WearableModel;
import dev.hope.vulcaobackend.Models.Properties.OfferingType;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
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
public class ImageUploadRequestService {

    private final S3Presigner presigner;
    public static final int MAX_IMAGES = 5;

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String UPLOAD_BUCKET;

    private String READ_BUCKET; // this is for everyone to access the image.


    public ImageUploadRequestService(S3Presigner presigner, S3Client s3Client){
        this.presigner = presigner;
        this.s3Client = s3Client;
    }

    public String generateOfferingKey(OfferingType offeringType, String itemId){
        return switch (offeringType) {
            case PRODUCT -> "offerings/products/" + itemId + "/" + UUID.randomUUID();
            case MOVIE -> "offerings/movies/" + itemId + "/" +  UUID.randomUUID();
            case TV_SHOW -> "offerings/tv_shows/" + itemId + "/" +  UUID.randomUUID();
            case ELECTRONIC -> "offerings/electronics/" + itemId + "/" + UUID.randomUUID();
            case BOOK -> "offerings/books/" + itemId + "/"+   UUID.randomUUID();
            case WEARABLE -> "offerings/wearables/" + itemId + "/"+   UUID.randomUUID();
            default -> null;
        };
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

    public PresignResult createCompanyPresignedURL(CompanyModel company, String contentType) {
        // 1. The backend OWNS the key — never let the client choose it.
        String key = "company/" + company.getId() + "/" + UUID.randomUUID();

       return generateUrlAndSignTheKey(key, contentType);
    }

    public PresignResult createOfferingPresignedURL(OfferingType offeringType, String itemId, String contentType) {
        // 1. The backend OWNS the key — never let the client choose it.
        String key = generateOfferingKey(offeringType, itemId);
        if (key == null) {
            throw new RuntimeException("BAD REQUEST, THERE IS NO SUCH TYPE OF OFFERING");
        }
        return generateUrlAndSignTheKey(key, contentType);
    }

    // THIS IS THE ENDPOINT THAT WILL BE CALLED FOR PRODUCT
    public ResponseEntity<?> requestProductPresignedUrls(ProductModel product) {
        System.out.println("Request image url endpoint: ");
        List<ImageMetadata> images = product.getImagesMetadata();
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
            PresignResult presignUrl = createOfferingPresignedURL(product.getOfferingType(), product.getId(),  meta.getExtension());
            System.out.println("Presign URL: "+ presignUrl.url() + " Presign Key" + presignUrl.key());
            results.add(presignUrl);
        }
        return ResponseEntity.ok(Map.of(
                "offeringId",  product.getId(),
                "uploadResult", results
        ));
    }

    // THIS IS THE ENDPOINT THAT WILL BE CALLED
    public ResponseEntity<?> requestCompanyPresignedUrlsFor(CompanyModel company) {
        System.out.println("Request image url endpoint: ");
        List<ImageMetadata> images = company.getImagesMetadata();
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
            PresignResult presignUrl = createCompanyPresignedURL(company,  meta.getExtension());
            System.out.println("Presign URL: "+ presignUrl.url() + " Presign Key" + presignUrl.key());
            results.add(presignUrl);
        }
        return ResponseEntity.ok(Map.of(
                "companyId",  company.getId(),
                "uploadResult", results
        ));
    }
    // THIS IS THE ENDPOINT THAT WILL BE CALLED FOR PRODUCT
    public ResponseEntity<?> requestWearablePresignedUrls(WearableModel item) {
        System.out.println("Request image url endpoint: ");
        List<ImageMetadata> images = item.getImagesMetadata();
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
            PresignResult presignUrl = createOfferingPresignedURL(item.getOfferingType(), item.getId(),  meta.getExtension());
            System.out.println("Presign URL: "+ presignUrl.url() + " Presign Key" + presignUrl.key());
            results.add(presignUrl);
        }
        return ResponseEntity.ok(Map.of(
                "offeringId",  item.getId(),
                "uploadResult", results
        ));
    }


    public record PresignResult(String url, String key) {}
}
