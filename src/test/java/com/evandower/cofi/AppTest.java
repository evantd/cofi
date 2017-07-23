package com.evandower.cofi;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class AppTest {
  @Test
  public void testAppHasAGreeting() {
    App classUnderTest = new App();
    assertThat(classUnderTest.getGreeting()).as("app should have a greeting").isNotNull();
  }
}
