package com.food.DineEase.Service;

import com.food.DineEase.io.OrderRequest;
import com.food.DineEase.io.OrderResponse;
import com.stripe.exception.StripeException;

import java.util.List;
import java.util.Map;

public interface OrderService {
    OrderResponse createOrderWithPayment(OrderRequest request) throws StripeException;

    void verifyPayment(Map<String, String> paymentData, String status) throws StripeException;

    List<OrderResponse> getUserOrders();

    void removeOrder(String orderId);

    List<OrderResponse> getOrdersOfAllUsers();

    void updateOrderStatus(String orderId, String status);
}
