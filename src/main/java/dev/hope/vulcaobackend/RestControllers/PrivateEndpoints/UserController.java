package dev.hope.vulcaobackend.RestControllers.PrivateEndpoints;


import dev.hope.vulcaobackend.DataTransferObjects.AccountDTOs.UserDTO;
import dev.hope.vulcaobackend.DataTransferObjects.UserContactData;
import dev.hope.vulcaobackend.DataTransferObjects.UserCredentials;
import dev.hope.vulcaobackend.DataTransferObjects.UserFullName;
import dev.hope.vulcaobackend.Models.AccountEntities.UserModel;
import dev.hope.vulcaobackend.Models.CompanyModel;
import dev.hope.vulcaobackend.Models.Shopping.CartItemModel;
import dev.hope.vulcaobackend.Models.Common.Address;
import dev.hope.vulcaobackend.Models.Offerings.ProductModel;
import dev.hope.vulcaobackend.Models.Properties.OfferingType;

import dev.hope.vulcaobackend.Repositories.Cart.CartItemRepository;
import dev.hope.vulcaobackend.Repositories.CompanyRepository;
import dev.hope.vulcaobackend.Repositories.Offerings.ProductRepository;
import dev.hope.vulcaobackend.Repositories.UserRepository;
import dev.hope.vulcaobackend.Services.CartItemService;
import dev.hope.vulcaobackend.Services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Autowired
    private CompanyRepository companyRepository;private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @DeleteMapping("/delete/{userID}")
    public ResponseEntity<?> deleteUserById(@PathVariable String userID, HttpServletRequest request) throws AccessDeniedException {
        logger.debug("REST request to deleteUserById: userID={}", userID);

        userService.deleteUserById(userID);

        logger.debug("Successfully deleted user with id: {}", userID);
        return ResponseEntity.noContent().build();
    }

    //Get User profile
    @GetMapping("/get/my-profile")
    public ResponseEntity<?> getCurrentUserProfile() {

        logger.debug("REST request to getCurrentUserProfile");

        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        logger.debug("Resolved session username: {}", sessionUser);

        UserDTO user = userService.findUserProfile(sessionUser);
        logger.debug("Found profile for user: {}", user != null ? user.getUsername() : "null");

        return ResponseEntity.ok(user);
    }

    //Update User profile
    @PutMapping("/update/{userId}/profile")
    public ResponseEntity<?> updatePersonalInfo(@PathVariable String userId, @RequestBody UserFullName updatedUser) throws Exception {
        logger.debug("REST request to updatePersonalInfo: userId={}", userId);

        userService.editUserPersonalData(userId, updatedUser);

        logger.debug("Successfully updated personal profile info for userId: {}", userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{userId}/address")
    public ResponseEntity<?> updateUserAddress(@PathVariable String userId, @RequestBody Address newAddress) throws AccessDeniedException {
        logger.debug("REST request to updateUserAddress: userId={}", userId);
        try {
            userService.editUserAddress(userId, newAddress);
            logger.debug("Successfully updated address for userId: {}", userId);
            return ResponseEntity.ok().build();
        } catch (AccessDeniedException e) {
            logger.debug("Access denied when trying to update user address for userId: {}. Error: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("/update/{userId}/credentials")
    public ResponseEntity<?> updateUserCredentials(@PathVariable String userId, @RequestBody UserCredentials newCredentials) throws Exception {
        logger.debug("REST request to updateUserCredentials: userId={}", userId);

        userService.editUserCredentials(userId, newCredentials);

        logger.debug("Successfully updated credentials for userId: {}", userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{userId}/contact")
    public ResponseEntity<?> updateUserContact(@PathVariable String userId, @RequestBody UserContactData updatedUser) throws Exception {
        logger.debug("REST request to updateUserContact: userId={}", userId);

        userService.editUserContact(userId, updatedUser);

        logger.debug("Successfully updated contact data for userId: {}", userId);
        return ResponseEntity.ok().build();
    }

    //=======CART ITEM =======
    @PostMapping("/cart/add/product/{productId}/{productQuantity}/{companyId}")
    public ResponseEntity<?> addItemToCart(@PathVariable String productId, @PathVariable Integer productQuantity, @PathVariable String companyId, HttpServletRequest request) {
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        logger.debug("REST request to addItemToCart: productId={}, quantity={}, companyId={}, sessionUser={}", productId, productQuantity, companyId, sessionUser);

        UserModel user = userRepository.findByUsername(sessionUser).orElseThrow(() -> {
            logger.debug("Access Denied: Session user '{}' not found in database.", sessionUser);
            return new org.springframework.security.access.AccessDeniedException("You do not have access to the user cart");
        });

        ProductModel product = productRepository.findById(productId).orElseThrow(() -> {
            logger.debug("Add to cart failed: Product not found with id: {}", productId);
            return new RuntimeException("There is no product with this id: " + productId);
        });

        CompanyModel company = companyRepository.findById(companyId).orElseThrow(() -> {
            logger.debug("Add to cart failed: Company not found with id: {}", companyId);
            return new RuntimeException("Company with id: " + companyId + " not found");
        });

        boolean isItemInCart = cartItemRepository
                .findCartItem(user.getId(), companyId, productId).isPresent();
        if (isItemInCart) {
            CartItemModel item = cartItemRepository.findCartItem(user.getId(), companyId, productId).orElseThrow(()-> new RuntimeException("This item does not exists in the cart: "+ productId));
            logger.debug("The product does exists in the cart: {} with the name of:{} ", productId, product.getName());
            item.setQuantity(productQuantity);
            item.onUpdate();
            cartItemRepository.update(item);
            return ResponseEntity.ok(item);
        }

        logger.debug("Creating new CartItemModel for product: {}", productId);
        CartItemModel newItem = new CartItemModel();
        newItem.setId(productId);
        newItem.setQuantity(productQuantity);
        newItem.setItemType(OfferingType.PRODUCT);
        newItem.setCompanyId(companyId);
        newItem.setUserId(user.getId());
        newItem.onCreate();

        cartItemService.addToCart(newItem, request, user);
        logger.debug("Successfully added new product {} to user {}'s cart", productId, user.getId());
        return ResponseEntity.ok(newItem);
    }




    @GetMapping("/get/cart/{cartId}/{companyId}")
    public ResponseEntity<List<CartItemModel>> getUserCartItems(@PathVariable String cartId, @PathVariable String companyId) {
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        logger.debug("REST request to getUserCartItems: cartId={}, companyId={}, sessionUser={}", cartId, companyId, sessionUser);

        UserModel user = userRepository.findByUsername(sessionUser).orElseThrow(() -> {
            logger.debug("Access Denied: Session user '{}' not found.", sessionUser);
            return new org.springframework.security.access.AccessDeniedException("You do not have access to the user cart");
        });

        if (!user.getId().equals(cartId)) {
            logger.debug("Forbidden access: Session user id '{}' does not match requested cartId '{}'", user.getId(), cartId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<CartItemModel> items = cartItemService.findUserCartItems(user.getId(), companyId);
        logger.debug("Total items fetched from service for cartId {}: {}", cartId, items.size());

        return ResponseEntity.ok(items);
    }

    @GetMapping("/get/cart/item/{itemId}/{companyId}")
    public ResponseEntity<?> getUserCartItemById(@PathVariable String itemId, @PathVariable String companyId) {
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        logger.debug("REST request to getUserCartItemById: itemId={}, sessionUser={}", itemId, sessionUser);
        try {
            UserModel user = userRepository.findByUsername(sessionUser).orElseThrow(() -> {
                logger.debug("Access Denied: Session user '{}' not found.", sessionUser);
                return new org.springframework.security.access.AccessDeniedException("You do not have access to the user cart");
            });

            CartItemModel item = cartItemService.findItemById(itemId);
            logger.debug("Successfully found cart item details for itemId: {}", itemId);
            return ResponseEntity.ok(item);
        } catch (RuntimeException e) {
            logger.debug("Failed to fetch cart item by id {}: {}", itemId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/delete/cart/item/{itemId}/{companyId}")
    public ResponseEntity<?> removeItemFromCart(@PathVariable String itemId, @PathVariable String companyId) {
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        logger.debug("REST request to deleteCratItemById (typo in mapping retained): itemId={}, sessionUser={}", itemId, sessionUser);

        UserModel user = userRepository.findByUsername(sessionUser).orElseThrow(() -> {
            logger.debug("Access Denied: Session user '{}' not found.", sessionUser);
            return new org.springframework.security.access.AccessDeniedException("You do not have access to the user cart");
        });

        cartItemService.deleteItemFromCart(user.getId(), companyId, itemId);
        logger.debug("Successfully removed item {} from database.", itemId);
        return ResponseEntity.ok().build();
    }
}


