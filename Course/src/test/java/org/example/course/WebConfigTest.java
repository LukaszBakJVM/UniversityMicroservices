package org.example.course;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebConfigTest {
    @Value("${baseUrl}")
    private String baseUrl;

    @Bean
    @Primary
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder().baseUrl(baseUrl);
    }


}
