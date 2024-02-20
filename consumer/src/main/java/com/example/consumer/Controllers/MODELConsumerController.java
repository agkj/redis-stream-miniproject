package com.example.consumer.Controllers;

import com.example.consumer.DTO.ConsumerDTO;
import com.example.consumer.Services.ConsumerService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/consumer")
public class MODELConsumerController {

    private final ConsumerService consumerService;
    @GetMapping(path = "/home")
    public String displayGates(Model model){
        consumerService.displayGates(model);
        return "consumer-home";
    }



}
