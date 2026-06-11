package dev.hope.vulcaobackend.RestControllers.PrivateEndpoints;

import dev.hope.vulcaobackend.Models.Shopping.OrderModel;
import dev.hope.vulcaobackend.Repositories.OrderRepository;
import dev.hope.vulcaobackend.Services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/user/order")
public class OrderController {

    @Autowired OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<?> placeOrder(@RequestBody OrderModel newOrder){
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        orderService.placeOrder(sessionUser, newOrder);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/get/{trackId}/{companyId}")
    public ResponseEntity<?> findOrderByTrackId(@PathVariable String trackId, @PathVariable String companyId){
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        return ResponseEntity.ok(orderService.findOrderByTrackId(companyId, sessionUser, trackId));
    }

    @GetMapping("/get/{companyId}/{userId}/{orderId}")
    public ResponseEntity<?> findOrder(@PathVariable String companyId, @PathVariable String userId, @PathVariable String orderId){
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        return ResponseEntity.ok(orderService.findOrder(companyId, userId, orderId, sessionUser));
    }

    @GetMapping("/get/all/{userId}/{companyId}")
    public ResponseEntity<?> findAllUserOrders(@PathVariable String userId, @PathVariable String companyId){
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        return ResponseEntity.ok(orderService.findAllUserOrders(sessionUser, userId, companyId));
    }

    @GetMapping("/get/item/{itemId}")
    public ResponseEntity<?> findOrderItemById(@PathVariable String itemId){
        return ResponseEntity.ok(orderService.findOrderItemById(itemId));
    }

    //TODO: UPDATE THE ORDER, DELETE THE ORDER

}
