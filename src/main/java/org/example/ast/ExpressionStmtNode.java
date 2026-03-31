package org.example.ast;

public class ExpressionStmtNode extends StatementNode {
    private final ExpressionNode expression;

    public ExpressionStmtNode(int line, int column,ExpressionNode expression) {
        super(line,column);
        this.expression = expression;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return "ExpressionStmtNode{expression=" + expression + "}";
    }
}