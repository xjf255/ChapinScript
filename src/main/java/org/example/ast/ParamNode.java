package org.example.ast;

public class ParamNode extends ASTNode {
    private final String type;
    private final String name;
    private final LiteralNode defaultValue;

    public ParamNode(int line, int column,String type, String name, LiteralNode defaultValue) {
        super(line,column);
        this.type = type;
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public LiteralNode getDefaultValue() {
        return defaultValue;
    }

    @Override
    public String toString() {
        return "ParamNode{type='" + type + "', name='" + name + "', defaultValue=" + defaultValue + "}";
    }
}