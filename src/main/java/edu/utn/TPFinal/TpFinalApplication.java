package edu.utn.TPFinal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"edu.utn.TPFinal.repository"})
public class TpFinalApplication {

	public static void main(String[] args) {
		SpringApplication.run(TpFinalApplication.class, args);
	}

}
