package dev.hope.vulcaobackend.RestControllers.Public;

import dev.hope.vulcaobackend.Repositories.OfferingRepository;
import dev.hope.vulcaobackend.Services.Offerings.ProductService;
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
     //TODO: ADD THE ALGORITHM TO FETCH ALL

    @GetMapping("/product/get/all")
    public ResponseEntity<?> findALlProducts() {
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

}
