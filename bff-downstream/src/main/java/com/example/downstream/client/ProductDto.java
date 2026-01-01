package com.example.downstream.client;

import java.util.List;

public record ProductDto(
    String id,
    String name,
    double price,
    double rating,
    boolean available,
    List<String> tags,
    CategoryDto category
) {}
