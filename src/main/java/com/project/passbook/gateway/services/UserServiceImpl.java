package com.project.passbook.gateway.services;

import com.project.passbook.gateway.entities.User;
import com.project.passbook.gateway.repositories.UserRepository;
import com.project.passbook.merchantService.model.exceptions.types.ConflictException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder bCryptPasswordEncoder;

  @Override
  public User registerUser(User user) {
    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    try {
      return userRepository.save(user);
    } catch (Exception e) {
      throw new ConflictException(String.format("User already exists! username %s", user.getName()));
    }
  }

  @Override
  public User findByName(String username) {
    User foundUser = userRepository.findByName(username);
    if (foundUser == null) {
      throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
    }

    return foundUser;
  }
}
