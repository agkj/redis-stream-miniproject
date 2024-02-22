package com.example.producer.Controllers;

import com.example.producer.Model.GateModel;
import com.example.producer.Services.ProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/producer")
public class APIProducerController {

    @Autowired
    private final ProducerService producerService;


    @PostMapping(value = "/userpub" )
    public String streamPublisher(@RequestBody String message){
        producerService.userPublisher(message);
        return "message published";
    }

    //TO TEST THIS SHIT
    @PostMapping(value = "/gatepub" )
    public String streamPublisher(@RequestBody GateModel gateModel){
        producerService.gatePublisher(gateModel);
        return "gate-pub";
    }




    //checks the stream for streams that existed for more than 10 sec
    @Scheduled(fixedDelay = 10000)
    public String deleteGates(){
        producerService.autoDeleteGates();
        return "deleting gate data";
    }



}
