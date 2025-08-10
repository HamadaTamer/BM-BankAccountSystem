package com.example.bankapp.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    // Marwa Yehia - DTO Mapping Configuration
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
