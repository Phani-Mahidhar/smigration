package com.sas.core.statement;

public interface Statement {
  <T> T accept(StatementVisitor<T> visitor);
}
