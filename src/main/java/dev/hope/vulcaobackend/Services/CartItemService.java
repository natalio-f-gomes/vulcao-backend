package dev.hope.vulcaobackend.Services;


import dev.hope.vulcaobackend.Models.AccountEntities.UserModel;
import dev.hope.vulcaobackend.Models.Shopping.CartItemModel;
import dev.hope.vulcaobackend.Repositories.Cart.CartItemRepository;
import dev.hope.vulcaobackend.Repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CartItemService {

    @Autowired private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;


    private static final Logger logger = LoggerFactory.getLogger(CartItemService.class);


    public void addToCart(CartItemModel newItem, HttpServletRequest request, UserModel user){
        logger.debug("Entering addToCart with newItem: {}, user: {}", newItem, user != null ? user.getId() : "null");
        cartItemRepository.save(newItem);
        logger.debug("Successfully added item to cart with generated ID: {}", newItem.getId());
    }


    public List<CartItemModel> findUserCartItems(String userId, String companyId){
        List<CartItemModel> cart = cartItemRepository.findCartItems(userId, companyId);
        logger.debug("Found {} items in cart for user: {}", cart.size(), userId);
        return cart;
    }


    public CartItemModel findItemById(String itemId){
        logger.debug("Entering findItemById with itemId: {}", itemId);
        return cartItemRepository.findByItemId(itemId).orElseThrow(() -> {
            logger.debug("Item not found with id: {}", itemId);
            return new RuntimeException("There is not Item with this id: "+ itemId);
        });
    }

    public List<CartItemModel> findCartItemByCompanyId(String companyId){
        logger.debug("Entering findItemByCompanyId with companyId: {}", companyId);

        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        logger.debug("Current session user name: {}", sessionUser);

        UserModel user = userRepository.findByUsername(sessionUser).orElseThrow(() -> {
            logger.debug("User not found in repository: {}", sessionUser);
            return new UsernameNotFoundException("User not found: " + sessionUser);
        });

        List<CartItemModel> items = cartItemRepository.findByCompanyId(companyId);
        logger.debug("Found {} items for companyId: {}", items.size(), companyId);
        return items;
    }

    public void deleteItemFromCart(String userId,String companyId, String itemId){
        logger.debug("Entering deleteItemFromCart with itemId: {}", itemId);
        CartItemModel item = cartItemRepository.findCartItem(userId, companyId, itemId).orElseThrow(()-> new RuntimeException("This item does not exists in the cart: "+ itemId));
        cartItemRepository.delete(item);
        logger.debug("Successfully deleted item from cart: {}", itemId);
    }



}
