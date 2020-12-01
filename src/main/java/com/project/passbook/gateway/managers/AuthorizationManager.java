package com.project.passbook.gateway.managers;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

  private List<String> authorities;

  @Override
  public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext context) {
    return mono.map(auth -> auth.getAuthorities().stream().filter(e -> authorities.contains(e.getAuthority())).count()
        > 0).map(AuthorizationDecision::new);
  }
}
