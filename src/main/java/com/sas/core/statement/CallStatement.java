package com.sas.core.statement;

import com.sas.core.expression.Expression;

public class CallStatement implements Statement {

    private final Expression callExpression;

    public CallStatement(Expression callExpression) {
        this.callExpression = callExpression;
    }

    public Expression getCallExpression() {
        return callExpression;
    }

    @Override
    public <T> T accept(StatementVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "CALL " + callExpression.toString() + ";";
    }
}
