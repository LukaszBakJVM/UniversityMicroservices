package org.example.course;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerClientRequestTransformer;
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Configuration
public class WebConfig {
    @Value("${baseUrl}")
    private String baseUrl;

    @Bean
    public ReactorLoadBalancerExchangeFilterFunction lbFunction(ReactiveLoadBalancer.Factory<ServiceInstance> loadBalancerFactory, List<LoadBalancerClientRequestTransformer> transformers) {
        return new ReactorLoadBalancerExchangeFilterFunction(loadBalancerFactory, transformers);
    }

    @Bean
    public WebClient.Builder webclient(ReactorLoadBalancerExchangeFilterFunction lbFunction) {
        return WebClient.builder().filter(lbFunction).baseUrl(baseUrl);
    }


}
