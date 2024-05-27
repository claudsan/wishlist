package br.com.claudsan.wishlist;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@OpenAPIDefinition
@SpringBootApplication
public class WishlistApplication {

	public static void main(String[] args) {
		SpringApplication.run(WishlistApplication.class, args);
	}

}
