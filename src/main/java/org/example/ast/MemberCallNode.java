package org.example.ast;

public class MemberCallNode extends ExpressionNode {
    private final ExpressionNode target;
    private final CallNode call;

    public MemberCallNode(int line, int column,ExpressionNode target, CallNode call) {
        super(line,column);
        this.target = target;
        this.call = call;
    }

    public ExpressionNode getTarget() {
        return target;
    }

    public CallNode getCall() {
        return call;
    }

    @Override
    public String toString() {
        return "MemberCallNode{target=" + target + ", call=" + call + "}";
    }
}