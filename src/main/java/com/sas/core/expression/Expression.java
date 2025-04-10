package com.sas.core.expression;

import com.sas.core.types.BasicType;

abstract public class Expression {
  public BasicType type() {
    return BasicType.UNKNOWN;
  }

  abstract public <T> T accept(ExpressionVisitor<T> visitor);
}
