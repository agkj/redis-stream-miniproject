package com.example.producer;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.redis.core.RedisHash;

@NoArgsConstructor
@Slf4j
@Data
@TypeAlias("Gate Object") // used to identify the object

public class GateObject {

    private String gateName;
    private boolean status;

}
