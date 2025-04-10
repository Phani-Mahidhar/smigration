package com.sas.core.statement;

import com.sas.core.expression.Expression;

/**
 * Represents a LET statement in SAS, which is used to assign a value to a
 * variable.
 */
public class LetStatement implements Statement {

  private final String name;
  private final Expression value;

  public LetStatement(String name, Expression value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  } 
  
  public Expression getValue() {
    return value;
  }

  @Override
  public <T> T accept(StatementVisitor<T> visitor) {
    return visitor.visit(this);
  }

  @Override
  public String toString() {
    return "LET " + name + " = " + value.toString();
  }
}
