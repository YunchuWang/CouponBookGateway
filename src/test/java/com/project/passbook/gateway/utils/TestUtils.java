package com.project.passbook.gateway.utils;

import com.project.passbook.gateway.entities.User;
import org.apache.commons.lang.RandomStringUtils;

public class TestUtils {

  public static User createTestUser() {
    return User.builder().name(RandomStringUtils.randomAlphabetic(12)).password(
        RandomStringUtils.randomAlphanumeric(20)).role(RandomStringUtils.randomAlphanumeric(12)).build();
  }
}
