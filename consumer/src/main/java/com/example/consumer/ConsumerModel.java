package com.example.consumer;


import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Slf4j
@Entity
@Table(name = "users")
public class ConsumerModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 45, nullable = false, name = "username")
    private String username;
    @Column(length = 15,nullable = false)
    private String password;
    @Column(nullable = false)
    private String accessLevel;


}
