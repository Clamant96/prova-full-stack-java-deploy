package br.com.helpconnect.provaFullStackJava.confg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class ProvaFullStackJavaSwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Blog Pessoal")
                        .version("1.0")
                        .description("API do Desafio Full Stack Java e Angular")
                        .contact(new Contact()
                                .name("Kevin Alec Neri Lazzarotto")
                                .url("https://github.com/Clamant96")
                                .email("Desenvolvedor Web Java Full Stack"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org")));
    }
}