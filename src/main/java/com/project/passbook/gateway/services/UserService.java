package com.project.passbook.gateway.services;

import com.project.passbook.gateway.entities.User;

public interface UserService {

  User registerUser(User user);

  User findByName(String username);
}
