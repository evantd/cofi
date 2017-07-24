package com.evandower.cofi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.ImmutableMap;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class App {

  private final PrintStream out;
  private final PrintStream err;
  private final ObjectMapper jsonMapper =
      new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true);

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

  public void run() throws JsonProcessingException {
    final QuandlWikiClient client = ImmutableQuandlWikiClient.builder().apiKey(apiKey).build();
    final ImmutableMap.Builder<String, SecurityDataSet> symbolStatsBuilder =
        new ImmutableMap.Builder<>();
    for (final String symbol : symbols) {
      symbolStatsBuilder.put(symbol, client.dataSet(symbol, START_DATE, END_DATE));
    }
    final Map<String, SecurityDataSet> symbolStats = symbolStatsBuilder.build();
    switch (op) {
      case MONTHLY_AVG_OPEN_CLOSE:
        printMonthlyAverageOpenClose(symbolStats);
    }
  }

  private void printMonthlyAverageOpenClose(final Map<String, SecurityDataSet> symbolStats)
      throws JsonProcessingException {
    final Map<String, List<SecurityMonthlySummary>> monthlySummaries =
        symbolStats
            .entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().monthlySummaries()));
    out.println(jsonMapper.writeValueAsString(monthlySummaries));
  }

  public static void main(final String[] args) throws JsonProcessingException {
    try {
      new App(System.out, System.err, args).run();
    } catch (final CmdLineException e) {
      System.exit(1);
    }
  }

  private enum Op {
    MONTHLY_AVG_OPEN_CLOSE /*,
                           MAX_DAILY_PROFIT,
                           BUSY_DAY,
                           BIGGEST_LOSER*/
  }
}
