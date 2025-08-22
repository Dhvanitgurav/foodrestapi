package com.food.DineEase.controller;


import com.food.DineEase.Service.ContactService;
import com.food.DineEase.io.ContactRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping
    public String sendMessage(@RequestBody ContactRequest request) {
        contactService.sendContactEmail(request);
        return "Message sent successfully!";
    }
}
