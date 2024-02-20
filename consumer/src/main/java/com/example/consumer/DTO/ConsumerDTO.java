package com.example.consumer.DTO;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ConsumerDTO {

    private Integer id;
    private String username;
    private String password;
    private String accessLevel;
    private String consumerGroup;
}
