package com.example.consumer.Controllers;

import com.example.consumer.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Controller
@RequiredArgsConstructor
@RequestMapping("/consumer")
//consumer uses port 8081
public class ConsumerController {

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

}
