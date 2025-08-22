package com.food.DineEase.Service;

import com.food.DineEase.io.FoodRequest;
import com.food.DineEase.io.FoodResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FoodService {
    String uploadFile(MultipartFile file);
 FoodResponse addFood(FoodRequest request, MultipartFile file);
 List<FoodResponse> readFoods();
FoodResponse readFood(String id);
boolean deleteFile(String filename);
void deleteFood(String id);
}
