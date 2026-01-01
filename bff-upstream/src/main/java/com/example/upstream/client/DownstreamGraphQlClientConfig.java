package com.example.upstream.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class DownstreamGraphQlClientConfig {
  @Bean
  HttpGraphQlClient downstreamGraphQlClient(
      @Value("${downstream.graphql.url}") String downstreamUrl) {
    WebClient webClient = WebClient.builder().baseUrl(downstreamUrl).build();
    return HttpGraphQlClient.builder(webClient).build();
  }
}
