package com.example.downstream.graphql;

import java.util.List;

public record CatalogOverview(
    List<ProductSummary> items,
    int totalCount,
    double averagePrice,
    List<CategorySummary> categories
) {}
