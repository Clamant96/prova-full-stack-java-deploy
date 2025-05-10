package br.com.helpconnect.provaFullStackJava.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CepComponent {

	@Value("${app.urlCEP}")
    private String urlCep;

	public String getUrlCep() {
		return urlCep;
	}

}
