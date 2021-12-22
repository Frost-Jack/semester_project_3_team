package com.example.accessingdata;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AccessingDataApplicationTests {

	@Autowired
	private MainController myController;

	@Test
	public void contextLoads() throws Exception {
		Assertions.assertThat(myController).isNotNull();
	}

}

