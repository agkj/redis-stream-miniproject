package com.example.consumer.Repository;

import com.example.consumer.DTO.LoginDTO;
import com.example.consumer.Model.ConsumerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface ConsumerRepository extends JpaRepository<ConsumerModel, Integer> {


    Optional<ConsumerModel> findByUsernameAndPassword(String username, String password);

    ConsumerModel findByUsername(String username);

    List<ConsumerModel> findAllByUsername(String username);


}
