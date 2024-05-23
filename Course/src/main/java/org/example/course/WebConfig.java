package org.example.course;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebConfig {
    @Value("${baseUrl}")
    private String baseUrl;


    @Bean
    public WebClient.Builder webclient() {
        return WebClient.builder().baseUrl(baseUrl);
    }


}
