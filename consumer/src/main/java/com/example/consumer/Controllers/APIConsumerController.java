package com.example.consumer.Controllers;

import com.example.consumer.DTO.ConsumerDTO;
import com.example.consumer.DTO.LoginDTO;
import com.example.consumer.Functions.LoginResponse;
import com.example.consumer.Model.ConsumerModel;
import com.example.consumer.Repository.ConsumerRepository;
import com.example.consumer.Services.ConsumerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping(path = "/adduser")
    public String addUser(@RequestBody ConsumerDTO consumerDTO){
        Boolean newConsumer = consumerService.addNewConsumer(consumerDTO);

        if(newConsumer){
            return "user: has been added with accesslevel: "+ consumerDTO.getAccessLevel();
        }else {
            return "user exists, create a new username";
        }


    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> loginConsumer(@RequestBody LoginDTO loginDTO){

        LoginResponse loginResponse = consumerService.loginUser(loginDTO);
        return ResponseEntity.ok(loginResponse);
    }

}
