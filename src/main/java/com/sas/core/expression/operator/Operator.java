package com.sas.core.expression.operator;

import com.sas.core.expression.Expression;
import com.sas.core.types.BasicType;

import java.util.List;


public class Operator {
  public enum OperatorType {
    SYSTEM_FUNCTION,
    USER_DEFINED_FUNCTION,
    BINARY_OPERATOR,
    UNARY_OPERATOR,
  }

  private final OperatorType operatorType;
  private final String operatorName;
  private final ReturnTypeInference returnTypeInference;

  public Operator(OperatorType operatorType, String operatorName, ReturnTypeInference returnTypeInference) {
    this.operatorType = operatorType;
    this.operatorName = operatorName;
    this.returnTypeInference = returnTypeInference;
  }

  public Operator(OperatorType operatorType, String operatorName) {
    this(operatorType, operatorName, ReturnTypes.UNKNOWN);
  }

  public OperatorType getOperatorType() {
    return operatorType;
  }

  public String getOperatorName() {
    return operatorName;
  }

  public boolean isSystemFunction() {
    return operatorType == OperatorType.SYSTEM_FUNCTION;
  }

  public boolean isBinaryFunction() {
    return operatorType == OperatorType.BINARY_OPERATOR;
  }

  public BasicType deriveReturnType(List<Expression> arguments) {
    return returnTypeInference.inferReturnType(arguments);
  }
}
