package dev.hope.vulcaobackend.RestControllers.Public;

import dev.hope.vulcaobackend.Services.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/public/business")
public class PublicBusinessController {

    @Autowired
    BusinessService businessService;

    @GetMapping("/get/all")
    public ResponseEntity<?> findALlBusinesses() {
        return ResponseEntity.ok(businessService.findAll());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> findBusinessById(@PathVariable String id) {
        return ResponseEntity.ok(businessService.findById(id));
    }
}
