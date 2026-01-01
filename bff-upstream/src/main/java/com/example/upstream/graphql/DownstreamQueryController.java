package com.example.upstream.graphql;

import com.example.upstream.client.CatalogOverviewDto;
import com.example.upstream.client.DownstreamProductClient;
import com.example.upstream.client.ProductSummaryDto;
import java.util.List;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class DownstreamQueryController {
  private final DownstreamProductClient downstreamProductClient;

  public DownstreamQueryController(DownstreamProductClient downstreamProductClient) {
    this.downstreamProductClient = downstreamProductClient;
  }

  @QueryMapping
  public ProductSummaryDto downstreamProductSummary(@Argument String id) {
    return downstreamProductClient.fetchProductSummary(id);
  }

  @QueryMapping
  public CatalogOverviewDto downstreamCatalogOverview(
      @Argument ProductFilter filter,
      @Argument ProductSort sort,
      @Argument PageInput page) {
    CatalogOverviewDto overview = downstreamProductClient.fetchCatalogOverview(filter, sort, page);
    return overview != null ? overview : new CatalogOverviewDto(List.of(), 0, 0.0, List.of());
  }
}
