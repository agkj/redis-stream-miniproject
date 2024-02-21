package com.example.producer.Services;

import com.example.producer.Enums.GateGroupKeys;
import com.example.producer.Enums.GateStreamKeys;
import com.example.producer.Model.GateModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor //reduce boilerplate code, no need to declare constructors/methods for implementation
@Slf4j
public class ProducerService {


    private final RedisTemplate<String, Object> redisTemplate;
    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    public void userPublisher(String message){
        //create consumer group
        try {                                             //key, group
            this.redisTemplate.opsForStream().createGroup(String.valueOf(GateStreamKeys.USER_STREAM_KEY), String.valueOf(GateStreamKeys.USER_GROUP_KEY));
        }catch (RedisSystemException e){


            if(e.getCause() != null){
                log.info("group exists already! skipping group creation with id: " + GateStreamKeys.USER_STREAM_KEY);
            }else throw e;
        }

        ObjectRecord<String ,String > record = StreamRecords.newRecord().ofObject(message).withStreamKey(String.valueOf(GateStreamKeys.USER_STREAM_KEY));
        this.redisTemplate.opsForStream().add(record);

        
        atomicInteger.incrementAndGet();
    }

    //Adds a new stream entry to the stream
    public void gatePublisher(GateModel gateModel){
        //create consumer group
        try {

            switch (gateModel.getGateGroup()){

                case "A":
                    this.redisTemplate.opsForStream().createGroup(String.valueOf(GateStreamKeys.GATE_STREAM_KEY), String.valueOf(GateGroupKeys.GATE_GROUP_KEY_A));
                    break;
                case "B":
                    this.redisTemplate.opsForStream().createGroup(String.valueOf(GateStreamKeys.GATE_STREAM_KEY), String.valueOf(GateGroupKeys.GATE_GROUP_KEY_B));
                    break;
                case "C":
                    this.redisTemplate.opsForStream().createGroup(String.valueOf(GateStreamKeys.GATE_STREAM_KEY), String.valueOf(GateGroupKeys.GATE_GROUP_KEY_C));
                case "MAIN":
                    this.redisTemplate.opsForStream().createGroup(String.valueOf(GateStreamKeys.GATE_STREAM_KEY), String.valueOf(GateGroupKeys.GATE_GROUP_KEY_MAIN));
                    break;
                default:
                    break;

            }

        }catch (RedisSystemException e){


            if(e.getCause() != null){
                log.info("group exists already! skipping group creation with id:");
            }else throw e;
        }

        ObjectRecord<String , GateModel> record = StreamRecords.newRecord().ofObject(gateModel).withStreamKey(String.valueOf(GateStreamKeys.GATE_STREAM_KEY));

        this.redisTemplate.opsForStream().add(record);

        atomicInteger.incrementAndGet();
    }


    public void autoDeleteGates(){
        //stream id represents the time it has entered the stream
        // if can use current time to minus the stream entry timestamp, can find out the time the stream has existed.


        StreamOperations<String, Object,Object> streamOperations = this.redisTemplate.opsForStream();

        //get stream size
        Long streamsize = streamOperations.size(String.valueOf(GateStreamKeys.GATE_STREAM_KEY));
        //get current time
        Long currentTime = System.currentTimeMillis();

        // Delete the stream entries using XTRIM, must have at least 5 entries before delete can be executed
        if(streamsize <=5 ){
            System.out.println("No entries to delete as at " + System.currentTimeMillis());
        }
        else {

            String bot = "0";
            String top = "+";
            //TODO: Clean up below code
            //retrieve records
            for (int i =0;i<streamsize;i++){


                String streamMessage = streamOperations.range(String.valueOf(GateStreamKeys.GATE_STREAM_KEY), Range.closed(bot,top)).get(i).getId().getValue();
                Long streamEntryTime = streamOperations.range(String.valueOf(GateStreamKeys.GATE_STREAM_KEY), Range.closed(bot,top)).get(i).getId().getTimestamp();
                RecordId recordId = streamOperations.range(String.valueOf(GateStreamKeys.GATE_STREAM_KEY), Range.closed(bot,top)).get(i).getId();

                //if gate hold checkbox is checked, prevent deletion

               //TODO: SERIALIZER HAS ISSUES, THIS IS A SIMPLE FIX REFER TO ConsumerService Class for implementation





                //need to get model attribute

                //delete entries
                if(currentTime - streamEntryTime >20000){
                    //streamOperations.trim(String.valueOf(GateStreamKeys.STREAM_KEY), minId);
                    streamOperations.delete(String.valueOf(GateStreamKeys.GATE_STREAM_KEY),recordId);

                    System.out.println("Message: " + streamMessage + " has been deleted");
                    break;
                }

            }

            //streamOperations.trim(String.valueOf(GateStreamKeys.STREAM_KEY), minId);

        }
    }
    //SINGLE INPUT MESSAGE
    public void autoDeleteUsers(){

        //stream id represents the time it has entered the stream
        // if can use current time to minus the stream entry timestamp, can find out the time the stream has existed.

        StreamOperations<String, Object,Object> streamOperations = this.redisTemplate.opsForStream();

        //get stream size
        Long streamsize = streamOperations.size(String.valueOf(GateStreamKeys.USER_STREAM_KEY));
        Long currentTime = System.currentTimeMillis();

        // Delete the stream entries using XTRIM, must have at least 1 entries before delete can be executed
        if(streamsize <=5){
            System.out.println("No entries to delete as at " + System.currentTimeMillis());
        }
        else {

            String bot = "0";
            String top = "+";

            //retrieve records
            for (int i =0;i<streamsize;i++){

                String streamMessage = streamOperations.range(String.valueOf(GateStreamKeys.USER_STREAM_KEY), Range.closed(bot,top)).get(i).getId().getValue();
                Long streamEntryTime = streamOperations.range(String.valueOf(GateStreamKeys.USER_STREAM_KEY), Range.closed(bot,top)).get(i).getId().getTimestamp();
                RecordId recordId = streamOperations.range(String.valueOf(GateStreamKeys.USER_STREAM_KEY), Range.closed(bot,top)).get(i).getId();

                //delete entries that have lasted for more than 20 sec
                if(currentTime - streamEntryTime >20000 ){
                    //streamOperations.trim(String.valueOf(GateStreamKeys.STREAM_KEY), minId);
                    streamOperations.delete(String.valueOf(GateStreamKeys.USER_STREAM_KEY),recordId);
                    System.out.println("Message: " + streamMessage + " has been deleted");
                    break;
                }

            }

            //streamOperations.trim(String.valueOf(GateStreamKeys.STREAM_KEY), minId);

        }

    }





}
