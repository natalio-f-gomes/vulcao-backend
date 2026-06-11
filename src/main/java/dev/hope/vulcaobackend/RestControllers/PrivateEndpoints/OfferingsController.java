package dev.hope.vulcaobackend.RestControllers.PrivateEndpoints;

import dev.hope.vulcaobackend.DataTransferObjects.OfferingImageUploadRequest;
import dev.hope.vulcaobackend.Models.AccountEntities.EmployerModel;
import dev.hope.vulcaobackend.Models.Offerings.BookModel;
import dev.hope.vulcaobackend.Models.Offerings.MovieModel;
import dev.hope.vulcaobackend.Models.Offerings.MusicModel;
import dev.hope.vulcaobackend.Models.Offerings.ProductModel;
import dev.hope.vulcaobackend.Models.Properties.OfferingType;
import dev.hope.vulcaobackend.Repositories.EmployerRepository;
import dev.hope.vulcaobackend.Repositories.Offerings.ProductRepository;
import dev.hope.vulcaobackend.Services.ImageUploadRequestService;
import dev.hope.vulcaobackend.Services.Offerings.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static dev.hope.vulcaobackend.Services.ImageUploadRequestService.MAX_IMAGES;

@RequestMapping("/api/v1/offering")
@RestController
public class OfferingsController {

    @Autowired private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired private ImageUploadRequestService uploadRequestService;
    @Autowired
    private S3Client s3Client;
    @Value("${aws.s3.bucket}")
    private String BUCKET_NAME;
    private final String READ_BUCKET = "vulcao-app-images";



    public static final long MAX_BYTES = 5L * 1024 * 1024; // 5 MB guard
    @Autowired
    private EmployerRepository employerRepository;


    public String generateNewIdForOffering(OfferingType type) {
        String randomId = UUID.randomUUID().toString();
        switch (type){
            case PRODUCT -> {
                boolean doesProductIdExists =true;
                while (doesProductIdExists) {
                    doesProductIdExists = productRepository.findById(randomId).isPresent();
                    randomId = UUID.randomUUID().toString();
                }
            }
        }
        return randomId;
    }

    public void addProductImageKeys(String productId, List<String> keys){
        ProductModel product = productRepository.findById(productId).orElseThrow(()-> new RuntimeException("There is no such product with this ID"));
        product.setImgS3UrlKeys(keys);
        productRepository.update(product);

    }

    @PostMapping("/image/upload/confirm")
    public ResponseEntity<?> confirmImageUpload(@RequestBody OfferingImageUploadRequest body) {
        System.out.println("UPLOAD CONFIRMATION ENDPOINT");

        List<String> keys = body.getS3PresignKeys();
        if (keys == null || keys.isEmpty()) {
            return ResponseEntity.badRequest().body("No keys provided.");
        }
        if (keys.size() > MAX_IMAGES) {
            return ResponseEntity.badRequest().body("Too many keys.");
        }

        // ---- verify EVERY key exists in S3 before persisting ANY ----
        for (String key : keys) {
            try {
                HeadObjectResponse head = s3Client.headObject(
                        HeadObjectRequest.builder()
                                .bucket(BUCKET_NAME)
                                .key(key)
                                .build());


                if (head.contentLength() > MAX_BYTES) {
                    return ResponseEntity.badRequest()
                            .body("Image too large for key: " + key);
                }

            } catch (NoSuchKeyException e) {
                System.out.println("GENUINE 404 — not in bucket: " +  BUCKET_NAME + "/" + key);
                return ResponseEntity.badRequest()
                        .body("Upload not found for key: " + key);

            } catch (S3Exception e) {
                // 403 here = the s3Client's IAM user lacks s3:GetObject — NOT a missing object
                System.out.println("HEAD failed: status=" + e.statusCode()
                        + " code=" + e.awsErrorDetails().errorCode()
                        + " key=" + key);
                return ResponseEntity.internalServerError()
                        .body("Could not verify upload (status " + e.statusCode() + ")");
            }
        }
        // now that we have the picture upload to the private bucket, lets upload it  to the public so anyone can read it
        for (String key : keys) {
            s3Client.copyObject(CopyObjectRequest.builder()
                    .sourceBucket(BUCKET_NAME).sourceKey(key)
                    .destinationBucket(READ_BUCKET).destinationKey(key)
                    .build());
        }

        addProductImageKeys(body.getOfferingId(), keys);
        System.out.println("Confirmed " + keys.size() + " image(s) for offering "
                + body.getOfferingId());
        for (String key : keys) {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(BUCKET_NAME).key(key).build());
            System.out.println("KEY CLEANED UP: "+ key);
        }

        return ResponseEntity.ok("Saved " + keys.size() + " image(s).");
    }

    // Add PRODUCT
    @PostMapping("/product/employer/add")
    public ResponseEntity<?> employerAddProduct(@RequestBody ProductModel newProduct, HttpServletRequest request){
        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        String randomId = generateNewIdForOffering(OfferingType.PRODUCT);
        newProduct.setId(randomId);
        productService.employerSaveProduct(newProduct,userSession, request);
        return uploadRequestService.requestProductPresignedUrls(newProduct);
    }

    @PutMapping("/product/update/image")
    public ResponseEntity<?> employerUpdateProductImage(@RequestBody ProductModel newProduct){
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        EmployerModel employer = employerRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is no employer with this name"));
        if(newProduct.getImagesMetadata() == null){return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("NO IMAGES ASSOCIATED WITH THIS PRODUCT"); }
        for (String key : newProduct.getImgS3UrlKeys()) {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(READ_BUCKET).key(key).build());
            System.out.println("KEY CLEANED UP: "+ key);
        }
        return uploadRequestService.requestProductPresignedUrls(newProduct);


    }

    @PostMapping("/product/staff/add")
    public ResponseEntity<?> staffAddProduct(@RequestBody ProductModel newProduct, HttpServletRequest request){
        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        productService.staffSaveProduct(newProduct,userSession, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // UPDATE PRODUCT
    @PutMapping("/product/employer/update/{id}")
    public ResponseEntity<?> employerUpdateProduct(@PathVariable String id, @RequestBody ProductModel newProduct, HttpServletRequest request){
        System.out.println("EMPLOYER UPDATE PRODUCT: "+ newProduct.getName());
        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        productService.employerUpdateProduct(id, newProduct,userSession, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/product/staff/update/{id}")
    public ResponseEntity<?> staffUpdateProduct(@PathVariable String id, @RequestBody ProductModel newProduct, HttpServletRequest request){
        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        productService.staffUpdateProduct(id, newProduct,userSession, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // DELETE PRODUCT
    @DeleteMapping("/product/employer/delete/{id}")
    public ResponseEntity<?> employerDeleteProduct(@PathVariable String id, HttpServletRequest request){
        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        productService.employerDeleteProduct(id,userSession, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/product/staff/delete/{id}")
    public ResponseEntity<?> staffDeleteProduct(@PathVariable String id, HttpServletRequest request){
        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        productService.staffDeleteProduct(id,userSession, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
