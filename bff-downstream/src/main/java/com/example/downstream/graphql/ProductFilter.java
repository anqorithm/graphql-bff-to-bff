package com.example.downstream.graphql;

public record ProductFilter(
    String categoryId,
    Double minPrice,
    Double maxPrice,
    String text,
    Boolean available
) {}
