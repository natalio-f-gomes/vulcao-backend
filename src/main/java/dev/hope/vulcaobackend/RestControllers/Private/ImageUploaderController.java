package dev.hope.vulcaobackend.RestControllers.Private;

import dev.hope.vulcaobackend.DTOs.ImageMetadata;
import dev.hope.vulcaobackend.DTOs.ImageUploadRequest;
import dev.hope.vulcaobackend.Models.Business.BusinessModel;
import dev.hope.vulcaobackend.Models.Offerings.OfferingModel;
import dev.hope.vulcaobackend.Models.UserModel;
import dev.hope.vulcaobackend.Properties.EntityType;
import dev.hope.vulcaobackend.Properties.UserType;
import dev.hope.vulcaobackend.Repositories.BusinessRepository;
import dev.hope.vulcaobackend.Repositories.OfferingRepository;
import dev.hope.vulcaobackend.Services.ExceptionHandler.ApiException;
import dev.hope.vulcaobackend.Services.GlobalServices;
import dev.hope.vulcaobackend.Services.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;


import java.util.List;
import java.util.Objects;

import static dev.hope.vulcaobackend.Services.ImageUploadService.MAX_IMAGES;


@RestController
@RequestMapping("/api/v2/image")
public class ImageUploaderController {

    @Autowired
    private S3Client s3Client;
    @Value("${aws.s3.bucket}")
    private String BUCKET_NAME;
    private final String READ_BUCKET = "vulcao-app-images";

    public static final long MAX_BYTES = 5L * 1024 * 1024; // 5 MB guard

    @Autowired
    private BusinessRepository businessRepository;
    @Autowired
    private GlobalServices globalServices;
    @Autowired
    private ImageUploadService imageUploadService;
    @Autowired
    private OfferingRepository offeringRepository;


    public void addImageKeys(EntityType entityType, String entityId, List<String> keys){
        System.out.println("entityId: " + entityId);
        System.out.println("entityType: " + entityType);
        if(entityType.equals(EntityType.BUSINESS)){
            BusinessModel business = businessRepository.findById(entityId).orElseThrow(()-> new ApiException(HttpStatus.NOT_FOUND, "Não existe nenhum negocio com este ID"));
            business.setImgS3UrlKeys(keys);
            businessRepository.update(business);
        }else{
            OfferingModel offering = offeringRepository.findById(entityId).orElseThrow(
                    ()-> new ApiException(HttpStatus.NOT_FOUND, "Não existe nenhum ofertas com este ID"));
            offering.setImgS3UrlKeys(keys);
            offeringRepository.update(offering);
        }

    }

    @PostMapping("/upload/confirm")
    public ResponseEntity<?> confirmImageUpload(@RequestBody ImageUploadRequest body) {
        System.out.println("UPLOAD CONFIRMATION ENDPOINT");
        System.out.println("Entity Id: "+ body.getEntityId());

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
                    throw new ApiException(HttpStatus.BAD_REQUEST, "Imagem demasiado grande, o tamanho máximo por imagem é 5MB");
                }

            } catch (NoSuchKeyException e) {
                System.out.println("GENUINE 404 — not in bucket: " +  BUCKET_NAME + "/" + key);
                return ResponseEntity.badRequest()
                        .body("O ficheiro solicitado não foi encontrado. Por favor, verifique se o caminho ou o nome estão corretos.");
            }
            catch (S3Exception e) {
                // 403 here = the s3Client's IAM user lacks s3:GetObject — NOT a missing object
                System.out.println("HEAD failed: status=" + e.statusCode()
                        + " code=" + e.awsErrorDetails().errorCode()
                        + " key=" + key);
                return ResponseEntity.internalServerError()
                        .body("Ocorreu um erro ao verificar a imagem. Por favor, tente novamente mais tarde ou contacte o suporte.");
            }
        }
        // now that we have the picture upload to the private bucket, lets upload it  to the public so anyone can read it
        for (String key : keys) {
            s3Client.copyObject(CopyObjectRequest.builder()
                    .sourceBucket(BUCKET_NAME).sourceKey(key)
                    .destinationBucket(READ_BUCKET).destinationKey(key)
                    .build());
        }
        System.out.println("Entity TYPE TYPE: " + body.getEntityType());

        addImageKeys(body.getEntityType(), body.getEntityId(), body.getS3PresignKeys());

        for (String key : keys) {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(BUCKET_NAME).key(key).build());
            System.out.println("KEY CLEANED UP: "+ key);
        }

        return ResponseEntity.ok("Saved " + keys.size() + " image(s).");
    }

    public List<String> validateImageUploadRequest(EntityType type, String entityId){
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        if(type.equals(EntityType.BUSINESS)){
            UserModel user = globalServices.getEmployerByUsername(sessionUser);
            BusinessModel business = businessRepository.findById(entityId).orElseThrow(
                    ()-> new ApiException(HttpStatus.NOT_FOUND, "NEGOCIO não encontrado com este ID: " + entityId)
            );
            if(!user.getId().equals(business.getCreatorId())){
                throw new ApiException(HttpStatus.FORBIDDEN, "Não tem autorização para atualizar este negocio.");
            }
            return business.getImgS3UrlKeys();
        }else{
            UserModel user = globalServices.validateAuthorization(sessionUser);
            OfferingModel offering = offeringRepository.findById(entityId).orElseThrow(
                    () -> new ApiException(HttpStatus.NOT_FOUND, "There is no offering with this id: "+ entityId)
            );
            BusinessModel business = businessRepository.findById(offering.getBusinessId()).orElseThrow(
                    ()-> new ApiException(HttpStatus.NOT_FOUND, "NEGOCIO não encontrado com este ID: " + offering.getBusinessId())
            );
            if(!user.getUserType().equals(UserType.EMPLOYER.toString()) || !business.getCreatorId().equals(user.getId())) {
                throw new ApiException(HttpStatus.FORBIDDEN, "Access denied since this offering does not belong to your business!");
            }
            return offering.getImgS3UrlKeys();
        }
    }

    @PutMapping("update/{entity}/{id}")
    public ResponseEntity<?> updateBusinessImage(@PathVariable EntityType entity, @PathVariable String id, @RequestBody List<ImageMetadata> imagesMetadata){
        if(imagesMetadata == null){
            throw new ApiException(HttpStatus.BAD_REQUEST, "NENHUMA IMAGEM ASSOCIADA A ESTE NEGOCIO"); }
        List<String> s3UrlKeys = validateImageUploadRequest(entity, id);
        for (String key : s3UrlKeys) {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(READ_BUCKET).key(key).build());
            System.out.println("KEY CLEANED UP: "+ key);
        }
        return imageUploadService.requestPresignedUrls(EntityType.BUSINESS, id, imagesMetadata);
    }

}
