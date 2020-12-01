package com.project.passbook.gateway.controllers;

import com.project.passbook.gateway.entities.User;
import com.project.passbook.gateway.services.UserService;
import com.project.passbook.gateway.util.JwtTokenUtil;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@NoArgsConstructor
@AllArgsConstructor
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  JwtTokenUtil jwtTokenUtil;

  @Autowired
  PasswordEncoder passwordEncoder;

  @PostMapping("/auth/register")
  public User registerUser(@Valid @RequestBody User user) {
    return userService.registerUser(user);
  }

  @PostMapping(value = "/auth/login")
  public ResponseEntity loginUser(@Valid @RequestBody User user) {
    User foundUser = userService.findByName(user.getName());
    if (!passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
      return new ResponseEntity("Unauthorized", HttpStatus.UNAUTHORIZED);
    }

    UserDetails userDetails = org.springframework.security.core.userdetails.User.builder().username(
        foundUser.getName()).password(foundUser.getPassword()).roles(foundUser.getRole()).build();
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.AUTHORIZATION, jwtTokenUtil.generateToken(userDetails));
    return new ResponseEntity(headers, HttpStatus.OK);
  }
}
