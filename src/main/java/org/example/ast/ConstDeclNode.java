package org.example.ast;

public class ConstDeclNode extends DeclarationNode {
    private final String type;
    private final String name;
    private final ExpressionNode value;

    public ConstDeclNode(int line, int column,String type, String name, ExpressionNode value) {
        super(line, column);
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public ExpressionNode getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ConstDeclNode{type='" + type + "', name='" + name + "', value=" + value + "}";
    }
}