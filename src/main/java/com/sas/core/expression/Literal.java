package com.sas.core.expression;

import java.math.BigDecimal;

import com.sas.core.types.BasicType;
import com.sas.core.types.TypeName;

public class Literal extends Expression {
  private final Object value;

  private final BasicType type;

  public Literal(Object value, BasicType type) {
    assert checkTypes(value, type);
    this.value = value;
    this.type = type;
  }

  public BigDecimal intValue() {
    return (BigDecimal) value;
  }

  public String stringValue() {
    return (String) value;
  }

  private boolean checkTypes(Object value, BasicType type) {
    return switch (type.getTypeName()) {
      case TypeName.NUMBER -> value instanceof BigDecimal;
      case TypeName.STRING -> value instanceof String;
      case TypeName.BOOLEAN -> value instanceof Boolean;
      default -> true;
    };
  }

  @Override
  public BasicType type() {
    return type;
  }

  @Override
  public String toString() {
    return value.toString();
  }

  @Override
  public <T> T accept(ExpressionVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
