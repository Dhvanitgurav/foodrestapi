package com.food.DineEase.io;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactRequest {

        private String firstname;
        private String lastname;
        private String email;
        private String message;




}
