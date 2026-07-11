package dev.hope.vulcaobackend.RestControllers.Public;

import dev.hope.vulcaobackend.DTOs.ProductDTO;
import dev.hope.vulcaobackend.Repositories.OfferingRepository;
import dev.hope.vulcaobackend.Services.Offerings.ProductService;
import dev.hope.vulcaobackend.Services.Offerings.WearableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/public/offering")
public class PublicOfferingController {
    @Autowired
    ProductService productService;
    @Autowired
    private OfferingRepository offeringRepository;
    @Autowired
    private WearableService wearableService;
    //TODO: ADD THE ALGORITHM TO FETCH ALL

    @GetMapping("/product/get/all")
    public ResponseEntity<?> findALlProducts() {
        System.out.println("findALlProducts");
       for(ProductDTO p: productService.findAll()){
           System.out.println(p.getType());
           System.out.printf(p.getName());
        }
        return ResponseEntity.ok(productService.findAll());
    }



    @GetMapping("/product/get/{id}")
    public ResponseEntity<?> findProductById(@PathVariable String id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @GetMapping("/business/{businessId}/get/product")
    public ResponseEntity<?> findProductsByBusinessId(@PathVariable String businessId) {
        return ResponseEntity.ok(productService.findByBusinessId(businessId));
    }


    @GetMapping("/product/get/creator/{id}")
    public ResponseEntity<?> findProductsByCreatorId(@PathVariable String id) {
        return ResponseEntity.ok(productService.findByCreatorId(id));
    }


    @GetMapping("/wearable/get/all")
    public ResponseEntity<?> findALlWearables() {
        return ResponseEntity.ok(wearableService.findAll());
    }


    @GetMapping("/wearable/get/{id}")
    public ResponseEntity<?> findWearableById(@PathVariable String id) {
        return ResponseEntity.ok(wearableService.findById(id));
    }

    @GetMapping("/business/{businessId}/get/wearable")
    public ResponseEntity<?> findWearableByBusinessId(@PathVariable String businessId) {
        return ResponseEntity.ok(wearableService.findByBusinessId(businessId));
    }


    @GetMapping("/wearable/get/creator/{id}")
    public ResponseEntity<?> findWearableByCreatorId(@PathVariable String id) {
        return ResponseEntity.ok(wearableService.findByCreatorId(id));
    }

}
