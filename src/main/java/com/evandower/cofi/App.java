package com.evandower.cofi;

import java.io.PrintStream;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

public class App {

  private PrintStream out;
  private PrintStream err;

  public App(final PrintStream out, final PrintStream err, final String[] args) {
    this.out = out;
    this.err = err;

    final CmdLineParser parser = new CmdLineParser(this);
    try {
      parser.parseArgument(args);
    } catch (final CmdLineException e) {
      err.println(e.getMessage());
      parser.printUsage(err);
    }
  }

  public String getGreeting() {
    return "Hello world.";
  }

  public void run() {
    out.println(getGreeting());
  }

  public static void main(final String[] args) {
    new App(System.out, System.err, args).run();
  }
}
