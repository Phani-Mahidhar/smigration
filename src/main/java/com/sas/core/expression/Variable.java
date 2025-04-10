package com.sas.core.expression;

import java.util.Locale;

public class Variable {
  protected final String value;

  public Variable(String value) {
    this.value = value;
  }

  public boolean matches(Variable other) {
    return this.value.equalsIgnoreCase(other.value);
  }

  public boolean matches(String name) {
    return this.value.equalsIgnoreCase(name);
  }

  @Override
  public String toString() {
    return value.toUpperCase(Locale.ROOT);
  }
}
