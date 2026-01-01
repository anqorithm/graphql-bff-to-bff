package com.example.upstream.client;

import com.example.upstream.graphql.PageInput;
import com.example.upstream.graphql.ProductFilter;
import com.example.upstream.graphql.ProductSort;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Service;

@Service
public class DownstreamProductClient {
  private final HttpGraphQlClient graphQlClient;

  public DownstreamProductClient(HttpGraphQlClient graphQlClient) {
    this.graphQlClient = graphQlClient;
  }

  public ProductSummaryDto fetchProductSummary(String id) {
    return graphQlClient
        .document("query($id: ID!) { productSummary(id: $id) { id name price rating available tags category { id name productCount } upstream } }")
        .variable("id", id)
        .retrieve("productSummary")
        .toEntity(ProductSummaryDto.class)
        .block();
  }

  public CatalogOverviewDto fetchCatalogOverview(ProductFilter filter, ProductSort sort, PageInput page) {
    return graphQlClient
        .document("""
            query($filter: ProductFilter, $sort: ProductSort, $page: PageInput) {
              catalogOverview(filter: $filter, sort: $sort, page: $page) {
                totalCount
                averagePrice
                categories { id name productCount }
                items {
                  id
                  name
                  price
                  rating
                  available
                  tags
                  category { id name productCount }
                  upstream
                }
              }
            }
            """)
        .variable("filter", filter)
        .variable("sort", sort)
        .variable("page", page)
        .retrieve("catalogOverview")
        .toEntity(CatalogOverviewDto.class)
        .block();
  }
}
