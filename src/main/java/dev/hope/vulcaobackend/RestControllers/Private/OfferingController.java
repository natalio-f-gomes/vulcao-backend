package dev.hope.vulcaobackend.RestControllers.Private;

import dev.hope.vulcaobackend.DTOs.ProductDTO;
import dev.hope.vulcaobackend.DTOs.WearableDTO;
import dev.hope.vulcaobackend.Properties.EntityType;
import dev.hope.vulcaobackend.Services.ImageUploadService;
import dev.hope.vulcaobackend.Services.Offerings.OfferingService;
import dev.hope.vulcaobackend.Services.Offerings.ProductService;
import dev.hope.vulcaobackend.Services.Offerings.WearableService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/offering")
public class OfferingController {
    @Autowired
    ImageUploadService imageUploadService;
    @Autowired
    ProductService productService;
    @Autowired
    private OfferingService offeringService;
    @Autowired
    private WearableService wearableService;

    @PostMapping("/add/product")
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductDTO newProduct) {
        System.out.println("addProduct endpoint reached");
        String id = productService.save(newProduct);
        return imageUploadService.requestPresignedUrls(EntityType.PRODUCT, id,  newProduct.getImagesMetadata());
    }

    @PutMapping("/update/product/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable String id, @Valid @RequestBody ProductDTO newProduct) {
        System.out.println("addProduct endpoint reached");
        productService.update(id, newProduct);
        return ResponseEntity.ok(newProduct);
    }

    @DeleteMapping("/delete/product/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        System.out.println("addProduct endpoint reached");
        offeringService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add/wearable")
    public ResponseEntity<?> addWearable(@Valid @RequestBody WearableDTO newWearable) {
        System.out.println("addProduct endpoint reached");
        String id = wearableService.save(newWearable);
        return imageUploadService.requestPresignedUrls(EntityType.WEARABLE, id,  newWearable.getImagesMetadata());
    }

    @PutMapping("/update/wearable/{id}")
    public ResponseEntity<?> updateWearable(@PathVariable String id, @Valid @RequestBody WearableDTO newWearable) {
        System.out.println("addProduct endpoint reached");
        wearableService.update(id, newWearable);
        return ResponseEntity.ok(newWearable);
    }

    @DeleteMapping("/delete/wearable/{id}")
    public ResponseEntity<?> deleteWearable(@PathVariable String id) {
        System.out.println("addProduct endpoint reached");
        offeringService.delete(id);
        return ResponseEntity.ok().build();
    }



}
