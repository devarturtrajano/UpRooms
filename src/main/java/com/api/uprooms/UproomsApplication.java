package com.api.uprooms;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UproomsApplication {

	public static void main(String[] args) {
		// Carrega as variáveis de ambiente a partir do teu ficheiro data.env na raiz do projeto
		Dotenv dotenv = Dotenv.configure()
				.filename("data.env")
				.ignoreIfMissing()
				.load();

		dotenv.entries().forEach(entry ->
				System.setProperty(entry.getKey(), entry.getValue())
		);

		SpringApplication.run(UproomsApplication.class, args);
	}
}