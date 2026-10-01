package com.api.uprooms;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootTest
class UproomsApplicationTests {

	@BeforeAll
	static void loadEnv() {
		Dotenv.configure().ignoreIfMissing().load().entries().forEach(entry ->
				System.setProperty(entry.getKey(), entry.getValue())
		);
	}

	@Test
	void contextLoads() {
	}

}
