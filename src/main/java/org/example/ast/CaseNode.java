package org.example.ast;

import java.util.List;

public class CaseNode extends ASTNode {
    private final ExpressionNode value;
    private final List<StatementNode> statements;
    private final boolean isDefault;

    public CaseNode(int line, int column, ExpressionNode value, List<StatementNode> statements, boolean isDefault) {
        super(line, column);
        this.value = value;
        this.statements = statements;
        this.isDefault = isDefault;
    }

    public ExpressionNode getValue() {
        return value;
    }

    public List<StatementNode> getStatements() {
        return statements;
    }

    public boolean isDefault() {
        return isDefault;
    }

    @Override
    public String toString() {
        return "CaseNode{value=" + value + ", statements=" + statements + ", isDefault=" + isDefault + "}";
    }
}