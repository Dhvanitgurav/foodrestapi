package com.food.DineEase.controller;

import com.food.DineEase.Service.UserService;
import com.food.DineEase.io.UserRequest;
import com.food.DineEase.io.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@RequestBody UserRequest request) {
return userService.registerUser(request);

    }
}
