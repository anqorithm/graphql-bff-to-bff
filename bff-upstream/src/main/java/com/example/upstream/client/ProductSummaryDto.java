package com.example.upstream.client;

import java.util.List;

public record ProductSummaryDto(
    String id,
    String name,
    double price,
    double rating,
    boolean available,
    List<String> tags,
    CategorySummaryDto category,
    String upstream) {
}
