package com.example.producer;

import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/producer")
public class ProducerController {

    @Autowired
    private final Producer producer;


    @PostMapping(value = "/publish" )
    public String streamPublisher(@RequestBody String message){
        producer.userPublisher(message);
        return "message published";
    }

    //TO TEST THIS SHIT
    @PostMapping(value = "/gatepub" )
    public String streamPublisher(@RequestBody GateObject gateObject){
        producer.gatePublisher(gateObject);
        return "gate-pub";
    }



    //executes every second
    //checks the stream for streams that existed for more than 20 sec
    @Scheduled(fixedDelay = 10000)
    public String deleteGates(){
        producer.autoDeleteGates();
        return "deleting gate data";
    }



}
