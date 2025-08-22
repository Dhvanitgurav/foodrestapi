package com.food.DineEase.Service;

import com.food.DineEase.entity.OrderEntity;
import com.food.DineEase.io.OrderRequest;
import com.food.DineEase.io.OrderResponse;
import com.food.DineEase.repository.CartRepository;
import com.food.DineEase.repository.OrderRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service

public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserService userService;
    public OrderServiceImpl(OrderRepository orderRepository,
                            CartRepository cartRepository,
                            UserService userService) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.userService = userService;
    }

    @Value("${stripe_key}")
    private String stripeKey;

    @Value("${stripe_Secret}")
    private String stripeSecret;

    @Override
    public OrderResponse createOrderWithPayment(OrderRequest request) throws StripeException {
        // ✅ Use the secret key here
        Stripe.apiKey = stripeSecret;

        OrderEntity newOrder = convertToEntity(request);
        newOrder.setUserId(userService.findByUserId());
        newOrder = orderRepository.save(newOrder);

        /*PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long) (newOrder.getAmount() * 100)) // amount in smallest currency unit
                .setCurrency("inr")
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);
        newOrder.setStripePaymentIntentId(paymentIntent.getId());*/
        orderRepository.save(newOrder);

        return convertToResponse(newOrder);
    }

    @Override
    public void verifyPayment(Map<String, String> paymentData, String status) throws StripeException {
        String paymentIntentId = paymentData.get("payment_intent");
        OrderEntity existingOrder = orderRepository.findByStripePaymentIntentId(paymentIntentId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        existingOrder.setPaymentStatus(status);
        orderRepository.save(existingOrder);

        if ("paid".equalsIgnoreCase(status)) {
            cartRepository.deleteByUserId(existingOrder.getUserId());
        }
    }

    @Override
    public List<OrderResponse> getUserOrders() {
        String loggedInUserId = userService.findByUserId();
        return orderRepository.findByUserId(loggedInUserId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void removeOrder(String orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public List<OrderResponse> getOrdersOfAllUsers() {
        return orderRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void updateOrderStatus(String orderId, String status) {
        OrderEntity entity = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        entity.setOrderStatus(status);
        orderRepository.save(entity);
    }

    private OrderResponse convertToResponse(OrderEntity order) {
        return OrderResponse.builder()
                .id(order.getId())
                .amount(order.getAmount())
                .userAddress(order.getUserAddress())
                .userId(order.getUserId())
                .stripePaymentIntentId(order.getStripePaymentIntentId())
                .paymentStatus(order.getPaymentStatus())
                .orderStatus(order.getOrderStatus())
                .orderedItems(order.getOrderedItems())
                .email(order.getEmail())
                .phoneNumber(order.getPhoneNumber())
                .build();
    }

    private OrderEntity convertToEntity(OrderRequest request) {
        return OrderEntity.builder()
                .userAddress(request.getUserAddress())
                .amount(request.getAmount())
                .orderedItems(request.getOrderedItems())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .orderStatus(request.getOrderStatus())
                .build();
    }
}
