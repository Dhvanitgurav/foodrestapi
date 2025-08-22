package com.food.DineEase.repository;

import com.food.DineEase.entity.FoodEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FoodRepository extends MongoRepository<FoodEntity, String>{
}
