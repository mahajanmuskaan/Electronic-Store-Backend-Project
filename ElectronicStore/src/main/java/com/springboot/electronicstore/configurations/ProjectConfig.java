package com.springboot.electronicstore.configurations;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Configuration class for project-specific configurations
@Configuration
public class ProjectConfig {

	// Bean definition for ModelMapper instance
	@Bean
	public ModelMapper mapper() {
		return new ModelMapper();
	}

}
