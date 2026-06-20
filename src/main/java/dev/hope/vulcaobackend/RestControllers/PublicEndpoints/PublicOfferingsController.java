package dev.hope.vulcaobackend.RestControllers.PublicEndpoints;


import dev.hope.vulcaobackend.DataTransferObjects.Offerings.BookDTO;
import dev.hope.vulcaobackend.DataTransferObjects.Offerings.MovieDTO;
import dev.hope.vulcaobackend.DataTransferObjects.Offerings.MusicDTO;
import dev.hope.vulcaobackend.DataTransferObjects.Offerings.ProductDTO;
import dev.hope.vulcaobackend.Models.Offerings.WearableModel;
import dev.hope.vulcaobackend.Services.Offerings.ProductService;
import dev.hope.vulcaobackend.Services.Offerings.WearableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/public/offering")
@RestController
public class PublicOfferingsController {

    @Autowired private ProductService productService;
    @Autowired
    private WearableService wearableService;

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


    // ========== WEARABLE PUBLIC ENDPOINTS ==========
    @GetMapping("/wearable/get/all")
    public List<WearableModel> getAllWearables() {
        return wearableService.findAll();
    }

    @GetMapping("/get/wearable/company/{companyId}")
    public ResponseEntity<List<WearableModel>> getWearablesByCompanyId(@PathVariable String companyId) {
        System.out.println("Get Wearables from company: "+ companyId+ " requested");
        return ResponseEntity.ok(wearableService.findByCompanyId(companyId));
    }


    @GetMapping("/wearable/creatorId/{creatorId}")
    public ResponseEntity<List<WearableModel>> getWearablesByCreatorId(@PathVariable String creatorId) {
        return ResponseEntity.ok(wearableService.findByCreatorId(creatorId));
    }

    @GetMapping("/wearable/get/{id}")
    public ResponseEntity<WearableModel> getWearablesById(@PathVariable String id) {
        return ResponseEntity.ok(wearableService.findById(id));
    }

    @GetMapping("/wearable/search/name")
    public ResponseEntity<List<WearableModel>> getWearablesByName(@RequestParam String name) {
        return ResponseEntity.ok(wearableService.findByName(name));
    }

    @GetMapping("/wearable/search/price")
    public ResponseEntity<List<WearableModel>> getWearablesByPrice(@RequestParam Double price) {
        return ResponseEntity.ok(wearableService.findByPrice(price));
    }

    @GetMapping("/wearable/search/color")
    public ResponseEntity<List<WearableModel>> getWearablesByColor(@RequestParam String color) {
        return ResponseEntity.ok(wearableService.findByColor(color));
    }



}