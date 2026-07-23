package com.ecom.ecom.controller;

import com.ecom.ecom.dto.CartItemRequest;
import com.ecom.ecom.models.CartItem;
import com.ecom.ecom.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(
            @RequestHeader("X-USER-ID") String userId
    ){

        return ResponseEntity.ok(cartService.getCart(userId));

    }


    @PostMapping
    public ResponseEntity<String> addToCart(
            @RequestHeader("X-USER-ID") String userId,
            @RequestBody CartItemRequest request
    ){
        if(!cartService.addToCard(userId,request)){
            return ResponseEntity.badRequest().body("Product out of stock or User not found or product not found");
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeFromCart(
          @RequestHeader("X-USER-ID")  String userId , @PathVariable Long productId
    ){
      boolean deleted =   cartService.deleteItemFromCart(userId,productId);
      return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();

    }


}
