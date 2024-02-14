package com.example.consumer;

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

        return streamOperations.range(String.valueOf(SharedKeysEnum.USER_STREAM_KEY), Range.closed("0","+"));
    }

    //returns json object of existing gates API
    public List<MapRecord<String ,Object,Object>> retrieveGates(){

        StreamOperations<String, Object,Object> streamOperations = this.redisTemplate.opsForStream();

        return streamOperations.range(String.valueOf(SharedKeysEnum.GATE_STREAM_KEY), Range.closed("0","+"));
    }



    //converts json object from retrieve gates and convert it to model view
    public Model displayGates(Model model){
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8081/consumer/retrieve-gates";
        String jsonData = restTemplate.getForObject(url, String.class);

        // Parse JSON and extract gateNumber and status
        List<Map<String, Object>> gateData = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(jsonData);

        //TODO: Based ON ACCESS LEVEL - TYPE OF KEY, GRANT CERTAIN DISPLAY BASED ON KEYS
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

    //TODO: convert epoch time to timestamp 13/2/24
    public String ConvertEpochTime(Long timestamp){

        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zoneId = ZoneId.systemDefault(); // Use the system default time zone
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = localDateTime.format(formatter);

        return formattedDateTime;
    }


    //TODO: Add access level to different consumers - gate_A can only access gate A, gate_B only gate B
    public String accessControl(ConsumerModel consumerModel){


        return "ok";
    }



}
