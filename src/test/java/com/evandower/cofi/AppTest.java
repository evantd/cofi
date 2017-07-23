package com.evandower.cofi;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.Test;
import org.kohsuke.args4j.CmdLineException;

public class AppTest {

  @Test
  public void testUsage_noArgs() throws Exception {
    final ByteArrayOutputStream outBaos = new ByteArrayOutputStream();
    final PrintStream outPs = new PrintStream(outBaos, true, UTF_8.name());
    final ByteArrayOutputStream errBaos = new ByteArrayOutputStream();
    final PrintStream errPs = new PrintStream(errBaos, true, UTF_8.name());

    assertThatThrownBy(
            () -> {
              new App(outPs, errPs);
            })
        .isInstanceOf(CmdLineException.class)
        .hasMessageContaining("required");

    final String outContent = new String(outBaos.toByteArray(), UTF_8);
    final String errContent = new String(errBaos.toByteArray(), UTF_8);

    assertThat(outContent).isEmpty();
    assertThat(errContent).contains("required");
  }
}
