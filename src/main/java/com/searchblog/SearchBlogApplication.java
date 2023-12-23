package com.searchblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SearchBlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchBlogApplication.class, args);
	}

}
