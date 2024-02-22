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

    private Integer id; //special id
    private String username; //consumers username
    private String password; //consumers password
    private String accessLevel; //consumers access level
    private String consumerGroup; //depending on access level, allocate the group that it can access
    private Boolean loginAccess; //sets whether the user is currently logged in or not
}
