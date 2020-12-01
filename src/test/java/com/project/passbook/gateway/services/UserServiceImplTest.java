package com.project.passbook.gateway.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.project.passbook.gateway.entities.User;
import com.project.passbook.gateway.repositories.UserRepository;
import com.project.passbook.gateway.utils.TestUtils;
import com.project.passbook.merchantService.model.exceptions.types.ConflictException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class UserServiceImplTest {

  private UserServiceImpl userServiceImpl;

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @BeforeMethod
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    userServiceImpl = new UserServiceImpl(userRepository, passwordEncoder);
  }

  @Test
  public void givenExistingUser_whenRegister_thenSucceed() {
    User expectedUser = TestUtils.createTestUser();
    when(passwordEncoder.encode(any())).thenReturn(expectedUser.getPassword());
    when(userRepository.save(any())).thenReturn(expectedUser);

    User actualUser = userServiceImpl.registerUser(expectedUser);

    assertEquals(actualUser, expectedUser);
  }

  @Test(expectedExceptions = ConflictException.class)
  public void givenNewUser_whenRegister_thenThrowException() {
    User expectedUser = TestUtils.createTestUser();
    when(passwordEncoder.encode(any())).thenReturn(expectedUser.getPassword());
    when(userRepository.save(any())).thenThrow(ConflictException.class);

    userServiceImpl.registerUser(expectedUser);
  }

  @Test
  public void givenExistingUser_whenFindUserByName_thenSucceed() {
    User expectedUser = TestUtils.createTestUser();
    when(userRepository.findByName(any())).thenReturn(expectedUser);

    User actualUser = userServiceImpl.findByName(expectedUser.getName());

    assertEquals(actualUser, expectedUser);
  }

  @Test(expectedExceptions = UsernameNotFoundException.class)
  public void givenUserNotExists_whenFindUserByName_thenThrowException() {
    when(userRepository.findByName(any())).thenThrow(UsernameNotFoundException.class);

    userServiceImpl.findByName(null);
  }
}