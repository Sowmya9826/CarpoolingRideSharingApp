package com.carpoolapp.carpoolService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CarpoolRideSharingServiceApplicationTests {

	@Test
	void contextLoads() {
		CarpoolRideSharingServiceApplication application = new CarpoolRideSharingServiceApplication();
		assertNotNull(application);
	}
}