package dev.hope.vulcaobackend.RestControllers.PrivateEndpoints;


import dev.hope.vulcaobackend.DataTransferObjects.AccountDTOs.UserDTO;
import dev.hope.vulcaobackend.DataTransferObjects.UserContactData;
import dev.hope.vulcaobackend.DataTransferObjects.UserCredentials;
import dev.hope.vulcaobackend.DataTransferObjects.UserFullName;
import dev.hope.vulcaobackend.Models.AccountEntities.UserModel;
import dev.hope.vulcaobackend.Models.Cart.CartItemModel;
import dev.hope.vulcaobackend.Models.Common.Address;
import dev.hope.vulcaobackend.Models.Offerings.ProductModel;
import dev.hope.vulcaobackend.Models.Properties.OfferingType;

import dev.hope.vulcaobackend.Repositories.Cart.CartItemRepository;
import dev.hope.vulcaobackend.Repositories.Offerings.ProductRepository;
import dev.hope.vulcaobackend.Repositories.UserRepository;
import dev.hope.vulcaobackend.Services.CartItemService;
import dev.hope.vulcaobackend.Services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class UserController {


    @Autowired
    UserService userService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartItemRepository cartItemRepository;



    @DeleteMapping("/delete/{userID}")
    public ResponseEntity<?> deleteUserById(@PathVariable String userID, HttpServletRequest request) throws AccessDeniedException {


        userService.deleteUserById(userID);
        return ResponseEntity.noContent().build();
    }

    //Get User profile
    @GetMapping("/get/my-profile")
    public ResponseEntity<?> getCurrentUserProfile() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        System.out.println("Method called: " + methodName);
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        UserDTO user = userService.findUserProfile(sessionUser);
        System.out.println("USER PROFILE: " + user.getUsername());
        return ResponseEntity.ok(user);
    }

    //Update User profile
    @PutMapping("/update/{userId}/profile")
    public ResponseEntity<?> updatePersonalInfo(@PathVariable String userId, @RequestBody UserFullName updatedUser) throws Exception {
        userService.editUserPersonalData(userId, updatedUser);
        return ResponseEntity.ok().build();

    }

    @PutMapping("/update/{userId}/address")
    public ResponseEntity<?> updateUserAddress(@PathVariable String userId, @RequestBody Address newAddress) throws AccessDeniedException {
        try{
            userService.editUserAddress(userId, newAddress);
            return ResponseEntity.ok().build();
        }catch (AccessDeniedException e){
            log.debug("DEBUG: ACCESS DENIED WHEN TRYING TO UPDATE USER ADDRESS");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }

    @PutMapping("/update/{userId}/credentials")
    public ResponseEntity<?> updateUserCredentials(@PathVariable String userId, UserCredentials newCredentials ) throws Exception {
        userService.editUserCredentials(userId, newCredentials);
        return ResponseEntity.ok().build();

    }

    @PutMapping("/update/{userId}/contact")
    public ResponseEntity<?> updateUserContact(@PathVariable String userId, @RequestBody UserContactData updatedUser) throws Exception {
            userService.editUserContact(userId, updatedUser);
            return ResponseEntity.ok().build();

    }

    //=======CART ITEM =======
    @PostMapping("/cart/add/product/{productId}/{productQuantity}")
    public ResponseEntity<?> addItemToCart(@PathVariable String productId, @PathVariable Integer productQuantity,  HttpServletRequest request){

        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        UserModel user = userRepository.findByUsername(sessionUser).orElseThrow(()-> new org.springframework.security.access.AccessDeniedException("You do not have access to the user cart"));
        ProductModel product = productRepository.findById(productId).orElseThrow(()-> new RuntimeException("There is no product with this id: "+productId));

        boolean isItemInCart = cartItemRepository.findByItemId(productId).isPresent();
        if(isItemInCart){
            System.out.println("This item is already in the cart. Lets update its quantity by 1");
            cartItemService.updateItemQuantityByOne(productId, request, user);
            return ResponseEntity.ok().build();
        }
        CartItemModel newItem = new CartItemModel();
        newItem.setItemId(productId);
        newItem.setQuantity(productQuantity);
        newItem.setItemType(OfferingType.PRODUCT);
        newItem.setCartId(user.getId());
        newItem.onCreate();
        cartItemService.addToCart(newItem,request, user);
        return ResponseEntity.ok().build();

    }

    @PutMapping("/cart/decrement/product/{productId}/{productQuantity}")
    public ResponseEntity<?> decrementCartItem(@PathVariable String productId, @PathVariable Integer productQuantity,  HttpServletRequest request){

        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        UserModel user = userRepository.findByUsername(sessionUser).orElseThrow(()-> new org.springframework.security.access.AccessDeniedException("You do not have access to the user cart"));
        ProductModel product = productRepository.findById(productId).orElseThrow(()-> new RuntimeException("There is no product with this id: "+productId));
        CartItemModel cartItem = cartItemRepository.findByItemId(productId).orElseThrow(()-> new RuntimeException("This item does not exist in the cart"));
        cartItemService.decrementCartItemQuantity(cartItem, productQuantity);
        return ResponseEntity.ok().build();


    }

    @PutMapping("/cart/update/product/{itemId}/quantity/{quantity}")
    public ResponseEntity<?> updateCartProduct(@PathVariable String itemId,@PathVariable Integer quantity, HttpServletRequest request){
            String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
            UserModel user = userRepository.findByUsername(sessionUser).orElseThrow(()-> new org.springframework.security.access.AccessDeniedException("You do not have access to the user cart"));
            CartItemModel cartItem = cartItemRepository.findByItemId(itemId).orElseThrow(()-> new RuntimeException("There is no item with this id: "+itemId));
            cartItem.setQuantity(quantity);
            cartItemService.updateItem(itemId,cartItem,request, user);
            return ResponseEntity.status(HttpStatus.OK).build();

    }

    @PutMapping("/cart/update/product/{itemId}")
    public ResponseEntity<?> updateCartProductByOne(@PathVariable String itemId,HttpServletRequest request){
        try{
            String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
            UserModel user = userRepository.findByUsername(sessionUser).orElseThrow(()-> new org.springframework.security.access.AccessDeniedException("You do not have access to the user cart"));
            cartItemService.updateItemQuantityByOne(itemId,request, user);
            return ResponseEntity.status(HttpStatus.OK).build();
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/get/cart/{cartId}")
    public ResponseEntity<List<CartItemModel>> getUserCartItems(@PathVariable String cartId){
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        UserModel user = userRepository.findByUsername(sessionUser).orElseThrow(()-> new org.springframework.security.access.AccessDeniedException("You do not have access to the user cart"));
        if(!user.getId().equals(cartId)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<CartItemModel> items =  cartItemService.findUserCart(user);
        System.out.println("Total Items: " + items.size());
        return ResponseEntity.ok(items);

    }

    @GetMapping("/get/cart/item/{itemId}")
    public ResponseEntity<?> getUserCartItemById(@PathVariable String itemId,  HttpServletRequest request){
        try{
            String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
            UserModel user = userRepository.findByUsername(sessionUser).orElseThrow(()-> new org.springframework.security.access.AccessDeniedException("You do not have access to the user cart"));
            CartItemModel item =  cartItemService.findItemById(itemId);
            return ResponseEntity.ok(item);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/delete/cart/item/{itemId}")
    public ResponseEntity<?> deleteCratItemById(@PathVariable String itemId,  HttpServletRequest request){

        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        UserModel user = userRepository.findByUsername(sessionUser).orElseThrow(()-> new org.springframework.security.access.AccessDeniedException("You do not have access to the user cart"));
        cartItemService.deleteItemFromCart(itemId);
        return ResponseEntity.ok().build();

    }



}
