package dev.hope.vulcaobackend.Services;


import dev.hope.vulcaobackend.Models.AccountEntities.UserModel;
import dev.hope.vulcaobackend.Models.Cart.CartItemModel;
import dev.hope.vulcaobackend.Repositories.Cart.CartItemRepository;
import dev.hope.vulcaobackend.Repositories.UserRepository;
import dev.hope.vulcaobackend.Services.Offerings.GlobalAccessControl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CartItemService {

    @Autowired private CartItemRepository cartItemRepository;
    @Autowired
    private GlobalAccessControl globalAccessControl;
    @Autowired
    private UserRepository userRepository;


    public void addToCart(CartItemModel newItem, HttpServletRequest request, UserModel user){
        globalAccessControl.canUserPerformAction(user, request);
        newItem.setId(globalAccessControl.generateNewIdForCart());
        cartItemRepository.save(newItem);
    }

    public List<CartItemModel> findUserCart(HttpServletRequest request, UserModel user){
        globalAccessControl.canUserPerformAction(user, request);
        return cartItemRepository.findByCartId(user.getId());
    }

    public CartItemModel findItemById(String itemId,  HttpServletRequest request, UserModel user){
        globalAccessControl.canUserPerformAction(user, request);
        return cartItemRepository.findByItemId(itemId).orElseThrow(()-> new RuntimeException("There is not Item with this id: "+ itemId));
    }

    public void deleteItemFromCart(String itemId,HttpServletRequest request, UserModel user){
        globalAccessControl.canUserPerformAction(user, request);
        cartItemRepository.deleteById(itemId);
    }


    public CartItemModel updateItemQuantityByOne(String itemId, HttpServletRequest request, UserModel user){
        globalAccessControl.canUserPerformAction(user, request);

        CartItemModel item = cartItemRepository.findByItemId(itemId).orElseThrow(()-> new RuntimeException("There is no item with this id: "+ itemId));
        item.setQuantity(item.getQuantity()+1);
        item.onUpdate();
        cartItemRepository.update(item);
        return item;
    }

    public CartItemModel updateItem(String itemId,CartItemModel updatedItem, HttpServletRequest request, UserModel user){
        globalAccessControl.canUserPerformAction(user, request);

        CartItemModel item = cartItemRepository.findByItemId(itemId).orElseThrow(()-> new RuntimeException("There is no item with this id: "+ itemId));
        item.setQuantity(updatedItem.getQuantity());
        item.setItemId(updatedItem.getItemId());
        item.onUpdate();
        cartItemRepository.update(item);
        return item;
    }

}
