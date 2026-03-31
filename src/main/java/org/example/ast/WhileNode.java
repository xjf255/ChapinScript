package org.example.ast;

public class WhileNode extends StatementNode {
    private final ExpressionNode condition;
    private final BlockNode body;

    public WhileNode(int line, int column,ExpressionNode condition, BlockNode body) {
        super(line, column);
        this.condition = condition;
        this.body = body;
    }

    public ExpressionNode getCondition() {
        return condition;
    }

    public BlockNode getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "WhileNode{condition=" + condition + ", body=" + body + "}";
    }
}