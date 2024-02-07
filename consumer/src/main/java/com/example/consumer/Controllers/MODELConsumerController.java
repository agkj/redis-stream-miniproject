package com.example.consumer.Controllers;

import com.example.consumer.Services.Consumer;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/consumer")
public class MODELConsumerController {

    private final Consumer consumer;
    @GetMapping(path = "")
    public String displayGates(Model model){

        consumer.displayGates(model);

        return "consumer-home";
    }



}
