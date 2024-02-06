package com.example.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    //returns json object of exisiting streams
    public List<MapRecord<String ,Object,Object>> retrieveStreams(){

        StreamOperations<String, Object,Object> streamOperations = this.redisTemplate.opsForStream();
        return streamOperations.range(String.valueOf(SharedKeysEnum.STREAM_KEY), Range.closed("0","+"));
    }



}
