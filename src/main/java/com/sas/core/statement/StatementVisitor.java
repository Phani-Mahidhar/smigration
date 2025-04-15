package com.sas.core.statement;

public interface StatementVisitor<T> {
  T visit(LetStatement let);

  T visit(PutStatement put);

  T visit(DataStepStatement dataStep);

  T visit(CallStatement call);

  T visit(LibraryStatement call);
}
