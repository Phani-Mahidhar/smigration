package com.sas.core.expression.operator;

import com.sas.core.expression.Expression;
import com.sas.core.types.BasicType;

import java.util.List;

public interface ReturnTypeInference {
  BasicType inferReturnType(List<Expression> arguments);
}
