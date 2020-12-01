package com.project.passbook.gateway.configs;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.json.JSONObject;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.authentication.HttpBasicServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@NoArgsConstructor
public class CustomHttpBasicServerAuthenticationEntryPoint extends HttpBasicServerAuthenticationEntryPoint {


  private static final String WWW_AUTHENTICATE = "WWW-Authenticate";
  private static final String DEFAULT_REALM = "Realm";
  private static String WWW_AUTHENTICATE_FORMAT = "Basic realm=\"%s\"";
  private String headerValue = createHeaderValue("Realm");

  public void setRealm(String realm) {
    this.headerValue = createHeaderValue(realm);
  }

  private static String createHeaderValue(@NonNull String realm) {
    return String.format(WWW_AUTHENTICATE_FORMAT, new Object[]{realm});
  }

  @Override
  public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(HttpStatus.UNAUTHORIZED);
    response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
    response.getHeaders().set(HttpHeaders.AUTHORIZATION, this.headerValue);
    JSONObject result = new JSONObject();
    result.append("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
    result.append("message", "Authorization failed!");
    byte[] dataBytes = result.toString().getBytes();
    DataBuffer bodyDataBuffer = response.bufferFactory().wrap(dataBytes);
    return response.writeWith(Mono.just(bodyDataBuffer));
  }
}
