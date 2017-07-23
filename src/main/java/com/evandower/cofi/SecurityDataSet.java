package com.evandower.cofi;

import com.jimmoores.quandl.DataSetRequest;
import com.jimmoores.quandl.QuandlSession;
import com.jimmoores.quandl.TabularResult;
import java.time.LocalDate;
import org.immutables.value.Value;

@Value.Immutable
public abstract class SecurityDataSet {
  protected abstract QuandlSession session();

  public abstract String symbol();

  public abstract LocalDate startDate();

  public abstract LocalDate endDate();

  @Value.Derived
  protected TabularResult tabularResult() {
    return session()
        .getDataSet(
            DataSetRequest.Builder.of(dataSetNameForSymbol(symbol()))
                .withStartDate(toThreeTen(startDate()))
                .withEndDate(toThreeTen(endDate()))
                .build());
  }

  private static String dataSetNameForSymbol(final String symbol) {
    return "WIKI/" + symbol;
  }

  private static org.threeten.bp.LocalDate toThreeTen(final LocalDate date) {
    return org.threeten.bp.LocalDate.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
  }
}
