package com.example.downstream.client;

import java.util.List;

public record ProductConnectionDto(List<ProductDto> nodes, int totalCount) {}
