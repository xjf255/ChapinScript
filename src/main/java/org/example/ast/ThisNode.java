package org.example.ast;

public class ThisNode extends ExpressionNode {

    public ThisNode(int line, int column) {
        super(line, column);
    }

    @Override
    public String toString() {
        return "ThisNode{}";
    }
}