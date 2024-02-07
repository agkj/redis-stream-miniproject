package com.example.consumer.Config;

import com.example.consumer.SharedKeysEnum;
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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;


@Configuration
@RequiredArgsConstructor

public class RedisConfigFile {

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
    @Bean
    public Subscription subscription() throws UnknownHostException {
        var options = StreamMessageListenerContainer.
                StreamMessageListenerContainerOptions.
                builder().
                pollTimeout(Duration.ofSeconds(1)).
                targetType(String.class).
                build();

        var listenerContainer = StreamMessageListenerContainer.create(lettuceConnectionFactory(),options);

        //consume message
        var subscription = listenerContainer.
                receiveAutoAck(Consumer.from(String.valueOf(SharedKeysEnum.GATE_GROUP_KEY),InetAddress.getLocalHost().getHostName()),
                StreamOffset.create(String.valueOf(SharedKeysEnum.GATE_STREAM_KEY), ReadOffset.lastConsumed()),
                streamListener);
        listenerContainer.start();
        return subscription;

    }





}

