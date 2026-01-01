package com.example.upstream.redis;

import com.example.upstream.graphql.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {
  private static final String KEY_PREFIX = "product:";
  private final RedisTemplate<String, Product> redisTemplate;

  public ProductRepository(RedisTemplate<String, Product> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  public void saveAll(List<Product> products) {
    for (Product product : products) {
      redisTemplate.opsForValue().set(key(product.id()), product);
    }
  }

  public Product findById(String id) {
    return redisTemplate.opsForValue().get(key(id));
  }

  public List<Product> findAll() {
    Set<String> keys = redisTemplate.keys(KEY_PREFIX + "*");
    if (keys == null || keys.isEmpty()) {
      return List.of();
    }
    List<Product> values = redisTemplate.opsForValue().multiGet(keys);
    if (values == null) {
      return List.of();
    }
    List<Product> results = new ArrayList<>();
    for (Product product : values) {
      if (product != null) {
        results.add(product);
      }
    }
    results.sort((a, b) -> a.id().compareToIgnoreCase(b.id()));
    return results;
  }

  private String key(String id) {
    return KEY_PREFIX + Objects.requireNonNull(id, "id");
  }
}
