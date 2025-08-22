package com.food.DineEase.Service;

import com.stripe.exception.StripeException;

import java.util.Map;

public interface PaymentService {

    Map<String, String> getPublicKey();

    Map<String, String> createPaymentIntent(Long amount) throws StripeException;

    Map<String, Object> verifySessionData(String sessionId) throws StripeException;

    Map<String, String> createCheckoutSession(Map<String, Object> data) throws StripeException;
}
