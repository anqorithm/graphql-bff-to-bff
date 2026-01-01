package com.example.upstream.graphql;

import java.util.List;

public record ProductConnection(List<Product> nodes, int totalCount) {}
