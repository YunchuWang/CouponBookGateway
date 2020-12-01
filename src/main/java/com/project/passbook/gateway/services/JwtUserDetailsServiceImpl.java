package com.project.passbook.gateway.services;

import com.project.passbook.gateway.entities.User;
import com.project.passbook.gateway.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service(JwtUserDetailsServiceImpl.USER_DETAILS_SERVICE)
@NoArgsConstructor
@AllArgsConstructor
public class JwtUserDetailsServiceImpl implements ReactiveUserDetailsService {

  public static final String USER_DETAILS_SERVICE = "userDetailsService";
  @Autowired
  private UserRepository userRepository;

  @Override
  public Mono<UserDetails> findByUsername(String username) {
    User user = userRepository.findByName(username);

    if (user == null) {
      throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
    }

    UserDetails userDetails = org.springframework.security.core.userdetails.User.builder().username(
        user.getName()).password(user.getPassword()).roles(user.getRole()).build();
    return Mono.just(userDetails);
  }
}
