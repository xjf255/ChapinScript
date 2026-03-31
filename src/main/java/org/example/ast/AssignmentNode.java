package org.example.ast;

public class AssignmentNode extends ExpressionNode {
    private final ExpressionNode target;
    private final ExpressionNode value;

    public AssignmentNode(int line, int column, ExpressionNode target, ExpressionNode value) {
        super(line, column);
        this.target = target;
        this.value = value;
    }

    public ExpressionNode getTarget() {
        return target;
    }

    public ExpressionNode getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "AssignmentNode{target=" + target + ", value=" + value + "}";
    }
}