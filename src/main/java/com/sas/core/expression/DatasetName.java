package com.sas.core.expression;

import java.util.List;

public class DatasetName extends Expression {

  public static final DatasetName NULL = new DatasetName("_null_");

  List<String> names;

  public DatasetName(List<String> names) {
    this.names = List.copyOf(names);
  }

  public List<String> getNames() {
    return names;
  }

  public DatasetName(String name) {
    this(List.of(name));
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof DatasetName)) {
      return false;
    }
    return this.names.equals(((DatasetName) obj).names);
  }

  @Override
  public String toString() {
    return String.join(".", names);
  }

  @Override
  public <T> T accept(ExpressionVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
