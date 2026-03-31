package org.example.ast;

import java.util.List;

public class ArrayDeclNode extends DeclarationNode {
    private final String accessModifier;
    private final String type;
    private final String name;
    private final Integer size;
    private final List<ExpressionNode> values;

    public ArrayDeclNode(int line, int column, String accessModifier, String type, String name, Integer size, List<ExpressionNode> values) {
        super(line, column);
        this.accessModifier = accessModifier;
        this.type = type;
        this.name = name;
        this.size = size;
        this.values = values;
    }

    public String getAccessModifier() {
        return accessModifier;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Integer getSize() {
        return size;
    }

    public List<ExpressionNode> getValues() {
        return values;
    }

    @Override
    public String toString() {
        return "ArrayDeclNode{accessModifier='" + accessModifier + "', type='" + type + "', name='" + name +
                "', size=" + size + ", values=" + values + "}";
    }
}