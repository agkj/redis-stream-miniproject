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

    private String username;
    private String password;
    private String accessLevel;


}
