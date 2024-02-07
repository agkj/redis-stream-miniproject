package com.example.producer.DAO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.redis.core.RedisHash;

@NoArgsConstructor
@Slf4j
@Data

public class GateObject {

    private int gateNumber;
    private boolean status;

}
