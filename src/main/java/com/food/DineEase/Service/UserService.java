package com.food.DineEase.Service;

import com.food.DineEase.io.UserRequest;
import com.food.DineEase.io.UserResponse;

public interface UserService {
    UserResponse registerUser(UserRequest request);

    String findByUserId();
}
