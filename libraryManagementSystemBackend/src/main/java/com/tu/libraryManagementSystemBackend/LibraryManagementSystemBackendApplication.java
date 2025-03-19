package com.tu.libraryManagementSystemBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LibraryManagementSystemBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryManagementSystemBackendApplication.class, args);
	}

}
