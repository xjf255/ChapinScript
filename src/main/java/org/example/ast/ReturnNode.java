package org.example.ast;

public class ReturnNode extends StatementNode {
    private final ExpressionNode expression;

    public ReturnNode(int line, int column,ExpressionNode expression) {
        super(line,column);
        this.expression = expression;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return "ReturnNode{expression=" + expression + "}";
    }
}