package br.com.helpconnect.provaFullStackJava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("br.com.helpconnect.provaFullStackJava.model")
@EnableJpaRepositories("br.com.helpconnect.provaFullStackJava.repository")
public class ProvaFullStackJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProvaFullStackJavaApplication.class, args);
	}

}
