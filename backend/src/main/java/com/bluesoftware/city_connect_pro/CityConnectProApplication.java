package com.bluesoftware.city_connect_pro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.bluesoftware.city_connect_pro.config.MercadoPagoProperties;

@SpringBootApplication
@EnableConfigurationProperties(MercadoPagoProperties.class)
public class CityConnectProApplication {

	public static void main(String[] args) {
		SpringApplication.run(CityConnectProApplication.class, args);
	}

}
