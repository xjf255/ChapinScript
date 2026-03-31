package org.example.ast;

public class LiteralNode extends ExpressionNode {
    private final Object value;

    public LiteralNode(int line, int column,Object value) {
        super(line,column);
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "LiteralNode{value=" + value + "}";
    }
}