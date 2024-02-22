package com.example.consumer.Controllers;

import com.example.consumer.DTO.ConsumerDTO;
import com.example.consumer.DTO.LoginDTO;
import com.example.consumer.Services.ConsumerService;
import lombok.RequiredArgsConstructor;

import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/consumer")
public class MODELConsumerController {

    private final ConsumerService consumerService;


    //TODO: ADD FUNCTION TO CHECK LOGGED IN STATUS OF USER AND GRANT CERTAIN VIEW
    @GetMapping(path = "/home")
    public String showHome(Model model){

        consumerService.displayGates(model);
        return "consumer-home";
    }



}
