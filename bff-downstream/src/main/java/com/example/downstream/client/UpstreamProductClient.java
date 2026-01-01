package com.example.downstream.client;

import com.example.downstream.graphql.PageInput;
import com.example.downstream.graphql.ProductFilter;
import com.example.downstream.graphql.ProductSort;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Service;

@Service
public class UpstreamProductClient {
  private final HttpGraphQlClient graphQlClient;

  public UpstreamProductClient(HttpGraphQlClient graphQlClient) {
    this.graphQlClient = graphQlClient;
  }

  public ProductDto fetchProduct(String id) {
    return graphQlClient
        .document("query($id: ID!) { product(id: $id) { id name price rating available tags category { id name } } }")
        .variable("id", id)
        .retrieve("product")
        .toEntity(ProductDto.class)
        .block();
  }

  public ProductConnectionDto searchProducts(ProductFilter filter, ProductSort sort, PageInput page) {
    return graphQlClient
        .document("""
            query($filter: ProductFilter, $sort: ProductSort, $page: PageInput) {
              searchProducts(filter: $filter, sort: $sort, page: $page) {
                totalCount
                nodes { id name price rating available tags category { id name } }
              }
            }
            """)
        .variable("filter", filter)
        .variable("sort", sort)
        .variable("page", page)
        .retrieve("searchProducts")
        .toEntity(ProductConnectionDto.class)
        .block();
  }
}
