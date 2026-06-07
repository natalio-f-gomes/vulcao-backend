package dev.hope.vulcaobackend.RestControllers.PrivateEndpoints;


import dev.hope.vulcaobackend.DataTransferObjects.*;
import dev.hope.vulcaobackend.Models.AccountEntities.EmployerModel;
import dev.hope.vulcaobackend.Models.Common.Address;
import dev.hope.vulcaobackend.Models.CompanyModel;
import dev.hope.vulcaobackend.Models.Offerings.ProductModel;
import dev.hope.vulcaobackend.Repositories.CompanyRepository;
import dev.hope.vulcaobackend.Repositories.EmployerRepository;
import dev.hope.vulcaobackend.Services.CompanyService;
import dev.hope.vulcaobackend.Services.ImageUploadRequestService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static dev.hope.vulcaobackend.RestControllers.PrivateEndpoints.OfferingsController.MAX_BYTES;
import static dev.hope.vulcaobackend.Services.ImageUploadRequestService.MAX_IMAGES;

@RestController
@RequestMapping("/api/v1/company")
public class CompanyController {

    @Autowired private CompanyService companyService;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private ImageUploadRequestService imageUploadRequestService;
    @Autowired
    private S3Client s3Client;
    @Value("${aws.s3.bucket}")
    private String BUCKET_NAME;
    private final String READ_BUCKET = "vulcao-app-images";
    @Autowired
    private EmployerRepository employerRepository;

    public String generateNewIdForCompany() {
        boolean doesUserIdExists = true;
        String randomId = UUID.randomUUID().toString();
        while (doesUserIdExists) {
            doesUserIdExists = companyRepository.findById(randomId).isPresent();
            randomId = UUID.randomUUID().toString();
        }
        return randomId;
    }


    public void addCompanyImageKeys(String companyId, List<String> keys){
        CompanyModel company = companyRepository.findById(companyId).orElseThrow(()-> new RuntimeException("There is no such company with this ID"));
        company.setImgS3UrlKeys(keys);
        companyRepository.update(company);

    }

    @PostMapping("/image/upload/confirm")
    public ResponseEntity<?> confirmImageUpload(@RequestBody CompanyImageUploadRequest body) {
        System.out.println("COMPANY UPLOAD CONFIRMATION ENDPOINT");
        System.out.println("COMPANY ID; " + body.getCompanyId());

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

        addCompanyImageKeys(body.getCompanyId(), keys);
        System.out.println("Confirmed " + keys.size() + " image(s) for offering "
                + body.getCompanyId());

        for (String key : keys) {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(BUCKET_NAME).key(key).build());
            System.out.println("KEY CLEANED UP: "+ key);
        }

        return ResponseEntity.ok("Saved " + keys.size() + " image(s).");
    }


    @PutMapping("/update/image")
    public ResponseEntity<?> updateCompanyImage(@RequestBody CompanyModel company){
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        EmployerModel employer = employerRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is no employer with this name"));
        if(company.getImagesMetadata() == null){return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("NO IMAGES ASSOCIATED WITH THIS PRODUCT"); }
        for (String key : company.getImgS3UrlKeys()) {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(READ_BUCKET).key(key).build());
            System.out.println("KEY CLEANED UP: "+ key);
        }
        return imageUploadRequestService.requestCompanyPresignedUrlsFor(company);
    }



    @PostMapping("/register")
    public ResponseEntity<?> registerBusiness(@Valid @RequestBody CompanyModel newBusiness, HttpServletRequest request){
        try{
            String id = generateNewIdForCompany();
            newBusiness.setId(id);
            companyService.registerBusiness(newBusiness,request);
            imageUploadRequestService.requestCompanyPresignedUrlsFor(newBusiness);
            return imageUploadRequestService.requestCompanyPresignedUrlsFor(newBusiness);
        }catch (AccessDeniedException e){
            System.out.println("ERROR: " + e.getClass().getName() + " - " + e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }catch (ResponseStatusException e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBusiness(@RequestBody CompanyDTO updatedBusiness, @PathVariable String id){
        companyService.updateCompany(id, updatedBusiness);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/update/address/{id}")
    public ResponseEntity<?> updateBusinessAddress(@RequestBody Address newAddress, @PathVariable String id){
        companyService.updateCompanyAddress(id, newAddress);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/update/description/{id}")
    public ResponseEntity<?> updateBusinessDescription(@RequestBody CompanyAboutInfo info, @PathVariable String id){
        companyService.updateCompanyAboutInfo(id, info);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/update/contact/{id}")
    public ResponseEntity<?> updateBusinessContact(@RequestBody CompanyContact newContact, @PathVariable String id){
        companyService.updateCompanyContact(id, newContact);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
