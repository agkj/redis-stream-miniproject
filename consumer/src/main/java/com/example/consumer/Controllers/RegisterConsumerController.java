package com.example.consumer.Controllers;

import com.example.consumer.DTO.ConsumerDTO;
import com.example.consumer.Services.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//TODO: This can only be accessed by admins
@Controller
@RequestMapping("/consumer/registration")
public class RegisterConsumerController {

    @Autowired
    private ConsumerService consumerService;

    //returns empty constructor, used to define the model
    @ModelAttribute("consumerUser")
    public ConsumerDTO consumerDTO() {
        return new ConsumerDTO();
    }
    @GetMapping
    public String showRegistration(){
        return "consumer-registration";
    }
    @PostMapping
    public String registerNewConsumerUser(@ModelAttribute("consumerUser")ConsumerDTO consumerDTO){

        Boolean newConsumer = consumerService.addNewConsumer(consumerDTO);

        if(newConsumer){

            System.out.println("Registered " + newConsumer);
            return "redirect:/consumer/registration?success";
        }
        else {
            return "redirect:/consumer/registration?failure";
        }




    }



}
