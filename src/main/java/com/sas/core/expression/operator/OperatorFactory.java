package com.sas.core.expression.operator;

public class OperatorFactory {
  public static Operator findOrCreateOperator(String operator) {
    Operator inbuiltOperator = SasOperators.findOperator(operator);
    if (inbuiltOperator != null) {
      return inbuiltOperator;
    }
    return createOperatorWithName(operator);
  }

  private static Operator createOperatorWithName(String operator) {
    return new Operator(Operator.OperatorType.USER_DEFINED_FUNCTION, operator);
  }
}
