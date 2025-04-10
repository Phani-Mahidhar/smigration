package com.sas.core.expression.operator;

import com.sas.core.types.BasicType;

public class ReturnTypes {

  public static final ReturnTypeInference STRING = (args) -> BasicType.STRING;

  public static final ReturnTypeInference NUMBER = (args) -> BasicType.NUMBER;

  public static final ReturnTypeInference DATE = (args) -> BasicType.DATE;
  public static final ReturnTypeInference UNKNOWN = (args) -> BasicType.UNKNOWN;

  public static final ReturnTypeInference PLUS_OPERAND = (args) -> {
    boolean allNumbers = args.stream().allMatch(arg -> arg.type() == BasicType.NUMBER);
    if (allNumbers) return BasicType.NUMBER;
    return BasicType.STRING;
  };

  public static final ReturnTypeInference MINUS_OPERAND = (args) -> {
    boolean allNumbers = args.stream().allMatch(arg -> arg.type() == BasicType.NUMBER);
    if (allNumbers) return BasicType.NUMBER;
    return BasicType.DATE;
  };
}
