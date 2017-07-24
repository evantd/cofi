package com.evandower.cofi;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.immutables.value.Value;

@JsonSerialize
@Value.Immutable
public abstract class SecurityDailySummary {
  public abstract LocalDate date();

  public abstract BigDecimal open();

  public abstract BigDecimal close();
}
