package com.example.upstream.client;

import java.util.List;

public record CatalogOverviewDto(
    List<ProductSummaryDto> items,
    int totalCount,
    double averagePrice,
    List<CategorySummaryDto> categories) {
}
