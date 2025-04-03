package com.sas.canonical.statement;

public interface SasStatement {
  SasStatement accept(SasStatementVisitor visitor);
}
