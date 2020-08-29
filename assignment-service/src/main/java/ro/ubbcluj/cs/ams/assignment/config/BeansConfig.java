package ro.ubbcluj.cs.ams.assignment.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class BeansConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder WebClientBuilderBean() {

        return WebClient.builder();
    }

    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {

        return new RestTemplate();
    }
}
