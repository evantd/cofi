package com.evandower.cofi;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@JsonSerialize
@Value.Immutable
public abstract class SecurityMonthlySummary {
  public abstract String month();

  public abstract String averageOpen();

  public abstract String averageClose();
}
