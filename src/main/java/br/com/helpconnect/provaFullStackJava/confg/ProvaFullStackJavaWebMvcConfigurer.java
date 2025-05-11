package br.com.helpconnect.provaFullStackJava.confg;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configurable
public class ProvaFullStackJavaWebMvcConfigurer implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	registry.addResourceHandler("/**/*")
        	.addResourceLocations("classpath:/public/")
        	.resourceChain(true)
        	.addResolver(new PathResourceResolver() {
            	@Override
            	protected Resource getResource(String resourcePath, Resource location) throws IOException {
                	Resource requestedResource = location.createRelative(resourcePath);
                	return requestedResource.exists() && requestedResource.isReadable() ? requestedResource : new ClassPathResource("/public/index.html");
            		}
        		});
		}
}
