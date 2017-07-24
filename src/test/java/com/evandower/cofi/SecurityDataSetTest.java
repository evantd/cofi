package com.evandower.cofi;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import org.junit.Test;

public class SecurityDataSetTest {

  private static final SecurityDailySummary LORI =
      ImmutableSecurityDailySummary.builder()
          .date(LocalDate.of(2017, Month.APRIL, 16))
          .open(new BigDecimal("4.00"))
          .close(new BigDecimal("-4.00"))
          .build();

  private static final SecurityDailySummary SARAH =
      ImmutableSecurityDailySummary.builder()
          .date(LocalDate.of(2017, Month.APRIL, 28))
          .open(new BigDecimal("5.00"))
          .close(new BigDecimal("-5.00"))
          .build();

  private static final SecurityMonthlySummary APRIL =
      ImmutableSecurityMonthlySummary.builder()
          .month("2017-04")
          .averageOpen("4.50")
          .averageClose("-4.50")
          .build();

  private static final SecurityDailySummary DAN =
      ImmutableSecurityDailySummary.builder()
          .date(LocalDate.of(2017, Month.MAY, 24))
          .open(new BigDecimal("3.00"))
          .close(new BigDecimal("-3.00"))
          .build();

  private static final SecurityMonthlySummary MAY =
      ImmutableSecurityMonthlySummary.builder()
          .month("2017-05")
          .averageOpen("3.00")
          .averageClose("-3.00")
          .build();

  private static final SecurityDailySummary EVAN =
      ImmutableSecurityDailySummary.builder()
          .date(LocalDate.of(2017, Month.OCTOBER, 7))
          .open(new BigDecimal("4.00"))
          .close(new BigDecimal("-4.00"))
          .build();

  private static final SecurityDailySummary DANIKA =
      ImmutableSecurityDailySummary.builder()
          .date(LocalDate.of(2017, Month.OCTOBER, 15))
          .open(new BigDecimal("6.00"))
          .close(new BigDecimal("-6.00"))
          .build();

  private static final SecurityDailySummary MARLAYNA =
      ImmutableSecurityDailySummary.builder()
          .date(LocalDate.of(2017, Month.OCTOBER, 23))
          .open(new BigDecimal("8.00"))
          .close(new BigDecimal("-8.00"))
          .build();

  private static final SecurityMonthlySummary OCTOBER =
      ImmutableSecurityMonthlySummary.builder()
          .month("2017-10")
          .averageOpen("6.00")
          .averageClose("-6.00")
          .build();

  @Test
  public void monthlyAvg() {
    assertThat(
            ImmutableSecurityDataSet.builder()
                .symbol("DOWER")
                .startDate(LocalDate.of(2017, Month.JANUARY, 1))
                .endDate(LocalDate.of(2017, Month.DECEMBER, 31))
                .addDailySummaries(LORI, SARAH, DAN, EVAN, DANIKA, MARLAYNA)
                .build()
                .monthlySummaries())
        .containsExactly(APRIL, MAY, OCTOBER);
  }

  @Test
  public void monthlyAvgEmpty() {
    assertThat(
            ImmutableSecurityDataSet.builder()
                .symbol("DOWER")
                .startDate(LocalDate.of(2017, Month.JANUARY, 1))
                .endDate(LocalDate.of(2017, Month.DECEMBER, 31))
                .build()
                .monthlySummaries())
        .isEmpty();
  }
}
