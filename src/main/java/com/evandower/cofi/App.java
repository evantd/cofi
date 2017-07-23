package com.evandower.cofi;

import java.io.PrintStream;
import java.util.List;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class App {

  private final PrintStream out;
  private final PrintStream err;

  @Option(name = "--op", usage = "select which analysis operation to perform", required = true)
  private Op op;

  @Argument(
    usage = "the stock symbols to analyze",
    metaVar = "<symbol1> <symbol2> ...",
    required = true
  )
  private List<String> symbols;

  public App(final PrintStream out, final PrintStream err, final String... args)
      throws CmdLineException {
    this.out = out;
    this.err = err;

    final CmdLineParser parser = new CmdLineParser(this);
    try {
      parser.parseArgument(args);
    } catch (final CmdLineException e) {
      err.println(e.getMessage());
      err.println();
      err.println("cofi [options] <symbol1> <symbol2> ...");
      err.println();
      parser.printUsage(err);
      throw e;
    }
  }

  public void run() {
    switch (op) {
      case MONTHLY_AVG_OPEN_CLOSE:
        printMonthlyAverageOpenClose();
    }
  }

  private void printMonthlyAverageOpenClose() {
    err.println("Printing monthly average open & close prices is not yet implemented.");
  }

  public static void main(final String[] args) {
    try {
      new App(System.out, System.err, args).run();
    } catch (final CmdLineException e) {
      System.exit(1);
    }
  }

  private enum Op {
    MONTHLY_AVG_OPEN_CLOSE,
    MAX_DAILY_PROFIT,
    BUSY_DAY,
    BIGGEST_LOSER
  }
}
