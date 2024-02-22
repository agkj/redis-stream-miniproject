package com.example.producer.Controllers;

import com.example.producer.Model.GateModel;
import com.example.producer.Services.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/producer/gateadd")
public class MODELProducerController {


    @Autowired
    private ProducerService producerService;
    @ModelAttribute("gateModel")
    public GateModel gateModel(){
        return new GateModel();
    }

    @GetMapping
    public String producerGates(){
        return "producer-gateadd";
    }

    @PostMapping
    public String produceGates(@ModelAttribute("gateModel") GateModel gateModel){
        producerService.gatePublisher(gateModel);
        return "redirect:/producer/gateadd?success";
    }




}
