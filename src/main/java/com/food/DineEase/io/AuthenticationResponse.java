package com.food.DineEase.io;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationResponse {
    private String email;
    private String token;
}
