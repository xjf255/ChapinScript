package org.example.ast;

public class ForNode extends StatementNode {
    private final ASTNode init;
    private final ExpressionNode condition;
    private final ExpressionNode update;
    private final BlockNode body;

    public ForNode(int line, int column,ASTNode init, ExpressionNode condition, ExpressionNode update, BlockNode body) {
        super(line, column);
        this.init = init;
        this.condition = condition;
        this.update = update;
        this.body = body;
    }

    public ASTNode getInit() {
        return init;
    }

    public ExpressionNode getCondition() {
        return condition;
    }

    public ExpressionNode getUpdate() {
        return update;
    }

    public BlockNode getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "ForNode{init=" + init + ", condition=" + condition + ", update=" + update + ", body=" + body + "}";
    }
}