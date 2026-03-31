package org.example.ast;

public class DoWhileNode extends StatementNode {
    private final BlockNode body;
    private final ExpressionNode condition;

    public DoWhileNode(int line, int column,BlockNode body, ExpressionNode condition) {
        super(line, column);
        this.body = body;
        this.condition = condition;
    }

    public BlockNode getBody() {
        return body;
    }

    public ExpressionNode getCondition() {
        return condition;
    }

    @Override
    public String toString() {
        return "DoWhileNode{body=" + body + ", condition=" + condition + "}";
    }
}