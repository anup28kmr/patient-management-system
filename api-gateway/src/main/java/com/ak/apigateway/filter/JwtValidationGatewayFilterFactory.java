package com.ak.apigateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class JwtValidationGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

  private static final Logger log = LoggerFactory.getLogger(JwtValidationGatewayFilterFactory.class);
  private final WebClient webClient;

  public JwtValidationGatewayFilterFactory(WebClient.Builder webClient, @Value("${auth.service.url}") String authServiceUrl) {
    this.webClient = webClient.baseUrl(authServiceUrl).build();
  }


  @Override
  public GatewayFilter apply(Object config) {
    return (exchange, chain) -> {
      String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
      if (ObjectUtils.isEmpty(token) || !token.startsWith("Bearer ")) {
        log.warn("Invalid JWT token: {}", token);
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
      }
      return webClient.get()
          .uri("/validate")
          .header(HttpHeaders.AUTHORIZATION, token)
          .retrieve()
          .toBodilessEntity()
          .then(chain.filter(exchange));
    };
  }
}
