package dev.hope.vulcaobackend.RestControllers.Public;

import dev.hope.vulcaobackend.Services.Account.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/public/client")
public class PublicClientController {
    @Autowired
    ClientService clientService;

    @GetMapping("/get/{id}")
    public ResponseEntity<?>  GetClientById(@PathVariable String id){
        return ResponseEntity.ok(clientService.findById(id));
    }
}
