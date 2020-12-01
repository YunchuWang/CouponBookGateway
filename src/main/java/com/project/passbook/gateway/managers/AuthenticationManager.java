package com.project.passbook.gateway.managers;

import com.project.passbook.gateway.util.JwtTokenUtil;
import java.util.ArrayList;
import java.util.Arrays;
import io.jsonwebtoken.Claims;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

  @Autowired
  JwtTokenUtil jwtTokenUtil;

  @Override
  public Mono<Authentication> authenticate(Authentication authentication) {
    String token = authentication.getCredentials().toString();
    jwtTokenUtil.validateToken(token);
    Function<Claims, String> getPassword = claims -> String.valueOf(claims.get("password"));
    Function<Claims, String> getRole = claims -> {
      List<Map<String, String>> roles = (ArrayList) claims.get("role");
      return roles.get(0).get("authority");
    };
    String role = jwtTokenUtil.getClaimFromToken(token, getRole);
    final UsernamePasswordAuthenticationToken updatedToken = new UsernamePasswordAuthenticationToken(
        jwtTokenUtil.getUsernameFromToken(token), getPassword,
        Arrays.<GrantedAuthority>asList(new SimpleGrantedAuthority(jwtTokenUtil.getClaimFromToken(token, getRole))));
    return Mono.just(updatedToken);
  }
}