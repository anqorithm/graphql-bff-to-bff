package com.example.downstream.graphql;

import java.util.List;

public record ProductSummary(
    String id,
    String name,
    double price,
    double rating,
    boolean available,
    List<String> tags,
    CategorySummary category,
    String upstream
) {}
