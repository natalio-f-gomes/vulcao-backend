package dev.hope.vulcaobackend.Services.Shopping;

import dev.hope.vulcaobackend.DTOs.UserDTO;
import dev.hope.vulcaobackend.Models.Offerings.OfferingModel;
import dev.hope.vulcaobackend.Models.Shopping.CartItem;
import dev.hope.vulcaobackend.Models.Shopping.CartModel;
import dev.hope.vulcaobackend.Repositories.CartRepository;
import dev.hope.vulcaobackend.Repositories.OfferingRepository;
import dev.hope.vulcaobackend.Services.Account.ClientService;
import dev.hope.vulcaobackend.Services.ExceptionHandler.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CartService {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private OfferingRepository offeringRepository;


    public void addToCart(String offeringId, Integer quantity){
        UserDTO client =  clientService.getProfile();
        OfferingModel offering = offeringRepository.findById(offeringId).orElseThrow(
                () -> new ApiException(HttpStatus.NOT_FOUND, "Não existe nenhum produto com este ID. Ele pode ter sido excluído, por favor entre em contato com o negocio!")

        );
        if(quantity <= 0){
            throw new ApiException(HttpStatus.BAD_REQUEST, "A quantidade do item no carrinho deve ser maior que 0");
        }
        System.out.println("Client Id: "+client.getId());
        CartModel cart = cartRepository.findById(client.getId()).orElseThrow(
                () -> new ApiException(HttpStatus.NOT_FOUND, "Não existe nenhum carrinho de compras com este ID!")
        );

        List<CartItem> items = cart.getItems();
        for(CartItem item: items){
            if(item != null){
                if(item.getOffering().getId().equals(offeringId)){
                    item.setQuantity(item.getQuantity() + quantity);
                    cartRepository.update(cart);
                    throw new ApiException(HttpStatus.OK, "Quantidade do item atualizada para " + (item.getQuantity() + quantity));
                }
            }

        }

        CartItem item = new CartItem();
        item.setOffering(offering);
        item.setQuantity(quantity);
        int itemsQuantity = cart.getItems().size();
        if(itemsQuantity > 50){
            throw new ApiException(HttpStatus.BAD_REQUEST, "O limite máximo de itens permitidos no carrinho é 50. Por favor, remova alguns se desejar prosseguir!");

        }
        cart.getItems().add(item);
        cartRepository.update(cart);
    }

    public void removeFromCart(String offeringId){
        UserDTO client =  clientService.getProfile();
        OfferingModel offering = offeringRepository.findById(offeringId).orElseThrow(
                () -> new ApiException(HttpStatus.NOT_FOUND, "Não existe nenhum produto com este ID. Ele pode ter sido excluído, por favor entre em contato com o negocio!")
        );
        CartModel cart = cartRepository.findById(client.getId()).orElseThrow(
                () -> new ApiException(HttpStatus.NOT_FOUND, "Não existe nenhum cliente com este ID!")
        );
        List<CartItem> items = cart.getItems();
        for(CartItem item: items){
            if(item.getOffering().getId().equals(offeringId)){
                items.remove(item);
                cartRepository.update(cart);
                return;
            }
        }
        throw new ApiException(HttpStatus.NOT_FOUND, "Este item não está no seu carrinho");
    }

    public void updateItemQuantity(String offeringId, Integer quantity){
        UserDTO client =  clientService.getProfile();
        OfferingModel offering = offeringRepository.findById(offeringId).orElseThrow(
                () -> new ApiException(HttpStatus.NOT_FOUND, "Não existe nenhum produto com este ID. Ele pode ter sido excluído, por favor entre em contato com o negocio!")

        );
        if(quantity <= 0){
            throw new ApiException(HttpStatus.BAD_REQUEST, "A quantidade do item no carrinho deve ser maior que 0");
        }
        System.out.println("Client Id: "+client.getId());
        CartModel cart = cartRepository.findById(client.getId()).orElseThrow(
                () -> new ApiException(HttpStatus.NOT_FOUND, "Não existe nenhum carrinho de compras com este ID!")
        );
        List<CartItem> items = cart.getItems();
        for(CartItem item: items){
            if(item.getOffering().getId().equals(offeringId)){
                if(Objects.equals(item.getQuantity(), quantity)){
                    return;
                }
                if(quantity > offering.getStock()){
                    throw new ApiException(HttpStatus.BAD_REQUEST, "O estoque desta oferta (" + offering.getStock() + ") é menor do que a quantidade solicitada, que é: " + quantity);
                }
                item.setQuantity(quantity);
                cartRepository.update(cart);
                return;
            }
        }
        throw new ApiException(HttpStatus.NOT_FOUND, "Este item não está no seu carrinho");
    }


}
