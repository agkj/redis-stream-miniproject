package com.example.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/consumer")
//consumerService uses port 8081
public class APIConsumerController {

    private final ConsumerService consumerService;

    //Retrieves singular message
    @GetMapping(path = "/retrieve-users")
    public List<MapRecord<String ,Object,Object>> retrieveUsers(){
        return consumerService.retrieveUsers();
        //return "list of users";
    }
    //Retrieve entire stream of gates
    @GetMapping(path = "/retrieve-gates")
    public List<MapRecord<String ,Object,Object>> retrieveGates(){
        return consumerService.retrieveGates();
        //return "list of users";
    }

    //Retrieves JSON data
    @GetMapping("/jsondata")
    public String getJSONData() {
        // Retrieve JSON data from the link
        RestTemplate restTemplate = new RestTemplate();
        String jsonData = restTemplate.getForObject("http://localhost:8081/consumer/retrieve-gates", String.class);

        // Return the JSON data
        return jsonData;
    }




}
