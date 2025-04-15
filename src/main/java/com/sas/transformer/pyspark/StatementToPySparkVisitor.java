package com.sas.transformer.pyspark;

import com.sas.core.expression.DatasetName;
import com.sas.core.statement.*;
import com.sas.transformer.writer.ExpressionWriter;

import java.util.*;

/**
 * This class is responsible for converting SAS statements to PySpark code. It
 * implements the StatementVisitor interface to visit different types of SAS
 * statements and generate the corresponding PySpark code.
 */
public class StatementToPySparkVisitor implements StatementVisitor<Void> {

  private final Map<String, Set<String>> imports = new HashMap<>();
  ExpressionWriter writer;

  private final PySparkConfig config;

  ExpressionToPySparkVisitor expressionToPySparkVisitor;

  StatementToPySparkVisitor(PySparkConfig config) {
    this.config = config;
    this.writer = new ExpressionWriter();
    this.expressionToPySparkVisitor = new ExpressionToPySparkVisitor(writer);
  }

  private void addImport(String packageName, String objectName) {
    Set<String> objectNames = imports.getOrDefault(packageName, new HashSet<>());
    objectNames.add(objectName);
    imports.put(packageName, objectNames);
  }

  private void addImport(String packageName) {
    imports.putIfAbsent(packageName, new HashSet<>());
  }


  public Map<String, Set<String>> getUsedImports() {
    return expressionToPySparkVisitor.imports;
  }

  public String getWriterContent() {
    return writer.toString();
  }

  @Override
  public Void visit(LetStatement let) {
    writer.append(variableName(let.getName()));
    writer.append(" = ");
    let.getValue().accept(expressionToPySparkVisitor);
    writer.append("\n");
    return null;
  }

  @Override
  public Void visit(PutStatement put) {
    writer.append("print(");
    put.getPutValue().accept(expressionToPySparkVisitor);
    writer.append(")\n");
    return null;
  }

  @Override
  public Void visit(DataStepStatement dataStep) {
    if (dataStep.getDatasetName() == DatasetName.NULL) {
      writer.append("\n");
      writer.append("# Data Step\n");
    }
    for (Statement dataStepContentStatement : dataStep.getDataStepContentStatements()) {
      dataStepContentStatement.accept(this);
    }
    writer.append("\n");
    return null;
  }

  @Override
  public Void visit(CallStatement call) {
    call.getCallExpression().accept(expressionToPySparkVisitor);
    writer.append("\n");
    return null;
  }

  @Override
  public Void visit(LibraryStatement call) {
    return null;
  }

  private String variableName(String name) {
    return config.capitalizeIdentifier() ? name.toUpperCase(Locale.ROOT) : name.toLowerCase(Locale.ROOT);
  }
}
