package com.example.consumer.Controllers;

import com.example.consumer.Services.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/consumer")
//consumer uses port 8081
public class APIConsumerController {

    private final Consumer consumer;

    @GetMapping(path = "/retrieve-users")
    public List<MapRecord<String ,Object,Object>> retrieveUsers(){
        return consumer.retrieveUsers();
        //return "list of users";
    }
    //TO EDIT THIS SHIT
    @GetMapping(path = "/retrieve-gates")
    public List<MapRecord<String ,Object,Object>> retrieveGates(){
        return consumer.retrieveGates();
        //return "list of users";
    }

    @GetMapping("/jsondata")
    public String getJSONData() {
        // Retrieve JSON data from the link
        RestTemplate restTemplate = new RestTemplate();
        String jsonData = restTemplate.getForObject("http://localhost:8081/consumer/retrieve-gates", String.class);

        // Return the JSON data
        return jsonData;
    }




}
