package com.food.DineEase.controller;

import com.food.DineEase.Service.PaymentService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/public-key")
    public Map<String, String> getPublicKey() {
        return paymentService.getPublicKey();
    }

    @PostMapping("/create-payment-intent")
    public Map<String, String> createPaymentIntent(@RequestParam Long amount) throws StripeException {
        return paymentService.createPaymentIntent(amount);
    }

    @GetMapping("/verify-session")
    public Map<String, Object> verifySession(@RequestParam String sessionId,
                                             @RequestHeader("Authorization") String authHeader) throws StripeException {
        return paymentService.verifySessionData(sessionId);
    }

    @PostMapping("/create-checkout-session")
    public Map<String, String> createCheckoutSession(@RequestBody Map<String, Object> data) throws StripeException {
        return paymentService.createCheckoutSession(data);
    }
}
















































/*package com.food.DineEase.controller;

import com.food.DineEase.Service.CartService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    @Autowired
    private CartService cartService;
    @Value("${stripe_key}")  // Public key for frontend
    private String stripePublicKey;

    @Value("${stripe_Secret}")
    private String stripeSecretKey;

    //  Endpoint to get public key for frontend
    @GetMapping("/public-key")
    public Map<String, String> getPublicKey() {
        Map<String, String> response = new HashMap<>();
        response.put("publicKey", stripePublicKey);
        return response;
    }

    // Endpoint to create a PaymentIntent
    @PostMapping("/create-payment-intent")
    public Map<String, String> createPaymentIntent(@RequestParam Long amount) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount)  // amount in paise (for INR) or cents (for USD)
                .setCurrency("usd") // change to "inr" if you’re using INR
                .build();

        PaymentIntent intent = PaymentIntent.create(params);

        Map<String, String> response = new HashMap<>();
        response.put("clientSecret", intent.getClientSecret());
        return response;
    }

    @GetMapping("/verify-session")
    public Map<String, Object> verifySession(@RequestParam String sessionId, @RequestHeader("Authorization") String authHeader) throws StripeException {


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


    @PostMapping("/create-checkout-session")
    public Map<String, String> createCheckoutSession(@RequestBody Map<String, Object> data) throws Exception {
        String orderId = (String) data.get("orderId");
        Integer amount = (Integer) data.get("amount"); // amount in paise

        Stripe.apiKey = stripeSecretKey; // use secret key

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
}*/