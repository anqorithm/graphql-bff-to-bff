package com.example.upstream.redis;

import com.example.upstream.graphql.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
  @Bean
  ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Bean
  RedisTemplate<String, Product> productRedisTemplate(
      RedisConnectionFactory connectionFactory,
      ObjectMapper objectMapper) {
    Jackson2JsonRedisSerializer<Product> serializer =
        new Jackson2JsonRedisSerializer<>(Product.class);
    serializer.setObjectMapper(objectMapper);

    RedisTemplate<String, Product> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(serializer);
    template.setDefaultSerializer(serializer);
    template.afterPropertiesSet();
    return template;
  }
}
