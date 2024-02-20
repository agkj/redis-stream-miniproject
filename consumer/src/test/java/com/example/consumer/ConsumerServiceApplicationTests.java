package com.example.consumer;

import com.example.consumer.Model.ConsumerModel;
import com.example.consumer.Repository.ConsumerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
class ConsumerServiceApplicationTests {

	@Autowired
	private ConsumerRepository repo;

	@Test
	public void addNewUser(){
		ConsumerModel consumerModel = new ConsumerModel();
		consumerModel.setPassword("123");
		consumerModel.setUsername("admin_gate_C");
		consumerModel.setAccessLevel("GATE_GROUP_KEY_C");

		ConsumerModel savedUser = repo.save(consumerModel);
		Assertions.assertThat(savedUser).isNotNull();
		Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
	}
	@Test
	public void deleteUser(){

		int userId = 8;
		repo.deleteById(userId);
		Optional<ConsumerModel> optionalUser = repo.findById(userId);
		Assertions.assertThat(optionalUser).isNotPresent();



	}


}
