package com.example.upstream.graphql;

import com.example.upstream.redis.ProductRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ProductQueryController {
  private final ProductRepository productRepository;

  public ProductQueryController(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @QueryMapping
  public Product product(@Argument String id) {
    return productRepository.findById(id);
  }

  @QueryMapping
  public List<Product> products() {
    return productRepository.findAll();
  }

  @QueryMapping
  public ProductConnection searchProducts(
      @Argument ProductFilter filter,
      @Argument ProductSort sort,
      @Argument PageInput page) {
    Stream<Product> stream = productRepository.findAll().stream();

    if (filter != null) {
      if (filter.categoryId() != null && !filter.categoryId().isBlank()) {
        stream = stream.filter(p -> p.category().id().equals(filter.categoryId()));
      }
      if (filter.minPrice() != null) {
        stream = stream.filter(p -> p.price() >= filter.minPrice());
      }
      if (filter.maxPrice() != null) {
        stream = stream.filter(p -> p.price() <= filter.maxPrice());
      }
      if (filter.text() != null && !filter.text().isBlank()) {
        String text = filter.text().toLowerCase(Locale.ROOT);
        stream = stream.filter(p ->
            p.name().toLowerCase(Locale.ROOT).contains(text)
                || p.tags().stream().anyMatch(tag -> tag.toLowerCase(Locale.ROOT).contains(text)));
      }
      if (filter.available() != null) {
        stream = stream.filter(p -> p.available() == filter.available());
      }
    }

    List<Product> filtered = stream.toList();

    if (sort != null && sort.field() != null) {
      Comparator<Product> comparator = switch (sort.field()) {
        case NAME -> Comparator.comparing(Product::name, String.CASE_INSENSITIVE_ORDER);
        case PRICE -> Comparator.comparingDouble(Product::price);
        case RATING -> Comparator.comparingDouble(Product::rating);
      };
      if (sort.direction() == SortDirection.DESC) {
        comparator = comparator.reversed();
      }
      filtered = filtered.stream().sorted(comparator).toList();
    }

    int offset = page != null && page.offset() != null ? page.offset() : 0;
    int size = page != null && page.size() != null ? page.size() : 10;
    int totalCount = filtered.size();
    int fromIndex = Math.min(Math.max(offset, 0), totalCount);
    int toIndex = Math.min(fromIndex + Math.max(size, 0), totalCount);

    List<Product> nodes = filtered.subList(fromIndex, toIndex);
    return new ProductConnection(nodes, totalCount);
  }
}
