package com.example.consumer.Controllers;

import com.example.consumer.DTO.ConsumerDTO;
import com.example.consumer.DTO.LoginDTO;
import com.example.consumer.Functions.LoginConsumerResponse;
import com.example.consumer.Services.ConsumerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
    //TODO: CHECK IF LOGIN ALREADY, IF LOGIN TRUE THEN DISPLAY THE GATES
    @GetMapping(path = "/retrieve-gates")
    public List<MapRecord<String ,Object,Object>> retrieveGates(){

        return consumerService.retrieveGates();
    }


    //ADDS NEW CONSUMER USER
    @PostMapping(path = "/adduser")
    public String addNewConsumer (@RequestBody ConsumerDTO consumerDTO) {

        Boolean newConsumer = consumerService.addNewConsumer(consumerDTO);

        if(newConsumer){
            return "user:"+consumerDTO.getUsername() + " has been added with access level: "+ consumerDTO.getAccessLevel();
        }
        return "Username exists, create a new username";

    }
    //LOGSIN CONSUMER, will set consumer loginAccess
    @PostMapping(path = "/login")
    public ResponseEntity<?> loginConsumer(@RequestBody LoginDTO loginDTO){

        LoginConsumerResponse loginConsumerResponse = consumerService.loginConsumer(loginDTO);
        return ResponseEntity.ok(loginConsumerResponse);
    }



    //Takes the json data from retrieve-gates, and puts it into the template for retrieval from model controller
    // to convert to model view
    // 20/2/24 keeping this code at the moment, currently being used in ConsumerService displaygates class
//    @GetMapping("/jsondata")
//    public String getJSONData() {
//        // Retrieve JSON data from the link
//        RestTemplate restTemplate = new RestTemplate();
//        String jsonData = restTemplate.getForObject("http://localhost:8081/consumer/retrieve-gates", String.class);
//
//        // Return the JSON data
//        return jsonData;
//    }





}
