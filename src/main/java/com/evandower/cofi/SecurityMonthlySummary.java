package com.evandower.cofi;

import org.immutables.value.Value;

@Value.Immutable
public abstract class SecurityMonthlySummary {
  public abstract String month();

  public abstract String averageOpen();

  public abstract String averageClose();
}
