package com.project.passbook.gateway.repositories;

import com.project.passbook.gateway.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserRepository extends JpaRepository<User, Integer> {

  @Override
  User save(User user);

  User findByName(String userName);
}
