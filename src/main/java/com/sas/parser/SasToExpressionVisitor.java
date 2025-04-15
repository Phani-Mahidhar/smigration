package com.sas.parser;

import com.sas.core.expression.Expression;
import com.sas.core.expression.ExpressionBuilder;
import com.sas.core.expression.Literal;
import com.sas.core.types.BasicType;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.sas.parser.SasParser.*;

public class SasToExpressionVisitor extends com.sas.parser.SasParserBaseVisitor<Expression> {

  @Override
  public Expression visitExpression(ExpressionContext ctx) {

    if (ctx.expression().size() == 2) {
      List<Expression> arguments = new ArrayList<>();
      for (ExpressionContext expressionContext : ctx.expression()) {
        arguments.add(expressionContext.accept(this));
      }
      String operator;
      if (ctx.PLUS() != null) {
        operator = "+";
      } else if (ctx.MINUS() != null) {
        operator = "-";
      } else if (ctx.POW() != null) {
        operator = "*";
      } else {
        throw new UnsupportedOperationException("Unknown operator in binary call");
      }
      return ExpressionBuilder.functionCall(operator, arguments);
    }
    return super.visitExpression(ctx);
  }

  @Override
  public Expression visitFunctionExpression(FunctionExpressionContext ctx) {
    String functionName = ctx.identifier().getText();
    List<Expression> arguments = new ArrayList<>();
    for (FunctionArgContext functionArgContext : ctx.functionArgList().functionArg()) {
      arguments.add(super.visitFunctionArg(functionArgContext));
    }
    return ExpressionBuilder.functionCall(functionName, arguments);
  }

  @Override
  public Expression visitMacroFunctionCall(MacroFunctionCallContext ctx) {
    String functionName = ctx.M_FUNCTION_ID().getText();
    List<Expression> arguments = new ArrayList<>();
    for (FunctionArgContext functionArgContext : ctx.functionArgList().functionArg()) {
      arguments.add(super.visitFunctionArg(functionArgContext));
    }
    return ExpressionBuilder.functionCall(functionName, arguments);
  }

  @Override
  public Expression visitLiteral(LiteralContext ctx) {
    if (ctx.NUMBER() != null) {
      return new Literal(new BigDecimal(ctx.getText()), BasicType.NUMBER);
    } else {
      return new Literal(ctx.getText(), BasicType.STRING);
    }
  }

  @Override
  public Expression visitFormat(FormatContext ctx) {
    return new Literal(StringUtils.stripEnd(ctx.getText(), "."), BasicType.STRING);
  }
}
