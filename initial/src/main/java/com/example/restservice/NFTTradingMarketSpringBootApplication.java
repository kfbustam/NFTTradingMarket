package com.example.restservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * The type Airline reservation system spring boot application.
 */
@SpringBootApplication
public class NFTTradingMarketSpringBootApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(NFTTradingMarketSpringBootApplication.class);
	}


	/**
	 * The entry point of application.
	 *
	* @param args the input Aiarguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(NFTTradingMarketSpringBootApplication.class, args);
	}

}
