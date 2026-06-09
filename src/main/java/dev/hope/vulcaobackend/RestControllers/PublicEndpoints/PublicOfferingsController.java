package dev.hope.vulcaobackend.RestControllers.PublicEndpoints;


import dev.hope.vulcaobackend.DataTransferObjects.Offerings.BookDTO;
import dev.hope.vulcaobackend.DataTransferObjects.Offerings.MovieDTO;
import dev.hope.vulcaobackend.DataTransferObjects.Offerings.MusicDTO;
import dev.hope.vulcaobackend.DataTransferObjects.Offerings.ProductDTO;
import dev.hope.vulcaobackend.Services.Offerings.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/public/offering")
@RestController
public class PublicOfferingsController {

    @Autowired private ProductService productService;

    // ========== PRODUCT ENDPOINTS ==========
    @GetMapping("/product/get/all")
    public List<ProductDTO> getAllProducts() {
        return productService.findAll();
    }

    @GetMapping("/get/product/company/{companyId}")
    public ResponseEntity<List<ProductDTO>> getProductsByCompanyId(@PathVariable String companyId) {
        System.out.println("Get products from company: "+ companyId+ " requested");
        return ResponseEntity.ok(productService.findProductsByCompanyId(companyId));
    }

    @GetMapping("/product/company/name/{companyName}")
    public ResponseEntity<List<ProductDTO>> getProductsByCompanyName(@PathVariable String companyName) {
        return ResponseEntity.ok(productService.findProductsByCompanyName(companyName));
    }

    @GetMapping("/product/creatorId/{creatorId}")
    public ResponseEntity<List<ProductDTO>> getProductsByCreatorId(@PathVariable String creatorId) {
        return ResponseEntity.ok(productService.findCreatorProductsByCreatorId(creatorId));
    }

    @GetMapping("/product/creator/username/{username}")
    public ResponseEntity<List<ProductDTO>> getProductsByCreatorUsername(@PathVariable String username) {
        return ResponseEntity.ok(productService.findProductsByCreatorName(username));
    }

    @GetMapping("/product/get/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable String id) {
        return ResponseEntity.ok(productService.findProductsById(id));
    }

    @GetMapping("/product/search/name")
    public ResponseEntity<List<ProductDTO>> getProductsByName(@RequestParam String name) {
        return ResponseEntity.ok(productService.findProductsByName(name));
    }

    @GetMapping("/product/search/price")
    public ResponseEntity<List<ProductDTO>> getProductsByPrice(@RequestParam Double price) {
        return ResponseEntity.ok(productService.findProductsByPrice(price));
    }


}