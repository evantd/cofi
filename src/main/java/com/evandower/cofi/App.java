package com.evandower.cofi;

import com.google.common.collect.ImmutableMap;
import com.jimmoores.quandl.QuandlSession;
import com.jimmoores.quandl.RetryPolicy;
import com.jimmoores.quandl.SessionOptions;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class App {

  private final PrintStream out;
  private final PrintStream err;

  private static final LocalDate START_DATE = LocalDate.of(2017, Month.JANUARY, 1);
  private static final LocalDate END_DATE = LocalDate.of(2017, Month.JUNE, 30);

  @Option(
    name = "--api-key",
    usage = "the API key to use in requests to the Quandl API",
    required = true
  )
  private String apiKey;

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
    final QuandlSession session =
        QuandlSession.create(
            SessionOptions.Builder.withAuthToken(apiKey)
                .withRetryPolicy(RetryPolicy.createFixedRetryPolicy(3, 10))
                .build());
    final ImmutableMap.Builder<String, ImmutableSecurityDataSet> symbolStatsBuilder =
        new ImmutableMap.Builder<>();
    for (final String symbol : symbols) {
      symbolStatsBuilder.put(
          symbol,
          ImmutableSecurityDataSet.builder()
              .session(session)
              .symbol(symbol)
              .startDate(START_DATE)
              .endDate(END_DATE)
              .build());
    }
    final ImmutableMap<String, ImmutableSecurityDataSet> symbolStats = symbolStatsBuilder.build();
    switch (op) {
      case MONTHLY_AVG_OPEN_CLOSE:
        printMonthlyAverageOpenClose(symbolStats);
    }
  }

  private void printMonthlyAverageOpenClose(
      final ImmutableMap<String, ImmutableSecurityDataSet> symbolStats) {
    out.println(symbolStats);
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
