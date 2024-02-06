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


    @PostMapping(value = "/publishers" )
    public String streamPublisher(@RequestBody String message){
        producer.streamPublisher(message);
        return "message published";
    }

    //executes every second
    //checks the stream for streams that existed for more than 20 sec
    @Scheduled(fixedDelay = 1000)
    public String deleteUsers(){
        producer.autoDeleteUsers();
        return "auto deleting users";
    }



}
