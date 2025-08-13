package com.linktic.productos;

import com.linktic.productos.utilities.AppConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;

@SpringBootApplication
public class ProductosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductosApplication.class, args);
	}

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Productos Spring Boot 3 API")
						.version("0.1")
						.description("Aplicación de productos")
						.termsOfService("http://swagger.io/terms/")
						.license(new License().name("Apache 2.0").url("http://springdoc.org")))
				.addSecurityItem(new SecurityRequirement().addList("ApiKeyAuth"))
				.components(new Components()
						.addSecuritySchemes("ApiKeyAuth",
								new SecurityScheme()
										.name("X-API-KEY")
										.type(SecurityScheme.Type.APIKEY)
										.in(SecurityScheme.In.HEADER)
										.description(AppConstants.PRIVATE_KEY)));
	}
}
