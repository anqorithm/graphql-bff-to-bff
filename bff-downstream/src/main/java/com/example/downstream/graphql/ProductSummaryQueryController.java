package com.example.downstream.graphql;

import com.example.downstream.client.ProductConnectionDto;
import com.example.downstream.client.ProductDto;
import com.example.downstream.client.UpstreamProductClient;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ProductSummaryQueryController {
  private final UpstreamProductClient upstreamProductClient;

  public ProductSummaryQueryController(UpstreamProductClient upstreamProductClient) {
    this.upstreamProductClient = upstreamProductClient;
  }

  @QueryMapping
  public ProductSummary productSummary(@Argument String id) {
    ProductDto product = upstreamProductClient.fetchProduct(id);
    if (product == null) {
      return null;
    }
    CategorySummary category = new CategorySummary(
        product.category().id(), product.category().name(), 1);
    return new ProductSummary(
        product.id(),
        product.name(),
        product.price(),
        product.rating(),
        product.available(),
        product.tags(),
        category,
        "upstream-bff");
  }

  @QueryMapping
  public CatalogOverview catalogOverview(
      @Argument ProductFilter filter,
      @Argument ProductSort sort,
      @Argument PageInput page) {
    ProductConnectionDto connection = upstreamProductClient.searchProducts(filter, sort, page);
    if (connection == null || connection.nodes() == null) {
      return new CatalogOverview(List.of(), 0, 0.0, List.of());
    }

    List<ProductSummary> items = new ArrayList<>();
    Map<String, CategorySummary> categoryMap = new LinkedHashMap<>();
    DoubleSummaryStatistics priceStats = new DoubleSummaryStatistics();

    for (ProductDto product : connection.nodes()) {
      CategorySummary category = categoryMap.compute(product.category().id(), (key, existing) -> {
        if (existing == null) {
          return new CategorySummary(product.category().id(), product.category().name(), 1);
        }
        return new CategorySummary(existing.id(), existing.name(), existing.productCount() + 1);
      });

      items.add(new ProductSummary(
          product.id(),
          product.name(),
          product.price(),
          product.rating(),
          product.available(),
          product.tags(),
          category,
          "upstream-bff"));
      priceStats.accept(product.price());
    }

    double averagePrice = items.isEmpty() ? 0.0 : priceStats.getAverage();

    return new CatalogOverview(
        items,
        connection.totalCount(),
        averagePrice,
        List.copyOf(categoryMap.values()));
  }
}
