package com.driver;

import com.driver.model.PaymentMode;
import com.driver.model.SpotType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class ParkPlus {

	public static void main(String[] args) {
		SpringApplication.run(ParkPlus.class, args);


	}

}
