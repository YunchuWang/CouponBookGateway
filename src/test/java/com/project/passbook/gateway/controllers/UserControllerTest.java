package com.project.passbook.gateway.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import com.project.passbook.gateway.entities.User;
import com.project.passbook.gateway.services.UserService;
import com.project.passbook.gateway.util.JwtTokenUtil;
import com.project.passbook.gateway.utils.TestUtils;
import com.project.passbook.merchantService.model.exceptions.types.ConflictException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class UserControllerTest {

  public static final String TEST_JWT_TOKEN = "testJwtToken";
  private UserController userController;

  @Mock
  private UserService userService;
  @Mock
  private JwtTokenUtil jwtTokenUtil;
  @Mock
  private PasswordEncoder passwordEncoder;

  @BeforeMethod
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    userController = new UserController(userService, jwtTokenUtil, passwordEncoder);
  }

  @Test
  public void givenNewUser_whenRegister_thenSucceed() {
    User expectedUser = TestUtils.createTestUser();
    when(userService.registerUser(expectedUser)).thenReturn(expectedUser);
    User actualUser = userController.registerUser(expectedUser);
    assertEquals(actualUser, expectedUser);
  }

  @Test(expectedExceptions = ConflictException.class)
  public void givenExistingUser_whenRegister_thenThrowException() {
    User expectedUser = TestUtils.createTestUser();
    when(userService.registerUser(expectedUser)).thenThrow(ConflictException.class);

    userController.registerUser(expectedUser);
  }

  @Test(expectedExceptions = UsernameNotFoundException.class)
  public void givenNoUser_whenLogIn_thenThrowException() {
    User expectedUser = TestUtils.createTestUser();
    when(userService.findByName(any())).thenThrow(UsernameNotFoundException.class);

    userController.loginUser(expectedUser);
  }

  @Test
  public void givenWrongPassword_whenLogin_thenReturnUnauthorized() {
    User expectedUser = TestUtils.createTestUser();
    when(userService.findByName(any())).thenReturn(expectedUser);
    when(passwordEncoder.matches(any(), any())).thenReturn(false);

    ResponseEntity responseEntity = userController.loginUser(expectedUser);
    assertEquals(responseEntity.getStatusCode(), HttpStatus.UNAUTHORIZED);
  }

  @Test
  public void givenValidUser_whenLogin_thenSucceed() {
    User expectedUser = TestUtils.createTestUser();

    when(userService.findByName(any())).thenReturn(expectedUser);
    when(passwordEncoder.matches(any(), any())).thenReturn(true);
    when(jwtTokenUtil.generateToken(any())).thenReturn(TEST_JWT_TOKEN);
    ResponseEntity responseEntity = userController.loginUser(expectedUser);

    assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    assertEquals(responseEntity.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0), TEST_JWT_TOKEN);
  }
}