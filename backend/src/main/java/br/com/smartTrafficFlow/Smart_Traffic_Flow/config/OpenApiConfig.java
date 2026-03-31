package br.com.smartTrafficFlow.Smart_Traffic_Flow.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI smartTrafficFlowOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Smart Traffic Flow API")
                        .description("API REST para simulacao, consulta e analise de dados de trafego urbano.")
                        .version("v1")
                        .contact(new Contact()
                                .name("Equipe Smart Traffic Flow")
                                .url("https://github.com"))
                        .license(new License()
                                .name("MIT")));
    }
}
