package org.example.ast;

public class VarDeclNode extends DeclarationNode {
    private final String accessModifier;
    private final String type;
    private final String name;
    private final ExpressionNode initializer;

    public VarDeclNode(int line, int column,String accessModifier, String type, String name, ExpressionNode initializer) {
        super(line, column);
        this.accessModifier = accessModifier;
        this.type = type;
        this.name = name;
        this.initializer = initializer;
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

    public ExpressionNode getInitializer() {
        return initializer;
    }

    @Override
    public String toString() {
        return "VarDeclNode{accessModifier='" + accessModifier + "', type='" + type + "', name='" + name +
                "', initializer=" + initializer + "}";
    }
}