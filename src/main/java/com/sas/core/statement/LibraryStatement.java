package com.sas.core.statement;

import com.sas.core.expression.Expression;
import com.sas.core.expression.Literal;

/**
 * Statement for LibName command in Sas.
 */
public class LibraryStatement implements Statement {
  private final Literal libraryName;
  private final Expression path;

  public LibraryStatement(Literal libraryName, Expression path) {
    this.libraryName = libraryName;
    this.path = path;
  }

  public Literal getLibraryName() {
    return libraryName;
  }

  public Expression getPath() {
    return path;
  }

  @Override
  public <T> T accept(StatementVisitor<T> visitor) {
    return visitor.visit(this);
  }
}