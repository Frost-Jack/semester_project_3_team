package com.example.accessingdata;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AccessingDataApplicationTests {

	@Test
	void TestSettingId() {
		User logger = new User();
		logger.setId(1);
		logger.getId();
	}
	@Test
	void TestSettingFirstName() {
		User logger = new User();
		logger.setFirstName("Y");
		logger.getId();
	}
}

