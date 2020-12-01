package com.project.passbook.gateway.configs;

import com.project.passbook.gateway.managers.AuthorizationManager;
import com.project.passbook.gateway.repositories.SecurityContextRepository;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class SecurityConfig {

  //security的鉴权排除的url列表
  private static final String[] excludedAuthPages = {
      "/auth/register",
      "/auth/login",
      "/auth/logout",
      "/health",
      "/api/socket/**"
  };

  private static final String[] ROLES = {
      "ROLE_merchant",
      "ROLE_admin"
  };

  @Autowired
  private CustomHttpBasicServerAuthenticationEntryPoint authenticationEntryPoint;

  @Autowired
  private SecurityContextRepository securityContextRepository;

  @Bean
  SecurityWebFilterChain webFluxSecurityFilterChain(ServerHttpSecurity http) {
    http
        .authorizeExchange()
        .pathMatchers(excludedAuthPages).permitAll()  //无需进行权限过滤的请求路径
        .pathMatchers(new String[]{"/merchants/**", "/coupontemplate"}).access(
        new AuthorizationManager(Arrays.asList(ROLES)))
        .pathMatchers(HttpMethod.OPTIONS).permitAll() //option 请求默认放行
        .anyExchange().authenticated()
        .and()
        .httpBasic().disable()
        .securityContextRepository(securityContextRepository)
        .formLogin().disable()
        .csrf().disable()//必须支持跨域
        .exceptionHandling()
        .authenticationEntryPoint(authenticationEntryPoint) // customize response when auth failed
        .and()
        .logout().disable();

    return http.build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
