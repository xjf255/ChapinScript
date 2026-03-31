package org.example.ast;

public class ThrowNode extends StatementNode {
    private final ExpressionNode expression;

    public ThrowNode(int line, int column,ExpressionNode expression) {
        super(line,column);
        this.expression = expression;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return "ThrowNode{expression=" + expression + "}";
    }
}