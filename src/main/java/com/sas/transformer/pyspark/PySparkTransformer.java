package com.sas.transformer.pyspark;

import com.sas.core.SasFile;
import com.sas.core.statement.Statement;

import java.util.Map;
import java.util.Set;

public class PySparkTransformer {

  private final PySparkConfig config;

  public PySparkTransformer(PySparkConfig config) {
    this.config = config;
  }

  public String translateSasFile(SasFile file) {
    StatementToPySparkVisitor statementToPySparkVisitor = new StatementToPySparkVisitor();
    for (Statement statement : file.getStatements()) {
      statement.accept(statementToPySparkVisitor);
    }
    StringBuilder pySparkCode = new StringBuilder();
    Map<String, Set<String>> importsUsed = statementToPySparkVisitor.getUsedImports();
    if (config.setupSparkSession()) {
      importsUsed.put("pyspark.sql", Set.of("SparkSession"));
    }
    pySparkCode.append(createImportStatements(importsUsed));
    pySparkCode.append("\n");
    if (config.setupSparkSession()) {
      pySparkCode.append("# Initialize SparkSession\n");
      pySparkCode.append("spark = SparkSession.builder.appName(\"SAS to PySpark\").getOrCreate()\n\n");
    }
    pySparkCode.append(statementToPySparkVisitor.getWriterContent());
    return pySparkCode.toString();
  }

  String createImportStatements(Map<String, Set<String>> usedImports) {
    StringBuilder importStatement = new StringBuilder();
    for (Map.Entry<String, Set<String>> importPackage : usedImports.entrySet()) {
      if (importPackage.getValue().isEmpty()) {
        importStatement.append("import ").append(importPackage.getKey());
      } else {
        importStatement.append("from ")
          .append(importPackage.getKey())
          .append(" import ")
          .append(String.join(",", importPackage.getValue()));
      }
      importStatement.append("\n");
    }
    return importStatement.toString();
  }
}
