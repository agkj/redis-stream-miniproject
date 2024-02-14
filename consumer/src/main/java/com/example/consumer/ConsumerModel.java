package com.example.consumer;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Entity
@NoArgsConstructor
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
