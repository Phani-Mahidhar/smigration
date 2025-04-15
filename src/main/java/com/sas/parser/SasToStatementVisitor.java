package com.sas.parser;

import com.sas.core.expression.Expression;
import com.sas.core.expression.ExpressionBuilder;
import com.sas.core.expression.Literal;
import com.sas.core.statement.*;
import com.sas.core.types.BasicType;
import org.antlr.v4.runtime.misc.Interval;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sas.core.expression.DatasetName;

import java.util.ArrayList;
import java.util.List;

public class SasToStatementVisitor extends com.sas.parser.SasParserBaseVisitor<Statement> {

  private static final Pattern PUT_REGEX = Pattern.compile("(?i)^%PUT\\s+(.*);");
  private static final Pattern MACRO_VARIABLE = Pattern.compile("(?<=&)([a-zA-Z_][a-zA-Z0-9_])+");
  private final SasToExpressionVisitor sasToExpressionVisitor;

  public SasToStatementVisitor() {
    this.sasToExpressionVisitor = new SasToExpressionVisitor();
  }

  @Override
  public Statement visitLetStatement(com.sas.parser.SasParser.LetStatementContext ctx) {
    String variableName = ctx.assignment().identifier().getText();
    Expression letValue = sasToExpressionVisitor.visit(ctx.assignment().expression());
    return new LetStatement(variableName, letValue);
  }

  @Override
  public Statement visitPutStatement(com.sas.parser.SasParser.PutStatementContext ctx) {
    int a = ctx.start.getStartIndex();
    int b = ctx.stop.getStopIndex();
    Interval interval = new Interval(a,b);
    String putStatement = ctx.start.getTokenSource().getInputStream().getText(interval);
    Matcher matcher = PUT_REGEX.matcher(putStatement);
    String putContent = "";
    if (matcher.find() && matcher.groupCount() >= 1) {
      putContent = matcher.group(1);
    }
    return new PutStatement(new Literal(putContent, BasicType.STRING));
  }

  @Override
  public Statement visitDataStepStatement(com.sas.parser.SasParser.DataStepStatementContext ctx) {
    DatasetName datasetName = ExpressionBuilder.datasetName(ctx.datasetName().getText());
    List<Statement> dataStepContentStatements = new ArrayList<>();
    for (com.sas.parser.SasParser.DataStepContentContext dataStepContentContext : ctx.dataStepContent()) {
      Statement statement = dataStepContentContext.accept(this);
      if (statement != null) {
        dataStepContentStatements.add(statement);
      }
    }
    return new DataStepStatement(datasetName, dataStepContentStatements);
  }

  @Override
  public Statement visitCallStatement(com.sas.parser.SasParser.CallStatementContext ctx) {
    Expression callExpression = ctx.expression().accept(sasToExpressionVisitor);
    return new CallStatement(callExpression);
  }

  @Override
  public Statement visitLibraryStatement(com.sas.parser.SasParser.LibraryStatementContext ctx) {
//    Literal libraryName = new Literal(ctx.libraryName().getText(), BasicType.STRING);
//    Expression libraryPath = sasToExpressionVisitor.visit(ctx.libraryPath().expression());
//    return new LibraryStatement(libraryName, libraryPath);
    return super.visitLibraryStatement(ctx);
  }
}
