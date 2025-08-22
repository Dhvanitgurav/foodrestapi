package com.food.DineEase.Service;

import com.food.DineEase.io.CartRequest;
import com.food.DineEase.io.CartResponse;

public interface CartService {
    public CartResponse addToCart(CartRequest request);
    CartResponse getCart();

    void clearCart();
     CartResponse removeFromCart(CartRequest cartRequest);
}
