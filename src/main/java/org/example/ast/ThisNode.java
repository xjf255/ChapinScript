package org.example.ast;

public class ThisNode extends ExpressionNode {

    protected ThisNode(int line, int column) {
        super(line, column);
    }

    @Override
    public String toString() {
        return "ThisNode{}";
    }
}