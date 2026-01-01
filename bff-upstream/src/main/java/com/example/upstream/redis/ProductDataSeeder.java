package com.example.upstream.redis;

import com.example.upstream.graphql.Category;
import com.example.upstream.graphql.Product;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ProductDataSeeder implements CommandLineRunner {
  private final ProductRepository productRepository;

  public ProductDataSeeder(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Override
  public void run(String... args) {
    Category stationery = new Category("c-10", "Stationery");
    Category travel = new Category("c-20", "Travel");
    Category tech = new Category("c-30", "Tech");

    List<Product> products = List.of(
        new Product("p-100", "Notebook", 12.50, 4.4, true, List.of("paper", "office"), stationery),
        new Product("p-200", "Pen", 1.75, 4.1, true, List.of("office", "ink"), stationery),
        new Product("p-300", "Backpack", 39.99, 4.6, true, List.of("travel", "carry"), travel),
        new Product("p-400", "Luggage", 129.00, 4.2, false, List.of("travel", "gear"), travel),
        new Product("p-500", "USB-C Hub", 49.50, 4.0, true, List.of("tech", "accessory"), tech),
        new Product("p-600", "Keyboard", 89.00, 4.7, true, List.of("tech", "office"), tech)
    );

    productRepository.saveAll(products);
  }
}
