package com.example.upstream.graphql;

import java.util.List;

public record Product(
    String id,
    String name,
    double price,
    double rating,
    boolean available,
    List<String> tags,
    Category category
) {}
