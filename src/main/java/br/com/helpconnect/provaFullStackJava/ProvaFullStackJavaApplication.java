package br.com.helpconnect.provaFullStackJava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("br.com.helpconnect.provaFullStackJava.model")
public class ProvaFullStackJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProvaFullStackJavaApplication.class, args);
	}

}
