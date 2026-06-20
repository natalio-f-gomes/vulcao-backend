package dev.hope.vulcaobackend.RestControllers.PrivateEndpoints.Offerings;

import dev.hope.vulcaobackend.DataTransferObjects.OfferingImageUploadRequest;
import dev.hope.vulcaobackend.Models.AccountEntities.EmployerModel;
import dev.hope.vulcaobackend.Models.AccountEntities.StaffModel;
import dev.hope.vulcaobackend.Models.Offerings.*;
import dev.hope.vulcaobackend.Models.Properties.OfferingType;
import dev.hope.vulcaobackend.Models.Properties.Permissions;
import dev.hope.vulcaobackend.Repositories.EmployerRepository;
import dev.hope.vulcaobackend.Repositories.Offerings.ProductRepository;
import dev.hope.vulcaobackend.Repositories.Offerings.WearableRepository;
import dev.hope.vulcaobackend.Repositories.StaffRepository;
import dev.hope.vulcaobackend.Services.ImageUploadRequestService;
import dev.hope.vulcaobackend.Services.Offerings.ProductService;
import dev.hope.vulcaobackend.Services.Offerings.WearableService;
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
public class OfferingController {

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
    @Autowired
    private WearableRepository wearableRepository;
    @Autowired
    private WearableService wearableService;
    @Autowired
    private StaffRepository staffRepository;


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
            case WEARABLE -> {
                boolean doesProductIdExists =true;
                while (doesProductIdExists) {
                    doesProductIdExists = wearableRepository.findById(randomId).isPresent();
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

    public void addWearableImageKeys(String itemId, List<String> keys){
        WearableModel item = wearableRepository.findById(itemId).orElseThrow(()-> new RuntimeException("There is no such product with this ID"));
        item.setImgS3UrlKeys(keys);
        wearableRepository.update(item);
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
        System.out.println("OFFERING TYPE TYPE: " + body.getOfferingType());

        if(body.getOfferingType().equals(OfferingType.PRODUCT)){
            addProductImageKeys(body.getOfferingId(), keys);
        }
        if(body.getOfferingType().equals(OfferingType.WEARABLE)){
            addWearableImageKeys(body.getOfferingId(), keys);
        }

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
        ResponseEntity<?> response =  productService.employerSaveProduct(newProduct,userSession, request);
        if(!response.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return response;
        }
        return uploadRequestService.requestProductPresignedUrls(newProduct);
    }

    @PutMapping("/product/update/image")
    public ResponseEntity<?> updateProductImage(@RequestBody ProductModel newProduct){
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        boolean employerExists = employerRepository.findByUsername(sessionUser).isPresent();
        boolean staffExists = staffRepository.findByUsername(sessionUser).isPresent();
        if(!employerExists && !staffExists){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if(staffExists){
            StaffModel staff = staffRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("Staff with username: " + sessionUser + " not found!"));
            if(!staff.getAssignedPermissions().contains(Permissions.UPDATE_WEARABLE)){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        if(employerExists){
            EmployerModel employer = employerRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("Employer with username: " + sessionUser + " not found!"));
        }

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
        String randomId = generateNewIdForOffering(OfferingType.PRODUCT);
        newProduct.setId(randomId);
        ResponseEntity<?> response =   productService.staffSaveProduct(newProduct,userSession, request);
        if(!response.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return response;
        }
        return uploadRequestService.requestProductPresignedUrls(newProduct);

    }

    // UPDATE PRODUCT
    @PutMapping("/product/employer/update/{id}")
    public ResponseEntity<?> employerUpdateProduct(@PathVariable String id, @RequestBody ProductModel newProduct, HttpServletRequest request){
        System.out.println("EMPLOYER UPDATE PRODUCT: "+ newProduct.getName());
        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        ResponseEntity<?> response = productService.employerUpdateProduct(id, newProduct,userSession, request);
        if(!response.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return response;
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/product/staff/update/{id}")
    public ResponseEntity<?> staffUpdateProduct(@PathVariable String id, @RequestBody ProductModel newProduct, HttpServletRequest request){
        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        ResponseEntity<?> response =  productService.staffUpdateProduct(id, newProduct,userSession, request);
        if(!response.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)){
            return response;
        }

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

    // Add Wearable
    @PostMapping("/wearable/employer/add")
    public ResponseEntity<?> employerAddWearable(@RequestBody WearableModel newItem, HttpServletRequest request){
        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        String randomId = generateNewIdForOffering(OfferingType.WEARABLE);
        newItem.setId(randomId);
        wearableService.employerSaveWearable(newItem,userSession);
        return uploadRequestService.requestWearablePresignedUrls(newItem);
    }

    // EMPLOYER UPDATE WEARABLE
    @PutMapping("/wearable/update/image")
    public ResponseEntity<?> employerUpdateWearableImage(@RequestBody WearableModel wearable){
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        boolean employerExists = employerRepository.findByUsername(sessionUser).isPresent();
        boolean staffExists = staffRepository.findByUsername(sessionUser).isPresent();
        if(!employerExists && !staffExists){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if(staffExists){
            StaffModel staff = staffRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("Staff with username: " + sessionUser + " not found!"));
            if(!staff.getAssignedPermissions().contains(Permissions.UPDATE_WEARABLE)){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        if(employerExists){
            EmployerModel employer = employerRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("Employer with username: " + sessionUser + " not found!"));
        }

        if(wearable.getImagesMetadata() == null){return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("NO IMAGES ASSOCIATED WITH THIS PRODUCT"); }
        for (String key : wearable.getImgS3UrlKeys()) {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(READ_BUCKET).key(key).build());
            System.out.println("KEY CLEANED UP: "+ key);
        }
        return uploadRequestService.requestWearablePresignedUrls(wearable);
    }

    @PutMapping("/wearable/employer/update/{id}")
    public ResponseEntity<?> employerUpdateWearable(@PathVariable String id, @RequestBody WearableModel newItem){
        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        wearableService.employerUpdateProduct(id, newItem,userSession);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @PostMapping("/wearable/staff/add")
    public ResponseEntity<?> staffAddWearable(@RequestBody WearableModel newItem){
        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        String randomId = generateNewIdForOffering(OfferingType.PRODUCT);
        newItem.setId(randomId);
        wearableService.staffSaveWearable(newItem,userSession);
        return uploadRequestService.requestWearablePresignedUrls(newItem);
    }

    @PutMapping("/wearable/staff/update/{id}")
    public ResponseEntity<?> staffUpdateWearable(@PathVariable String id, @RequestBody WearableModel newItem){
        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        wearableService.staffUpdateWearable(id, newItem,userSession);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // DELETE PRODUCT
    @DeleteMapping("/wearable/employer/delete/{id}")
    public ResponseEntity<?> employerDeleteWearable(@PathVariable String id){
        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        wearableService.employerDeleteProduct(id,userSession);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/wearable/staff/delete/{id}")
    public ResponseEntity<?> staffDeleteWearable(@PathVariable String id){
        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        wearableService.staffDeleteProduct(id,userSession);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
