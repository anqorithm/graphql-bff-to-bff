package com.example.upstream.graphql;

public record ProductFilter(
    String categoryId,
    Double minPrice,
    Double maxPrice,
    String text,
    Boolean available
) {}
