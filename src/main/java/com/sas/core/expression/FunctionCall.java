package com.sas.core.expression;

import java.util.List;
import java.util.stream.Collectors;

import com.sas.core.expression.operator.Operator;
import com.sas.core.types.BasicType;

public class FunctionCall extends Expression {
  private final Operator op;

  private final List<Expression> args;

  private BasicType returnType;

  public FunctionCall(Operator op, List<Expression> args) {
    this.op = op;
    this.args = List.copyOf(args);
    this.returnType = deriveReturnType(op, args);
  }

  public Operator getOperator() {
    return op;
  }

  public List<Expression> getArgs() {
    return args;
  }

  BasicType deriveReturnType(Operator op, List<Expression> args) {
    return this.returnType = op.deriveReturnType(args);
  }

  @Override
  public BasicType type() {
    return this.returnType;
  }

  @Override
  public String toString() {
    return op.getOperatorName() + "("
      + args.stream().map(Object::toString).collect(Collectors.joining(","))
      + ")";
  }

  @Override
  public <T> T accept(ExpressionVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
