package dev.hope.vulcaobackend.RestControllers.Private;

import dev.hope.vulcaobackend.Authentication.CookieUtil;
import dev.hope.vulcaobackend.Authentication.JWTService;
import dev.hope.vulcaobackend.DTOs.*;
import dev.hope.vulcaobackend.Properties.Address;
import dev.hope.vulcaobackend.Properties.OrderStatus;
import dev.hope.vulcaobackend.Properties.UserType;
import dev.hope.vulcaobackend.Repositories.CartRepository;
import dev.hope.vulcaobackend.Services.Account.ClientService;
import dev.hope.vulcaobackend.Services.Shopping.CartService;
import dev.hope.vulcaobackend.Services.Shopping.OrderService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/api/v2/client")
public class ClientController {

    @Autowired private ClientService clientService;
    @Autowired private CookieUtil cookieUtil;
    @Autowired private JWTService jWTService;
    @Autowired private CartService cartService;
    @Autowired private CartRepository cartRepository;
    @Autowired private OrderService orderService;

    @GetMapping("get/profile")
    public ResponseEntity<?> getProfile(){
        return ResponseEntity.ok(clientService.getProfile());
    }

    @PutMapping("update/name")
    public ResponseEntity<?> updateClientName(@RequestBody FullName fullName){
        clientService.updateUserName(fullName);
        return ResponseEntity.ok().build();
    }

    @PutMapping("update/address")
    public ResponseEntity<?> updateClientAddress(@RequestBody Address address){
        clientService.updateUserAddress(address);
        return ResponseEntity.ok().build();
    }

    @PutMapping("update/contact")
    public ResponseEntity<?> updateClientContact(@RequestBody Contact contact){
        clientService.updateUserContact(contact);
        return ResponseEntity.ok().build();
    }

    @PutMapping("update/credentials")
    public ResponseEntity<?> updateClientCredentials(@RequestBody Credentials credentials,HttpServletResponse response){
        String username = clientService.updateUserCredentials(credentials);

        String token = jWTService.generateJWTToken(username, UserType.CLIENT.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, cookieUtil.createCookie(token).toString());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> clientLogout(HttpServletResponse response, Principal principal){

        if (principal == null) {
            // still clear the cookie so a half-logged-out client gets cleaned up
            response.addHeader(HttpHeaders.SET_COOKIE, cookieUtil.clearCookie().toString());
            return ResponseEntity.ok("ALREADY LOGGED OUT");
        }
        log.debug("clientLogout Endpoint reached:");

        response.addHeader(HttpHeaders.SET_COOKIE,cookieUtil.clearCookie().toString());
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Sessão encerrada com sucesso para o usuário: " + principal.getName());
    }

    @GetMapping("get/cart")
    public ResponseEntity<?> getClientCart(){
        UserDTO user = clientService.getProfile();
        return ResponseEntity.ok(cartRepository.findById(user.getId()));
    }

    @PostMapping("/cart/add/{offeringId}/{quantity}")
    public ResponseEntity<?> AddToCart(@PathVariable String offeringId, @PathVariable Integer quantity){
        cartService.addToCart(offeringId, quantity);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/cart/update/{offeringId}/{quantity}")
    public ResponseEntity<?> UpdateCartItem(@PathVariable String offeringId, @PathVariable Integer quantity){
        System.out.println("Endpoint reached!");
        cartService.updateItemQuantity(offeringId, quantity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/cart/delete/{offeringId}")
    public ResponseEntity<?> DeleteCartItem(@PathVariable String offeringId){
        cartService.removeFromCart(offeringId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/place/order/{businessId}")
    public ResponseEntity<?> PlaceOrder(@PathVariable String businessId, @RequestBody OrderRequest orderRequest) {

        for(OrderRequest.OrderItemRequest item: orderRequest.getItems()){
            System.out.println("PlaceOrder Item: " + item.name());
        }
        orderService.placeOrder (businessId, orderRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get/order/trackId")
    public ResponseEntity<?> findOrderByTrackId(@RequestParam String trackId){
        return ResponseEntity.ok(orderService.findByTrackId(trackId));
    }

    @GetMapping("/get/orders")
    public ResponseEntity<?> FindOrders(){
        return ResponseEntity.ok(orderService.findAllClientOrders());
    }


    @PutMapping("/cancel/order/{trackId}")
    public ResponseEntity<?> CancelOrder(@PathVariable String trackId){
        System.out.println("CANCEL ORDER ENDPOINT CALLED: " + trackId);
        orderService.ClientCancelOrder(trackId);
        return ResponseEntity.ok().build();
    }



}