package dev.hope.vulcaobackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class VulcaoBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(VulcaoBackendApplication.class, args);
	}

}
