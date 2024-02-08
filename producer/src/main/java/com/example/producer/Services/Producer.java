package com.example.producer.Services;

import com.example.producer.Model.GateModel;
import com.example.producer.SharedKeysEnum;
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
public class Producer {


    private final RedisTemplate<String, Object> redisTemplate;
    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    public void userPublisher(String message){
        //create consumer group
        try {                                             //key, group
            this.redisTemplate.opsForStream().createGroup(String.valueOf(SharedKeysEnum.USER_STREAM_KEY), String.valueOf(SharedKeysEnum.USER_GROUP_KEY));
        }catch (RedisSystemException e){


            if(e.getCause() != null){
                log.info("group exists already! skipping group creation with id: " + SharedKeysEnum.USER_STREAM_KEY);
            }else throw e;
        }

        ObjectRecord<String ,String > record = StreamRecords.newRecord().ofObject(message).withStreamKey(String.valueOf(SharedKeysEnum.USER_STREAM_KEY));
        this.redisTemplate.opsForStream().add(record);

        
        atomicInteger.incrementAndGet();
    }

    //TO TEST THIS SHIT
    public void gatePublisher(GateModel gateModel){
        //create consumer group
        try {                                             //key, group
            this.redisTemplate.opsForStream().createGroup(String.valueOf(SharedKeysEnum.GATE_STREAM_KEY), String.valueOf(SharedKeysEnum.GATE_GROUP_KEY_A));
        }catch (RedisSystemException e){


            if(e.getCause() != null){
                log.info("group exists already! skipping group creation with id:");
            }else throw e;
        }

        ObjectRecord<String , GateModel> record = StreamRecords.newRecord().ofObject(gateModel).withStreamKey(String.valueOf(SharedKeysEnum.GATE_STREAM_KEY));
        this.redisTemplate.opsForStream().add(record);

        atomicInteger.incrementAndGet();
    }

    public void autoDeleteGates(){
        //stream id represents the time it has entered the stream
        // if can use current time to minus the stream entry timestamp, can find out the time the stream has existed.

        StreamOperations<String, Object,Object> streamOperations = this.redisTemplate.opsForStream();

        //get stream size
        Long streamsize = streamOperations.size(String.valueOf(SharedKeysEnum.GATE_STREAM_KEY));
        Long currentTime = System.currentTimeMillis();

        // Delete the stream entries using XTRIM, must have at least 1 entries before delete can be executed
        if(streamsize <=5){
            System.out.println("No entries to delete as at " + System.currentTimeMillis());
        }
        else {

            String bot = "0";
            String top = "+";
            //TODO: Clean up below code
            //retrieve records
            for (int i =0;i<streamsize;i++){

                String streamMessage = streamOperations.range(String.valueOf(SharedKeysEnum.GATE_STREAM_KEY), Range.closed(bot,top)).get(i).getId().getValue();
                Long streamEntryTime = streamOperations.range(String.valueOf(SharedKeysEnum.GATE_STREAM_KEY), Range.closed(bot,top)).get(i).getId().getTimestamp();
                RecordId recordId = streamOperations.range(String.valueOf(SharedKeysEnum.GATE_STREAM_KEY), Range.closed(bot,top)).get(i).getId();

                //delete entries that have lasted for more than 20 sec
                if(currentTime - streamEntryTime >20000 ){
                    //streamOperations.trim(String.valueOf(SharedKeysEnum.STREAM_KEY), minId);
                    streamOperations.delete(String.valueOf(SharedKeysEnum.GATE_STREAM_KEY),recordId);

                    System.out.println("Message: " + streamMessage + " has been deleted");
                    break;
                }

            }

            //streamOperations.trim(String.valueOf(SharedKeysEnum.STREAM_KEY), minId);

        }
    }
    //SINGLE INPUT MESSAGE
    public void autoDeleteUsers(){

        //stream id represents the time it has entered the stream
        // if can use current time to minus the stream entry timestamp, can find out the time the stream has existed.

        StreamOperations<String, Object,Object> streamOperations = this.redisTemplate.opsForStream();

        //get stream size
        Long streamsize = streamOperations.size(String.valueOf(SharedKeysEnum.USER_STREAM_KEY));
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

                String streamMessage = streamOperations.range(String.valueOf(SharedKeysEnum.USER_STREAM_KEY), Range.closed(bot,top)).get(i).getId().getValue();
                Long streamEntryTime = streamOperations.range(String.valueOf(SharedKeysEnum.USER_STREAM_KEY), Range.closed(bot,top)).get(i).getId().getTimestamp();
                RecordId recordId = streamOperations.range(String.valueOf(SharedKeysEnum.USER_STREAM_KEY), Range.closed(bot,top)).get(i).getId();

                //delete entries that have lasted for more than 20 sec
                if(currentTime - streamEntryTime >20000 ){
                    //streamOperations.trim(String.valueOf(SharedKeysEnum.STREAM_KEY), minId);
                    streamOperations.delete(String.valueOf(SharedKeysEnum.USER_STREAM_KEY),recordId);
                    System.out.println("Message: " + streamMessage + " has been deleted");
                    break;
                }

            }

            //streamOperations.trim(String.valueOf(SharedKeysEnum.STREAM_KEY), minId);

        }

    }





}
