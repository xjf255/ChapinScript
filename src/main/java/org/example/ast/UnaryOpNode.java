package org.example.ast;

public class UnaryOpNode extends ExpressionNode {
    private final String operator;
    private final ExpressionNode expression;

    public UnaryOpNode(int line, int column,String operator, ExpressionNode expression) {
        super(line,column);
        this.operator = operator;
        this.expression = expression;
    }

    public String getOperator() {
        return operator;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return "UnaryOpNode{operator='" + operator + "', expression=" + expression + "}";
    }
}