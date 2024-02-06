package com.example.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/consumer")
//consumer uses port 8081
public class ConsumerController {

    private final Consumer consumer;
    @GetMapping(path = "/streamlist")
    public List<MapRecord<String ,Object,Object>> retrieveUsers(){
        return consumer.retrieveStreams();
        //return "list of users";
    }

}
