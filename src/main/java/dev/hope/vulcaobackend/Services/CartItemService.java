package dev.hope.vulcaobackend.Services;


import dev.hope.vulcaobackend.Models.AccountEntities.UserModel;
import dev.hope.vulcaobackend.Models.Cart.CartItemModel;
import dev.hope.vulcaobackend.Repositories.Cart.CartItemRepository;
import dev.hope.vulcaobackend.Repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemService {

    @Autowired private CartItemRepository cartItemRepository;
    @Autowired
    private GlobalAccessControl globalAccessControl;
    @Autowired
    private UserRepository userRepository;


    public void addToCart(CartItemModel newItem, HttpServletRequest request, UserModel user){
        newItem.setId(globalAccessControl.generateNewIdForCart());
        cartItemRepository.save(newItem);
    }

    public void decrementCartItemQuantity(CartItemModel item, Integer quantity){
        item.setQuantity(item.getQuantity() - quantity);
        if(item.getQuantity() < 0){
            throw new RuntimeException("This product cannot be decreased any longer");
        }
        if(item.getQuantity() == 0){
            deleteItemFromCart(item.getItemId());
        }
        item.onUpdate();
        cartItemRepository.update(item);
    }

    public List<CartItemModel> findUserCart(UserModel user){
        return cartItemRepository.findByCartId(user.getId());
    }

    public CartItemModel findItemById(String itemId){

        return cartItemRepository.findByItemId(itemId).orElseThrow(()-> new RuntimeException("There is not Item with this id: "+ itemId));
    }

    public void deleteItemFromCart(String itemId){
        cartItemRepository.deleteById(itemId);
    }


    public CartItemModel updateItemQuantityByOne(String itemId, HttpServletRequest request, UserModel user){

        CartItemModel item = cartItemRepository.findByItemId(itemId).orElseThrow(()-> new RuntimeException("There is no item with this id: "+ itemId));
        item.setQuantity(item.getQuantity()+1);
        item.onUpdate();
        cartItemRepository.update(item);
        return item;
    }

    public CartItemModel updateItem(String itemId,CartItemModel updatedItem, HttpServletRequest request, UserModel user){
        CartItemModel item = cartItemRepository.findByItemId(itemId).orElseThrow(()-> new RuntimeException("There is no item with this id: "+ itemId));
        item.setQuantity(updatedItem.getQuantity());
        item.setItemId(updatedItem.getItemId());
        item.onUpdate();
        cartItemRepository.update(item);
        return item;
    }

}
