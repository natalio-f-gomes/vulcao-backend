package dev.hope.vulcaobackend.RestControllers.System;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v2/actuator")
public class SystemController {

    @GetMapping("/health")
    public ResponseEntity<?> GetHealth(){
        return ResponseEntity.ok("THE SYSTEM IS UP AND RUNNING");
    }
}
