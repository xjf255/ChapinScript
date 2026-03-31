package org.example.ast;

public class IdentifierNode extends ExpressionNode {
    private final String name;

    public IdentifierNode(int line, int column,String name) {
        super(line,column);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "IdentifierNode{name='" + name + "'}";
    }
}