package br.com.smartTrafficFlow.Smart_Traffic_Flow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class SmartTrafficFlowApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartTrafficFlowApplication.class, args);
	}

}
