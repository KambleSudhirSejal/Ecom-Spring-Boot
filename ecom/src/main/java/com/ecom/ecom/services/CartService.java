package com.ecom.ecom.services;

import com.ecom.ecom.dto.CartItemRequest;
import com.ecom.ecom.models.CartItem;
import com.ecom.ecom.models.Product;
import com.ecom.ecom.models.User;
import com.ecom.ecom.repositories.CartItemRepository;
import com.ecom.ecom.repositories.ProductRepository;
import com.ecom.ecom.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final ProductRepository productRepository;
    private  final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    public boolean addToCard(String userId, CartItemRequest request) {

        Optional<Product> productOpt = productRepository.findById(request.getProductId());
        if(productOpt.isEmpty()){
            return false;
        }
        Product product = productOpt.get();
        if(product.getStockQuantity() < request.getQuantity())
            return false;

        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));

        if(userOpt.isEmpty()){
            return false;
        }

        User user = userOpt.get();

        CartItem existingCartItem = cartItemRepository.findByUserAndProduct(user,product);

        if(existingCartItem != null){
            //updating quantity
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
            cartItemRepository.save(existingCartItem);
        }else{

            //create new cart item

            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
            cartItemRepository.save(cartItem);

        }

        return true;
    }

    public boolean deleteItemFromCart(String userId, Long productId) {

        Optional<Product> productOpt = productRepository.findById(productId);
        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));

        if(productOpt.isPresent() && userOpt.isPresent()){
            cartItemRepository.deleteByUserAndProduct(userOpt.get(),productOpt.get());
            return true;

        }

      return false;

    }


    public List<CartItem> getCart(String userId) {

        return userRepository.findById(Long.valueOf(userId))
                .map(cartItemRepository::findByUser)
                .orElseGet(List::of);
    }
}
