package com.evandower.cofi;

import com.google.common.collect.ImmutableList;
import com.jimmoores.quandl.DataSetRequest;
import com.jimmoores.quandl.QuandlSession;
import com.jimmoores.quandl.RetryPolicy;
import com.jimmoores.quandl.Row;
import com.jimmoores.quandl.SessionOptions;
import com.jimmoores.quandl.SortOrder;
import com.jimmoores.quandl.TabularResult;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.immutables.value.Value;

@Value.Immutable
public abstract class QuandlWikiClient {
  private static final String DATE = "Date";
  private static final String OPEN = "Open";
  private static final String CLOSE = "Close";

  protected abstract String apiKey();

  @Value.Lazy
  protected QuandlSession session() {
    return QuandlSession.create(
        SessionOptions.Builder.withAuthToken(apiKey())
            .withRetryPolicy(RetryPolicy.createFixedRetryPolicy(3, 10))
            .build());
  }

  public SecurityDataSet dataSet(final String symbol, final LocalDate start, final LocalDate end) {
    return ImmutableSecurityDataSet.builder()
        .symbol(symbol)
        .startDate(start)
        .endDate(end)
        .dailySummaries(dailySummaries(symbol, start, end))
        .build();
  }

  private List<SecurityDailySummary> dailySummaries(
      final String symbol, final LocalDate start, final LocalDate end) {
    final ImmutableList.Builder<SecurityDailySummary> summariesBuilder =
        new ImmutableList.Builder<>();
    for (final Row row : tabularResult(symbol, start, end)) {
      summariesBuilder.add(toSummary(row));
    }
    return summariesBuilder.build();
  }

  private TabularResult tabularResult(
      final String symbol, final LocalDate start, final LocalDate end) {
    return session()
        .getDataSet(
            DataSetRequest.Builder.of(dataSetNameForSymbol(symbol))
                .withStartDate(toThreeTen(start))
                .withEndDate(toThreeTen(end))
                .withSortOrder(SortOrder.ASCENDING)
                .build());
  }

  public static SecurityDailySummary toSummary(final Row row) {
    return ImmutableSecurityDailySummary.builder()
        .date(fromThreeTen(row.getLocalDate(DATE)))
        .open(new BigDecimal(row.getString(OPEN)))
        .close(new BigDecimal(row.getString(CLOSE)))
        .build();
  }

  private static String dataSetNameForSymbol(final String symbol) {
    return "WIKI/" + symbol;
  }

  public static LocalDate fromThreeTen(final org.threeten.bp.LocalDate date) {
    return LocalDate.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
  }

  public static org.threeten.bp.LocalDate toThreeTen(final LocalDate date) {
    return org.threeten.bp.LocalDate.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
  }
}
