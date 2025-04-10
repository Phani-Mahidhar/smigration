package com.sas.core.statement;

import com.sas.core.expression.Expression;

/**
 * Represents a PUT statement in SAS, which is used to output the value of an
 * expression.
 */
public class PutStatement implements Statement {
  private final Expression putValue;

  public PutStatement(Expression value) {
    this.putValue = value;
  }

  public Expression getPutValue() {
    return putValue;
  }

  @Override
  public <T> T accept(StatementVisitor<T> visitor) {
    return visitor.visit(this);
  }

  @Override
  public String toString() {
    return "PUT " + putValue.toString();
  }
}
