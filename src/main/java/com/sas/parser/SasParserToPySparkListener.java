//package com.sas.parser;
//
//import com.sas.transformer.writer.ExpressionWriter;
//import org.antlr.v4.runtime.tree.ParseTree;
//import org.apache.commons.lang3.StringUtils;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class SasParserToPySparkListener extends com.sas.parser.SasParserBaseListener {
//
//  private final ExpressionWriter writer;
//  private final Map<String, List<String>> imports;
//  private final List<String> variables = new ArrayList<>();
//  private boolean putOn = false;
//
//  public SasParserToPySparkListener(ExpressionWriter writer, Map<String, List<String>> imports) {
//    this.writer = writer;
//    this.imports = imports;
//  }
//
//  private void appendImports(String key, String value) {
//    if (value == null) {
//      imports.put(key, new ArrayList<>());
//      return;
//    }
//    List<String> list = imports.get(key);
//    if (list == null) {
//      list = new ArrayList<>();
//      imports.put(key, list);
//    }
//    if (list.contains(value)) {
//      return;
//    }
//    list.add(value);
//  }
//
//  @Override
//  public void enterProgramStatement(SasParser.ProgramStatementContext ctx) {
//
//  }
//
//  @Override
//  public void enterMacroStatement(SasParser.MacroStatementContext ctx) {
//  }
//
//  @Override
//  public void exitMacroStatement(SasParser.MacroStatementContext ctx) {
//  }
//
//  @Override
//  public void enterAssignment(SasParser.AssignmentContext ctx) {
//  }
//
//  @Override
//  public void exitAssignment(SasParser.AssignmentContext ctx) {
//  }
//
//  @Override
//  public void enterPutStatement(SasParser.PutStatementContext ctx) {
//    String var = ctx.getChild(1).getText().trim();
//    String varWithoutAnd = var.replaceFirst("&", "");
//    if (variables.contains(varWithoutAnd)) {
//      writer.append("print(" + varWithoutAnd + ")").newlineAndIndent();
//    } else {
//      for (String s : variables) {
//        String withAnd = "&" + s;
//        String withBrackets = "{" + s + "}";
//        var = var.replaceAll(withAnd + "\\.", withBrackets).replace(withAnd + "\\s*", withBrackets);
//      }
//      writer.append("print(f'" + var + "')").newlineAndIndent();
//    }
//  }
//
//  @Override
//  public void exitPutStatement(SasParser.PutStatementContext ctx) {
//  }
//
//  @Override
//  public void enterLetStatement(SasParser.LetStatementContext ctx) {
//    appendImports("os", null);
//    variables.add(ctx.getChild(1).getText().replaceAll("'", ""));
//    writer.append(ctx.getChild(1).getText() + " = ");
//    if (ctx.getChild(3).getText().toLowerCase().contains("%sysfunc")) {
//      String path = ctx.getChild(3).getChild(0).getChild(2).getChild(0).getChild(0).getChild(2).getText().replace("\"", "");
//      for (String s : variables) {
//        String withAnd = "&" + s;
//        String withBrackets = "{" + s + "}";
//        path = path.replaceAll(withAnd + "\\.", withBrackets).replace(withAnd + "\\s*", withBrackets);
//      }
//      appendImports("pathlib", "Path");
//      writer.append("int(Path(f'" + path + "').exists())").newlineAndIndent();
//    } else if (ctx.getChild(3).getText().toLowerCase().contains("%sysget")) {
//      writer.append("os.getenv(\""
//        + ctx.getChild(3).getChild(0).getChild(2).getText() + "\")").newlineAndIndent();
//    } else {
//      writer.append("'").append(ctx.getChild(3).getText()).append("'").newlineAndIndent();
//    }
//  }
//
//  @Override
//  public void enterLibraryStatement(SasParser.LibraryStatementContext ctx) {
//    String library = ctx.getChild(1).getText().replaceAll("'", "");
//    String var = ctx.getChild(2).getText().replace("\"", "");
//    String varWithoutAnd = var.replaceFirst("&", "");
//    variables.add(library);
//    writer.append(library).appendAssignment();
//    if (variables.contains(varWithoutAnd)) {
//      writer.append(varWithoutAnd).newlineAndIndent();
//    } else {
//      for (String s : variables) {
//        String withAnd = "&" + s;
//        String withBrackets = "{" + s + "}";
//        var = var.replaceAll(withAnd + "\\.", withBrackets).replace(withAnd + "\\s*", withBrackets);
//      }
//      writer.append("f'" + var + "'").newlineAndIndent();
//    }
//  }
//
////    @Override
////    public void enterDataStepStatement(SasParser.DataStepStatementContext ctx) {
////        System.out.println("Entering LET statement: " + ctx.getText());
//  ////        writer.append(ctx.getChild(1).getText() + " = os.getenv(\"" +
//  ////                ctx.getChild(3).getChild(0).getChild(2).getText() + "\")").newlineAndIndent();
////    }
//
////    @Override
////    public void enterDataStepContent(SasParser.DataStepContentContext ctx) {
////        System.out.println("Entering LET statement: " + ctx.getText());
//
//  /// /        writer.append(ctx.getChild(1).getText() + " = os.getenv(\"" +
//  /// /                ctx.getChild(3).getChild(0).getChild(2).getText() + "\")").newlineAndIndent();
////    }
//  @Override
//  public void enterCallStatement(SasParser.CallStatementContext ctx) {
////        writer.append(ctx.getChild(1).getText() + " = os.getenv(\"" +
////                ctx.getChild(3).getChild(0).getChild(2).getText() + "\")").newlineAndIndent();
//  }
//
//  @Override
//  public void enterSymputArgs(SasParser.SymputArgsContext ctx) {
//    variables.add(ctx.getChild(0).getText().replaceAll("'", ""));
//    writer.append(ctx.getChild(0).getText().replaceAll("'", "")).appendAssignment();
////        writer.append(ctx.getChild(1).getText() + " = os.getenv(\"" +
////                ctx.getChild(3).getChild(0).getChild(2).getText() + "\")").newlineAndIndent();
//  }
//
//  @Override
//  public void exitSymputArgs(SasParser.SymputArgsContext ctx) {
//    writer.newlineAndIndent();
////        writer.append(ctx.getChild(0).getText().replaceAll("'", "")).appendAssignment();
////        writer.append(ctx.getChild(1).getText() + " = os.getenv(\"" +
////                ctx.getChild(3).getChild(0).getChild(2).getText() + "\")").newlineAndIndent();
//  }
//
//  @Override
//  public void enterFunctionExpression(SasParser.FunctionExpressionContext ctx) {
//    if (ctx.getChild(0).getChild(0).getText().equalsIgnoreCase("put")) {
//      putOn = true;
//      appendImports("datetime", "datetime");
//      appendImports("datetime", "timedelta");
//      String operator = ctx.getChild(0).getChild(2).getChild(0).getChild(0).getChild(1).getText();
//      String varName = ctx.getChild(0).getChild(2).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getChild(2).getChild(0).getText().replaceAll("\"", "").replaceFirst("&", "").toUpperCase();
//      writer.append("datetime.fromtimestamp(int((datetime.fromtimestamp(int(datetime.strptime("
//        + varName + ", \"%Y%m%d\").timestamp())) " + operator + " timedelta(days = 1)).timestamp())).strftime(\"%Y%m%d\")");
//    }
//    if (!putOn && ctx.getChild(0).getChild(0).getText().equalsIgnoreCase("input")) {
//      appendImports("datetime", "datetime");
//      appendImports("datetime", "timedelta");
//      String varName = ctx.getChild(0).getChild(2).getChild(0).getText().replaceAll("\"", "").replaceFirst("&", "").toUpperCase();
//      writer.append("int(datetime.strptime(" + varName + ", \"%Y%m%d\").timestamp())");
//    }
//    if (ctx.getChild(0).getChild(0).getText().equalsIgnoreCase("run")) {
//      ctx.getChild(0).getChild(2).getChild(0).getChild(0).getText().replaceAll("\"", "").replaceFirst("&", "").toUpperCase();
//    }
////        writer.append(ctx.getChild(1).getText() + " = os.getenv(\"" +
////                ctx.getChild(3).getChild(0).getChild(2).getText() + "\")").newlineAndIndent();
//  }
//
//  @Override
//  public void exitFunctionExpression(SasParser.FunctionExpressionContext ctx) {
//    if (ctx.getChild(0).getChild(0).getText().equalsIgnoreCase("put")) {
//      putOn = false;
//    }
//  }
//
//  @Override
//  public void enterIncludeStatement(SasParser.IncludeStatementContext ctx) {
//    String path = ctx.getChild(1).getText().replaceAll("'", "").replaceAll("\"", "");
//    String[] paths = path.split("\\.");
//    paths[paths.length - 1] = "PY";
//    appendImports("importlib.util", null);
//    String[] names = path.split("[\\\\/]");
//    String name = names[names.length - 1].split("\\.")[0];
//    writer.append(name + "_file_path").appendAssignment().appendSingleQuote().append(String.join(".", paths)).appendSingleQuote().newlineAndIndent();
//    variables.add(name + "_file_path");
//    variables.add(name + "_spec");
//    writer.append(name + "_spec = importlib.util.spec_from_file_location(\"module\", " + name + "_file_path)").newlineAndIndent();
//    writer.append(name + " = importlib.util.module_from_spec(" + name + "_spec)").newlineAndIndent();
//    writer.append(name + "_spec.loader.exec_module(" + name + ")").newlineAndIndent();
//  }
//
//  @Override
//  public void enterMacroDefStatement(SasParser.MacroDefStatementContext ctx) {
//    String methodName = ctx.getChild(1).getText().replaceAll("'", "");
//    writer.newlineAndIndent();
//    writer.append("def ").append(methodName).append("(");
//    if (ctx.getChild(2).getText().equalsIgnoreCase(";")) {
//      writer.append("):").newlineAndIndent();
//      writer.begin();
//      return;
//    }
//    int paramCount = ctx.getChild(3).getChildCount();
//    for (int i = 0; i < paramCount; i++) {
//      ParseTree tree = ctx.getChild(3).getChild(i);
//      if (i % 2 == 0) {
//        writer.append(tree.getChild(0).getText());
////                if (tree.getChildCount() <= 2) {
////                    writer.append("None");
////                } else {
////                    writer.append(tree.getChild(2).getText());
////                }
//      } else {
//        if (i != paramCount - 1) {
//          writer.append(tree.getText()).appendSpace();
//        }
//      }
//    }
//    writer.append("):").newlineAndIndent();
//    writer.begin();
//    // writer.append(")").newlineAndIndent();
//  }
//
//  @Override
//  public void enterMacroCall(SasParser.MacroCallContext ctx) {
//    String methodName = ctx.getChild(1).getText().replaceAll("'", "");
//    writer.append(methodName).append("(");
//    if (ctx.getChild(2).getText().equalsIgnoreCase(";")) {
//      writer.append(")");
//      return;
//    }
//    int paramCount = ctx.getChild(3).getChildCount();
//    for (int i = 0; i < paramCount; i++) {
//      ParseTree tree = ctx.getChild(3).getChild(i);
//      if (i % 2 == 0) {
//        writer.append(tree.getChild(0).getText()).appendAssignment();
//        if (tree.getChildCount() <= 2) {
//          writer.append("None");
//        } else {
//          vv(tree.getChild(2).getText());
//        }
//      } else {
//        if (i != paramCount - 1) {
//          writer.append(tree.getText()).appendSpace();
//        }
//      }
//    }
//    writer.append(")").newlineAndIndent();
//  }
//
//  private void vv(String var) {
//    String varWithoutAnd = var.replaceFirst("&", "");
//    if (variables.contains(varWithoutAnd)) {
//      writer.append(varWithoutAnd);
//    } else {
//      for (String s : variables) {
//        String withAnd = "&" + s;
//        String withBrackets = "{" + s + "}";
//        var = var.replaceAll(withAnd + "\\.", withBrackets).replace(withAnd + "\\s*", withBrackets);
//      }
//      writer.append("f'" + var + "'");
//    }
//  }
//
//  @Override
//  public void exitMacroDefStatement(SasParser.MacroDefStatementContext ctx) {
//    writer.newlineAndIndent();
//    writer.end();
//
//  }
//
////    @Override
////    public void exitFunctionExpression(SasParser.FunctionExpressionContext ctx) {
////        System.out.println("Entering LET statement: " + ctx.getText());
////        if (ctx.getChild(0).getChild(0).getText().equalsIgnoreCase("put")) {
////            writer.append(").strftime(\"%Y%m%d\")").newlineAndIndent();
////        }
//
//  /// /        writer.append(ctx.getChild(1).getText() + " = os.getenv(\"" +
//  /// /                ctx.getChild(3).getChild(0).getChild(2).getText() + "\")").newlineAndIndent();
////    }
//  @Override
//  public void enterStandardFunction(SasParser.StandardFunctionContext ctx) {
////        writer.append(ctx.getChild(1).getText() + " = os.getenv(\"" +
////                ctx.getChild(3).getChild(0).getChild(2).getText() + "\")").newlineAndIndent();
//  }
//
//  @Override
//  public void enterIfStatement(SasParser.IfStatementContext ctx) {
//    ParseTree conditionTree = ctx.getChild(1);
//    Map<String, String> operators = new HashMap<>();
//    operators.put("eq", " == ");
//    operators.put("=", " == ");
//    operators.put("gt", " > ");
//    operators.put("lt", " < ");
//    operators.put("gte", " >= ");
//    operators.put("lte", " <= ");
//    writer.append("if ");
//    writer.append(conditionTree.getChild(0).getText().replace("&", "").replace(".", ""))
//      .append(operators.get(conditionTree.getChild(1).getText().trim()));
//    if (!conditionTree.getChild(2).getText().startsWith("&")) {
//      String right = conditionTree.getChild(2).getText();
//      writer.append(StringUtils.isNumeric(right) ? right : "'" + right + "'");
//    } else {
//      writer.append(conditionTree.getChild(2).getText().replace("&", ""));
//    }
//    writer.append(":").newlineAndIndent();
//    writer.begin();
//    appendImports("pyspark.shell", "spark");
//    writer.append("file_path = f'{SRC_LANDING}/{in_dataset}-{RUNDATE}.dat'").newlineAndIndent();
//    writer.append("df = spark.read.csv(file_path, header=True, inferSchema=True)").newlineAndIndent();
//    writer.end();
//    writer.append("else:").newlineAndIndent();
//    writer.begin();
//    writer.append("df = spark.read.format(\"sas7bdat\").load(f\"{INLIB}/{in_dataset}\")").newlineAndIndent();
//    writer.end();
//    // writer.append("    ").append("df = df.withColumnRenamed(\"ACCT\", \"ACCT_NBR\")").newlineAndIndent();
////        writer.append(ctx.getChild(1).getText() + " = os.getenv(\"" +
////                ctx.getChild(3).getChild(0).getChild(2).getText() + "\")").newlineAndIndent();
//  }
//
//  @Override
//  public void exitStandardFunction(SasParser.StandardFunctionContext ctx) {
////        writer.append(ctx.getChild(1).getText() + " = os.getenv(\"" +
////                ctx.getChild(3).getChild(0).getChild(2).getText() + "\")").newlineAndIndent();
//  }
//
//  @Override
//  public void enterInputFunction(SasParser.InputFunctionContext ctx) {
////        if (ctx.getChild(0).getText().equalsIgnoreCase("put")) {
////            writer.append("datetime.fromtimestamp(");
////        }
////        writer.append(ctx.getChild(1).getText() + " = os.getenv(\"" +
////                ctx.getChild(3).getChild(0).getChild(2).getText() + "\")").newlineAndIndent();
//  }
//
//  @Override
//  public void exitInputFunction(SasParser.InputFunctionContext ctx) {
////        if (ctx.getChild(0).getText().equalsIgnoreCase("put")) {
////            writer.append(").strftime(\"%Y%m%d\")").newlineAndIndent();
////        }
////        writer.append(ctx.getChild(1).getText() + " = os.getenv(\"" +
////                ctx.getChild(3).getChild(0).getChild(2).getText() + "\")").newlineAndIndent();
//  }
//
//  @Override
//  public void exitLetStatement(SasParser.LetStatementContext ctx) {
//  }
//}
