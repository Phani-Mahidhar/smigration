package com.sas.core.statement;

import java.util.List;

import com.sas.core.expression.DatasetName;

public class DataStepStatement implements Statement {

    DatasetName datasetName;
    List<Statement> dataStepContentStatements;

    public DataStepStatement(DatasetName datasetName, List<Statement> dataStepContentStatements) {
        this.datasetName = datasetName;
        this.dataStepContentStatements = List.copyOf(dataStepContentStatements);
    }

    public DatasetName getDatasetName() {
        return datasetName;
    }

    public List<Statement> getDataStepContentStatements() {
        return dataStepContentStatements;
    }

    @Override
    public String toString() {
        StringBuilder stringContent = new StringBuilder("DATA");
        stringContent.append(" ").append(datasetName.toString()).append("\n");
        for (Statement dataStepContentStatement : dataStepContentStatements) {
            stringContent.append("\t").append(dataStepContentStatement.toString()).append("\n");
        }
        stringContent.append("RUN");
        return stringContent.toString();
    }

    @Override
    public <T> T accept(StatementVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
