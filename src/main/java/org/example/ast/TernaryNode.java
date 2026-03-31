package org.example.ast;

public class TernaryNode extends ExpressionNode {
    private final ExpressionNode condition;
    private final ExpressionNode trueExpr;
    private final ExpressionNode falseExpr;

    public TernaryNode(int line, int column,ExpressionNode condition, ExpressionNode trueExpr, ExpressionNode falseExpr) {
        super(line,column);
        this.condition = condition;
        this.trueExpr = trueExpr;
        this.falseExpr = falseExpr;
    }

    public ExpressionNode getCondition() {
        return condition;
    }

    public ExpressionNode getTrueExpr() {
        return trueExpr;
    }

    public ExpressionNode getFalseExpr() {
        return falseExpr;
    }

    @Override
    public String toString() {
        return "TernaryNode{condition=" + condition + ", trueExpr=" + trueExpr + ", falseExpr=" + falseExpr + "}";
    }
}