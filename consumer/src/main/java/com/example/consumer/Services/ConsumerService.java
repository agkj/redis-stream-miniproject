package com.example.consumer.Services;

import com.example.consumer.DTO.ConsumerDTO;
import com.example.consumer.DTO.LoginDTO;
import com.example.consumer.Enums.GateGroupKeys;
import com.example.consumer.Enums.GateStreamKeys;
import com.example.consumer.Functions.LoginConsumerResponse;
import com.example.consumer.Model.ConsumerModel;
import com.example.consumer.Repository.ConsumerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@RequiredArgsConstructor //reduce boilerplate code, no need to declare constructors/methods for implementation
@Slf4j
public class ConsumerService implements StreamListener<String, ObjectRecord<String,String>> {

    @Autowired
    @Lazy // DONT WANT TO USE THIS, MAY NEED TO FIX
    private RedisTemplate<String, Object> redisTemplate;

    //send and publish to stream, listening
    @Override
    public void onMessage(ObjectRecord<String, String> message) {
        System.out.println(message);

    }

    //returns singular message, not to be used API
    public List<MapRecord<String ,Object,Object>> retrieveUsers(){

        StreamOperations<String, Object,Object> streamOperations = this.redisTemplate.opsForStream();

        return streamOperations.range(String.valueOf(GateStreamKeys.USER_STREAM_KEY), Range.closed("0","+"));
    }

    //returns json object of existing gates API
    public List<MapRecord<String ,Object,Object>> retrieveGates(){

        StreamOperations<String, Object,Object> streamOperations = this.redisTemplate.opsForStream();

        return streamOperations.range(String.valueOf(GateStreamKeys.GATE_STREAM_KEY), Range.closed("0","+"));
    }



    //converts json object from retrieve gates and convert it to model view
    public Model displayGates(Model model){
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8081/consumer/retrieve-gates";
        String jsonData = restTemplate.getForObject(url, String.class);

        // Parse JSON and extract gateNumber and status
        List<Map<String, Object>> gateData = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(jsonData);

        //TODO: Based ON ACCESS LEVEL - TYPE OF KEY, GRANT CERTAIN DISPLAY BASED ON GROUP KEYS
        //

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            JSONObject value = jsonObject.getJSONObject("value");
            String gateNumber = value.getString("gateNumber");
            String gateStatus = value.getString("gateStatus");
            String gateGroup = value.getString("gateGroup");

            //TODO: get timestamp and convert from epoch to datetime
            JSONObject id = jsonObject.getJSONObject("id");
            Long  timestamp  = id.getLong("timestamp");
            String gateTimestamp =  ConvertEpochTime(timestamp);
            System.out.println(gateTimestamp);

            //TODO: Fix serializer if possible to reduce code below
            //QUICK FIX TO CONVERT STRING TO OBJECT TYPE BECAUSE JSON SERIALIZER NOT WORKING

            boolean gateStat = "1".equals(gateStatus);
           // boolean gateHoldBool = "1".equals(gateHold);

            Map<String, Object> gateInfo = new HashMap<>();
            gateInfo.put("gateNumber", gateNumber);
            gateInfo.put("gateStatus", gateStat);
            gateInfo.put("gateTimestamp",gateTimestamp);
           // gateInfo.put("gateHold", gateHoldBool);
            gateInfo.put("gateGroup", gateGroup);
            gateData.add(gateInfo);
        }

        // Pass the data to the HTML template


        return model.addAttribute("gateData", gateData);


    }

    //TODO: convert epoch time to  timestamp 13/2/24 and move to some other class
    public String ConvertEpochTime(Long timestamp){

        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zoneId = ZoneId.systemDefault(); // Use the system default time zone
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = localDateTime.format(formatter);

        return formattedDateTime;
    }

    @Autowired
    private ConsumerRepository consumerRepository;

    private PasswordEncoder passwordEncoder;

    public Boolean addNewConsumer(ConsumerDTO consumerDTO){

        //TODO: implement a check to see if user exist and to allocate group type

        ConsumerModel consumerModel1 = consumerRepository.findByUsername(consumerDTO.getUsername());

        //Group allocation
        if(consumerModel1 == null){
            System.out.println(consumerDTO.getAccessLevel());

            switch (consumerDTO.getAccessLevel()){
                case "A":
                    consumerDTO.setConsumerGroup(String.valueOf(GateGroupKeys.GATE_GROUP_KEY_A));
                    break;
                case "B":
                    consumerDTO.setConsumerGroup(String.valueOf(GateGroupKeys.GATE_GROUP_KEY_B));
                    break;
                case "C":
                    consumerDTO.setConsumerGroup(String.valueOf(GateGroupKeys.GATE_GROUP_KEY_C));
                    break;
                case "MAIN":
                    consumerDTO.setConsumerGroup(String.valueOf(GateGroupKeys.GATE_GROUP_KEY_MAIN));
                default:
                    break;

            }

            ConsumerModel consumerModel = new ConsumerModel(
                    consumerDTO.getId(),
                    consumerDTO.getUsername(),
                    consumerDTO.getPassword(),
                    consumerDTO.getAccessLevel(),
                    consumerDTO.getConsumerGroup()
            );
            consumerRepository.save(consumerModel);
            return true;
        }

        return false;

    }


    public LoginConsumerResponse loginConsumer(LoginDTO loginDTO) {

        ConsumerModel consumerModel1 = consumerRepository.findByUsername(loginDTO.getUsername());

        if(consumerModel1 != null){
            String password = loginDTO.getPassword();
            String encodedPassword = consumerModel1.getPassword();

            String accessLevel = loginDTO.getAccessLevel();


            Boolean passwordCompare = password.equals(encodedPassword);

            if(passwordCompare){
                Optional<ConsumerModel> consumer = consumerRepository.findByUsernameAndPassword(loginDTO.getUsername(), loginDTO.getPassword());
                if(consumer.isPresent()){

                    return new LoginConsumerResponse("Welcome",true);
                }else {
                    return new LoginConsumerResponse("Login failed",false);
                }
            }else {
                return new LoginConsumerResponse("Invalid details, try again",false);
            }

        }
        return new LoginConsumerResponse("User does not exist",false);


    }



}
