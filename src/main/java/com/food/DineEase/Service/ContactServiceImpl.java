package com.food.DineEase.Service;

import com.food.DineEase.io.ContactRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements ContactService {
    @Autowired
    private JavaMailSender mailSender;
@Override
    public void sendContactEmail(ContactRequest request){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("guravdhwanit999@gmail.com");
        message.setSubject("New Contact Message from " + request.getFirstname() + " " + request.getLastname());
        message.setText(
                "Name: " + request.getFirstname() + " " + request.getLastname() + "\n" +
                        "Email: " + request.getEmail() + "\n\n" +
                        "Message:\n" + request.getMessage()
        );

        mailSender.send(message);
    }
}
