package br.com.helpconnect.provaFullStackJava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProvaFullStackJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProvaFullStackJavaApplication.class, args);
		System.out.println("======== APLICAÇÃO INICIADA COM SUCESSO ========");
		logMemoryStats();
	}

	private static void logMemoryStats() {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();

        System.out.println("[MEMORY] Max: " + (maxMemory / 1024 / 1024) + " MB");
        System.out.println("[MEMORY] Allocated: " + (allocatedMemory / 1024 / 1024) + " MB");
        System.out.println("[MEMORY] Free: " + (freeMemory / 1024 / 1024) + " MB");
    }

}
