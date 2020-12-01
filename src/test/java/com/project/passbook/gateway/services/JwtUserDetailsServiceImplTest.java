package com.project.passbook.gateway.services;

import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.assertEquals;

import com.project.passbook.gateway.entities.User;
import com.project.passbook.gateway.repositories.UserRepository;
import com.project.passbook.gateway.utils.TestUtils;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import reactor.core.publisher.Mono;

public class JwtUserDetailsServiceImplTest {

  private static final String TEST_USER_NAME = "testUserName";
  private JwtUserDetailsServiceImpl jwtUserDetailsService;
  @Mock private UserRepository userRepository;

  @BeforeMethod
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    jwtUserDetailsService = new JwtUserDetailsServiceImpl(userRepository);
  }

  @Test
  public void givenExistentUser_whenFindUserDetails_thenSucceed() {
    User testUser = TestUtils.createTestUser();
    UserDetails expectedUserDetails = org.springframework.security.core.userdetails.User.builder().username(
        testUser.getName()).password(testUser.getPassword()).roles(testUser.getRole()).build();
    when(userRepository.findByName(TEST_USER_NAME)).thenReturn(testUser);
    Mono<UserDetails> userDetailsMono = jwtUserDetailsService.findByUsername(TEST_USER_NAME);

    userDetailsMono.subscribe(userDetails -> {
      assertEquals(userDetails, expectedUserDetails);
    });
  }
}