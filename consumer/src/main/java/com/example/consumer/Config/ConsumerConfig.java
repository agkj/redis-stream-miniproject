package com.example.consumer.Config;

import com.example.consumer.Enums.GateGroupKeys;
import com.example.consumer.Enums.GateStreamKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


@Configuration
@RequiredArgsConstructor

public class ConsumerConfig {

    //enables connectivity, configuration properties
    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory(){
        RedisProperties properties = new RedisProperties();
       // RedisProperties properties = properties();
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(properties.getHost());
        config.setPort(properties.getPort());
        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, Object> template(){
        RedisTemplate<String, Object> template = new RedisTemplate<String,Object>();
        template.setConnectionFactory(lettuceConnectionFactory());

        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        //template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
        template.setValueSerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());

        // template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }

//    //defines application properties that we are going to use for our app
//    @Bean
//    public RedisProperties properties(){
//        return new RedisProperties();
//    }




    private final StreamListener<String, ObjectRecord<String ,String>> streamListener;

    //TODO: MODIFY SUBSCRIPTION SO CAN HAVE MULTIPLE GATE GROUPS
//    @Bean
//    public Subscription subscription() throws UnknownHostException {
//        var options = StreamMessageListenerContainer.
//                StreamMessageListenerContainerOptions.
//                builder().
//                pollTimeout(Duration.ofSeconds(1)).
//                targetType(String.class).
//                build();
//
//        var listenerContainer = StreamMessageListenerContainer.create(lettuceConnectionFactory(),options);
//
//        //consume message
//        var subscription = listenerContainer.
//                receiveAutoAck(Consumer.from(String.valueOf(GateStreamKeys.GATE_GROUP_KEY_A),InetAddress.getLocalHost().getHostName()),
//                StreamOffset.create(String.valueOf(GateStreamKeys.GATE_STREAM_KEY), ReadOffset.lastConsumed()),
//                streamListener);
//        listenerContainer.start();
//        return subscription;
//    }


    //TODO: THIS IS A SIMPLE METHOD TO GET A LIST OF SUBSCRIPTION, NEED TO CLEAN UP THE HARDCODED SECTION
    // OF ADDING NEW SUBSCRIPTIONS
    @Bean
    public List<Subscription> subscription() throws UnknownHostException {
        var options = StreamMessageListenerContainer.
                StreamMessageListenerContainerOptions.
                builder().
                pollTimeout(Duration.ofSeconds(1)).
                targetType(String.class).
                build();

        var listenerContainer = StreamMessageListenerContainer.create(lettuceConnectionFactory(),options);

        //consume message
        var subA = listenerContainer.
                receiveAutoAck(Consumer.from(String.valueOf(GateGroupKeys.GATE_GROUP_KEY_A),InetAddress.getLocalHost().getHostName()),
                        StreamOffset.create(String.valueOf(GateStreamKeys.GATE_STREAM_KEY), ReadOffset.lastConsumed()),
                        streamListener);

        var subB = listenerContainer.
                receiveAutoAck(Consumer.from(String.valueOf(GateGroupKeys.GATE_GROUP_KEY_B),InetAddress.getLocalHost().getHostName()),
                        StreamOffset.create(String.valueOf(GateStreamKeys.GATE_STREAM_KEY), ReadOffset.lastConsumed()),
                        streamListener);

        var subC = listenerContainer.
                receiveAutoAck(Consumer.from(String.valueOf(GateGroupKeys.GATE_GROUP_KEY_C),InetAddress.getLocalHost().getHostName()),
                        StreamOffset.create(String.valueOf(GateStreamKeys.GATE_STREAM_KEY), ReadOffset.lastConsumed()),
                        streamListener);

        List<Subscription> subscriptionList = new ArrayList<Subscription>();
        subscriptionList.add(subA);
        subscriptionList.add(subB);
        subscriptionList.add(subC);

        listenerContainer.start();
        return subscriptionList;
    }

















    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}

