package com.sas.core.expression;

import com.sas.core.expression.operator.Operator;
import com.sas.core.expression.operator.OperatorFactory;

import java.util.Arrays;
import java.util.List;

public class ExpressionBuilder {
  public static Expression functionCall(String functionName, List<Expression> arguments) {
    Operator op = OperatorFactory.findOrCreateOperator(functionName);
    return new FunctionCall(op, arguments);
  }

  public static DatasetName datasetName(String datasetName) {
    if (datasetName.equalsIgnoreCase("_null_")) {
      return DatasetName.NULL;
    }
    return new DatasetName(Arrays.stream(datasetName.split("\\.")).toList());
  }
}
