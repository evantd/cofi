package com.evandower.cofi;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AppTest {
    @Test
    public void testAppHasAGreeting() {
        App classUnderTest = new App();
        assertThat(classUnderTest.getGreeting()).as("app should have a greeting").isNotNull();
    }
}
