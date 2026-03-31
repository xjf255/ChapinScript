package org.example.ast;

public class PrintNode extends StatementNode {
    private final ExpressionNode expression;

    public PrintNode(int line, int column,ExpressionNode expression) {
        super(line,column);
        this.expression = expression;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return "PrintNode{expression=" + expression + "}";
    }
}