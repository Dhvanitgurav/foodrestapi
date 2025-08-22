package com.food.DineEase.Service;

import com.food.DineEase.Service.CartService;
import com.food.DineEase.Service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final CartService cartService;

    @Value("${stripe_key}")
    private String stripePublicKey;

    @Value("${stripe_Secret}")
    private String stripeSecretKey;

    @Override
    public Map<String, String> getPublicKey() {
        Map<String, String> response = new HashMap<>();
        response.put("publicKey", stripePublicKey);
        return response;
    }

    @Override
    public Map<String, String> createPaymentIntent(Long amount) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency("usd") // or "inr"
                .build();

        PaymentIntent intent = PaymentIntent.create(params);

        Map<String, String> response = new HashMap<>();
        response.put("clientSecret", intent.getClientSecret());
        return response;
    }

    @Override
    public Map<String, Object> verifySessionData(String sessionId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;
        Session session = Session.retrieve(sessionId);

        if ("paid".equals(session.getPaymentStatus())) {
            cartService.clearCart();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", session.getId());
        response.put("amount", session.getAmountTotal());
        response.put("customer_email", session.getCustomerEmail());
        response.put("status", session.getPaymentStatus());
        return response;
    }

    @Override
    public Map<String, String> createCheckoutSession(Map<String, Object> data) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        String orderId = (String) data.get("orderId");
        Integer amount = (Integer) data.get("amount");

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:5173/payment-success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("http://localhost:5173/payment-cancel")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("inr")
                                                .setUnitAmount(Long.valueOf(amount))
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Order " + orderId)
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();

        Session session = Session.create(params);

        Map<String, String> response = new HashMap<>();
        response.put("id", session.getId());
        return response;
    }
}
