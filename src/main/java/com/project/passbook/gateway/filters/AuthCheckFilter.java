package com.project.passbook.gateway.filters;

import com.project.passbook.gateway.filters.AuthCheckFilter.Config;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthCheckFilter extends AbstractGatewayFilterFactory<Config> {

  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) -> {
      log.info("auth check filter");
      String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
      if (token == null || token.isEmpty()) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
      }
      return chain.filter(exchange);
    };
  }

  @Override
  public Config newConfig() {
    return new Config("AuthCheckFilter");
  }

  @Override
  public Class<Config> getConfigClass() {
    return Config.class;
  }

  @Data
  @AllArgsConstructor
  public static class Config {
    private String name;
  }
}