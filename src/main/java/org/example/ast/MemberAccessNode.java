package org.example.ast;

public class MemberAccessNode extends ExpressionNode {
    private final ExpressionNode target;
    private final String member;

    public MemberAccessNode(int line, int column,ExpressionNode target, String member) {
        super(line,column);
        this.target = target;
        this.member = member;
    }

    public ExpressionNode getTarget() {
        return target;
    }

    public String getMember() {
        return member;
    }

    @Override
    public String toString() {
        return "MemberAccessNode{target=" + target + ", member='" + member + "'}";
    }
}