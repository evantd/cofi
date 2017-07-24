package com.evandower.cofi;

import com.google.common.collect.ImmutableList;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.immutables.value.Value;

@Value.Immutable
public abstract class SecurityDataSet {
  private static final DateTimeFormatter MONTH_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM");

  public abstract String symbol();

  public abstract LocalDate startDate();

  public abstract LocalDate endDate();

  public abstract List<SecurityDailySummary> dailySummaries();

  // This method depends on the daily summaries being sorted by date.
  @Value.Lazy
  public List<SecurityMonthlySummary> monthlySummaries() {
    final ImmutableList.Builder<SecurityMonthlySummary> summariesBuilder =
        new ImmutableList.Builder<>();
    String curMonth = null;
    int dayCount = 0;
    BigDecimal openSum = BigDecimal.ZERO;
    BigDecimal closeSum = BigDecimal.ZERO;

    for (final SecurityDailySummary dailySummary : dailySummaries()) {
      final String newMonth = dailySummary.date().format(MONTH_FORMAT);
      if (curMonth == null) {
        curMonth = newMonth;
      }

      if (!curMonth.equals(newMonth)) {
        summariesBuilder.add(
            ImmutableSecurityMonthlySummary.builder()
                .month(curMonth)
                .averageOpen(
                    openSum.divide(new BigDecimal(dayCount), RoundingMode.HALF_UP).toPlainString())
                .averageClose(
                    closeSum.divide(new BigDecimal(dayCount), RoundingMode.HALF_UP).toPlainString())
                .build());
        curMonth = newMonth;
        dayCount = 0;
        openSum = BigDecimal.ZERO;
        closeSum = BigDecimal.ZERO;
      }

      dayCount++;
      openSum = openSum.add(dailySummary.open());
      closeSum = closeSum.add(dailySummary.close());
    }

    if (curMonth != null) {
      summariesBuilder.add(
          ImmutableSecurityMonthlySummary.builder()
              .month(curMonth)
              .averageOpen(
                  openSum.divide(new BigDecimal(dayCount), RoundingMode.HALF_UP).toPlainString())
              .averageClose(
                  closeSum.divide(new BigDecimal(dayCount), RoundingMode.HALF_UP).toPlainString())
              .build());
    }

    return summariesBuilder.build();
  }
}
