package com.sas.core;

import com.sas.core.statement.Statement;
import com.sas.parser.SasParser;
import com.sas.parser.SasToStatementVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SasFile {
  List<Statement> statements;

  public SasFile(List<Statement> statements) {
    this.statements = List.copyOf(statements);
  }

  public List<Statement> getStatements() {
    return statements;
  }

  public static SasFile buildCanonical(SasParser parser) {
    List<Statement> statements = new ArrayList<>();
    SasParser.ProgramContext tree = parser.program();
    SasToStatementVisitor sasToStatementVisitor = new SasToStatementVisitor();
    for (SasParser.ProgramStatementContext programStatementContext: tree.programStatement()) {
      Statement statement = programStatementContext.accept(sasToStatementVisitor);
      if (statement != null) {
        statements.add(statement);
      }
    }
    return new SasFile(statements);
  }

  @Override
  public String toString() {
    return statements.stream().map(Object::toString).collect(Collectors.joining("\n"));
  }
}
