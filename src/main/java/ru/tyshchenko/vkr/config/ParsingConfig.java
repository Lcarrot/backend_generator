package ru.tyshchenko.vkr.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParsingConfig {

    @Bean
     ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
