package com.sbilhbank.insur.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@Slf4j
@RefreshScope
public class WebFluxConfig implements WebFluxConfigurer {
    @Value("${api.url}")
    private String urlAPI = "http://localhost:8080";
    @Value("${api.prefix}")
    private String apiPrefix = "/api";
    @Bean
    public WebClient getWebClient (){

        return WebClient.builder ()
                .baseUrl (urlAPI+apiPrefix)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.set(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE);
                    httpHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
                })
                .build();
    }
}
