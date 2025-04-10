package com.sas.core.expression;

public interface ExpressionVisitor<T> {
    T visit(Literal literal);
    T visit(FunctionCall functionCall);
    T visit(DatasetName datasetName);
}
