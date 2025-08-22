package com.food.DineEase.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodResponse {
    private String id;
    private String imageUrl;
    private String name;
    private String description;
    private double price;
    private String category;

}
