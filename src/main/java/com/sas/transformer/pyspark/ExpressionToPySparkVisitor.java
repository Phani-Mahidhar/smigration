package com.sas.transformer.pyspark;

import com.sas.core.SasStringUtil;
import com.sas.core.expression.*;
import com.sas.core.expression.operator.Operator;
import com.sas.core.types.TypeName;
import com.sas.transformer.writer.ExpressionWriter;

import java.util.*;
import java.util.regex.Pattern;

class ExpressionToPySparkVisitor implements ExpressionVisitor<Void> {

  private static final Pattern MACRO_VARIABLE = Pattern.compile("&([a-zA-Z_][a-zA-Z0-9_]+)\\.?");

  Map<String, Set<String>> imports = new HashMap<>();

  private final ExpressionWriter writer;

  public ExpressionToPySparkVisitor(ExpressionWriter writer) {
    this.writer = writer;
  }

  private void addImport(String packageName, String... objectNames) {
    Set<String> existingObjects = imports.getOrDefault(packageName, new HashSet<String>());
    existingObjects.addAll(Arrays.stream(objectNames).toList());
    imports.put(packageName, existingObjects);
  }

  private void addImport(String packageName) {
    imports.putIfAbsent(packageName, new HashSet<>());
  }

  @Override
  public Void visit(Literal literal) {
    switch (literal.type().getTypeName()) {
      case NUMBER -> writer.append(literal.intValue());
      case STRING -> writer.append(asPythonStringLiteral((literal.stringValue())));
    }
    return null;
  }

  @Override
  public Void visit(DatasetName datasetName) {
    writer.append(asPythonStringLiteral(String.join(".", datasetName.getNames())));
    return null;
  }

  @Override
  public Void visit(FunctionCall functionCall) {
    if (functionCall.getOperator().getOperatorType() == Operator.OperatorType.BINARY_OPERATOR) {
      unparseBinaryFunctionCall(functionCall);
      return null;
    }
    unparseFunctionCall(functionCall);
    return null;
  }

  private void unparseBinaryFunctionCall(FunctionCall call) {
    switch (call.getOperator().getOperatorName()) {
      case "+", "-" -> unparseArithmeticCall(call);
      default -> unparseBinarySyntax(call);
    }
  }

  private void unparseFunctionCall(FunctionCall call) {
    switch (call.getOperator().getOperatorName()) {
      case "%SYSGET" -> unparseSysGet(call);
      case "%SYSFUNC" -> call.getArgs().get(0).accept(this);
      case "SYMPUT" -> unparseAssignmentSyntax(call.getArgs().get(0), call.getArgs().get(1));
      case "INPUT" -> unparseInputCall(call);
      case "PUT" -> unparsePutCall(call);
      case "FILEEXIST" -> unparseFileExistCall(call);
      default -> unparseFunctionSyntax(call);
    }
  }

  private void unparseSysGet(FunctionCall call) {
    addImport("os");
    writer.append("os.getenv");
    writer.append("(");
    int i = 0;
    for (Expression arg : call.getArgs()) {
      if (i != 0) writer.appendComma();
      i++;
      arg.accept(this);
    }
    writer.append(")");
  }

  private void unparseInputCall(FunctionCall call) {
    addImport("datetime", "datetime", "timedelta");

    writer.append("datetime.strptime(");
    call.getArgs().getFirst().accept(this);
//    writer.append(asPythonVariable(call.getArgs().get(0).toString()));
    writer.appendComma();
    writer.append(asPythonDatetimeFormat(call.getArgs().get(1).toString()));
    writer.append(")");
  }

  private void unparseFileExistCall(FunctionCall call) {
    addImport("os");
    writer.append("int(os.path.exists");
    writer.append("(");
    call.getArgs().getFirst().accept(this);
    writer.append("))");
  }

  private void unparseBinarySyntax(FunctionCall call) {
    call.getArgs().get(0).accept(this);
    writer.append(" " + call.getOperator().getOperatorName() + " ");
    call.getArgs().get(1).accept(this);
  }

  private void unparseFunctionSyntax(FunctionCall call) {
    writer.append(call.getOperator().getOperatorName());
    writer.append("(");
    int i = 0;
    for (Expression arg : call.getArgs()) {
      if (i != 0) writer.appendComma();
      i++;
      arg.accept(this);
    }
    writer.append(")");
  }

  private void unparseArithmeticCall(FunctionCall call) {
    if (call.type().getTypeName() == TypeName.NUMBER) {
      unparseBinarySyntax(call);
      return;
    }
    addImport("datetime", "datetime", "timedelta");

    List<Expression> args = call.getArgs();
    for (int i = 0; i < 2; i++) {
      Expression arg = args.get(i);
      if (arg.type().getTypeName() == TypeName.DATE) {
        args.getFirst().accept(this);
      } else {
        writer.append("datetime.timedelta(days=");
        arg.accept(this);
        writer.append(")");
      }
      if (i == 0) {
        writer.append(" " + call.getOperator().getOperatorName() + " ");
      }
    }
  }

  private void unparsePutCall(FunctionCall call) {
    addImport("datetime", "datetime", "timedelta");

    List<Expression> args = call.getArgs();
    boolean wrapBraces = isBinaryFunction(args.getFirst());
    if (wrapBraces) writer.append("(");
    call.getArgs().getFirst().accept(this);
    if (wrapBraces) writer.append(")");
    writer.append(".strftime(")
      .append(asPythonDatetimeFormat(call.getArgs().getLast().toString()))
      .append(")");
  }

  private void unparseAssignmentSyntax(Expression varName, Expression value) {
    writer.append(asPythonVariable(varName.toString()));
    writer.append(" = ");
    value.accept(this);
  }

  static String asPythonVariable(String value) {
    return MACRO_VARIABLE
      .matcher(value)
      .replaceAll(matcher -> matcher.group(1).toUpperCase())
      .replace("\"", "")
      .replace("'", "");
  }

  static String asPythonDatetimeFormat(String value) {
    return "\"%Y%m%d\"";
  }

  protected static String asPythonStringLiteral(String value) {
    if (SasStringUtil.isStringLiteral(value)) {
      return value;
    }
    String substitutedLiteral = MACRO_VARIABLE
      .matcher(value)
      .replaceAll(matcher -> "{" + matcher.group(1).toUpperCase() + "}");
    if (SasStringUtil.isDoubleQuoted(substitutedLiteral)) {
      return "f" + substitutedLiteral;
    }
    return "f'" + substitutedLiteral + "'";
  }

  private boolean isBinaryFunction(Expression expr) {
    return expr instanceof FunctionCall call && call.getOperator().isBinaryFunction();
  }

  public static void main(String[] args) {
    assert Objects.equals(asPythonStringLiteral("&ST_STAGING./staging/&rundate."), "f'{ST_STAGING}/staging/{rundate}'");
  }
}
