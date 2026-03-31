package org.example.ast;

public class IfNode extends StatementNode {
    private final ExpressionNode condition;
    private final BlockNode thenBlock;
    private final ASTNode elseBranch;

    public IfNode(int line, int column,ExpressionNode condition, BlockNode thenBlock, ASTNode elseBranch) {
        super(line, column);
        this.condition = condition;
        this.thenBlock = thenBlock;
        this.elseBranch = elseBranch;
    }

    public ExpressionNode getCondition() {
        return condition;
    }

    public BlockNode getThenBlock() {
        return thenBlock;
    }

    public ASTNode getElseBranch() {
        return elseBranch;
    }

    @Override
    public String toString() {
        return "IfNode{condition=" + condition + ", thenBlock=" + thenBlock + ", elseBranch=" + elseBranch + "}";
    }
}