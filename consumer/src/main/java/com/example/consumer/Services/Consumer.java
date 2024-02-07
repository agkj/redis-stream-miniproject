package com.example.consumer.Services;

import com.example.consumer.SharedKeysEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamInfo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor //reduce boilerplate code, no need to declare constructors/methods for implementation
@Slf4j
public class Consumer implements StreamListener<String, ObjectRecord<String,String>> {

    @Autowired
    @Lazy // DONT WANT TO USE THIS, MAY NEED TO FIX
    private RedisTemplate<String, Object> redisTemplate;

    //send and publish to stream, listening
    @Override
    public void onMessage(ObjectRecord<String, String> message) {
        System.out.println(message);

    }

    //returns json object of existing streams
    public List<MapRecord<String ,Object,Object>> retrieveUsers(){

        StreamOperations<String, Object,Object> streamOperations = this.redisTemplate.opsForStream();

        return streamOperations.range(String.valueOf(SharedKeysEnum.USER_STREAM_KEY), Range.closed("0","+"));
    }

    //returns json object of existing gates
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
        List<Map<String, String>> gateData = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(jsonData);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            JSONObject value = jsonObject.getJSONObject("value");
            String gateNumber = value.getString("gateNumber");
            String gateStatus = value.getString("gateStatus");
            Map<String, String> gateInfo = new HashMap<>();
            gateInfo.put("gateNumber", gateNumber);
            gateInfo.put("gateStatus", gateStatus);
            gateData.add(gateInfo);
        }

        // Pass the data to the HTML template
         return model.addAttribute("gateData", gateData);


    }




}
