package org.example.ast;

public class BinaryOpNode extends ExpressionNode {
    private final String operator;
    private final ExpressionNode left;
    private final ExpressionNode right;

    public BinaryOpNode(int line, int column, String operator, ExpressionNode left, ExpressionNode right) {
        super(line, column);
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    public String getOperator() {
        return operator;
    }

    public ExpressionNode getLeft() {
        return left;
    }

    public ExpressionNode getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "BinaryOpNode{operator='" + operator + "', left=" + left + ", right=" + right + "}";
    }
}