package br.com.helpconnect.provaFullStackJava.confg;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class ProvaFullStackJavaSwaggerConfig {

	@Bean
	OpenAPI springProvaFullStackJavaOpenAPI() {
	    return new OpenAPI()
	        .info(new Info()
	            .title("Desafio Full Stack Tokio")
	            .description("API do Desafio Full Stack Java e Angular")
	            .version("v0.0.1")
	            .contact(new Contact()
	                .name("Kevin Alec Neri Lazzarotto")
	                .url("https://github.com/conteudoGeneration")
	                .email("neri.kevin96@gmail.com")))
	        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
	        .components(new Components().addSecuritySchemes("bearerAuth",
	            new SecurityScheme()
	                .name("Authorization")
	                .type(SecurityScheme.Type.HTTP)
	                .scheme("bearer")
	                .bearerFormat("JWT")));
	}

    @Bean
    OpenApiCustomizer customerGlobalHeaderOpenApiCustomiser() {
        return openApi -> {
            openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
                ApiResponses apiResponses = operation.getResponses();
                apiResponses.addApiResponse("200", createApiResponse("Sucesso!"));
                apiResponses.addApiResponse("201", createApiResponse("Objeto Persistido!"));
                apiResponses.addApiResponse("204", createApiResponse("Objeto Excluído!"));
                apiResponses.addApiResponse("400", createApiResponse("Erro na Requisição!"));
                apiResponses.addApiResponse("401", createApiResponse("Acesso Não Autorizado!"));
                apiResponses.addApiResponse("403", createApiResponse("Acesso Proibido!"));
                apiResponses.addApiResponse("404", createApiResponse("Objeto Não Encontrado!"));
                apiResponses.addApiResponse("500", createApiResponse("Erro na Aplicação!"));
            }));
        };
    }

    private ApiResponse createApiResponse(String message) {
        return new ApiResponse().description(message);
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-api")
                .pathsToMatch("/**")
                .build();
    }
}