package com.example.consumer.DTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class LoginDTO {

    //this is only called when users are going to log in
    private String username; //gets consumer username
    private String password; //gets consumer password
    private String accessLevel; //gets consumer access level
    private String consumerGroup; //gets consumer group
    private Boolean loginAccess; //gets logged in status


}
