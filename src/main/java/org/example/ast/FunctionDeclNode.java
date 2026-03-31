package org.example.ast;

import java.util.List;

public class FunctionDeclNode extends DeclarationNode {
    private final String accessModifier;
    private final String returnType;
    private final String name;
    private final List<ParamNode> params;
    private final BlockNode body;

    public FunctionDeclNode(int line, int column,String accessModifier, String returnType, String name, List<ParamNode> params, BlockNode body) {
        super(line, column);
        this.accessModifier = accessModifier;
        this.returnType = returnType;
        this.name = name;
        this.params = params;
        this.body = body;
    }

    public String getAccessModifier() {
        return accessModifier;
    }

    public String getReturnType() {
        return returnType;
    }

    public String getName() {
        return name;
    }

    public List<ParamNode> getParams() {
        return params;
    }

    public BlockNode getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "FunctionDeclNode{accessModifier='" + accessModifier + "', returnType='" + returnType +
                "', name='" + name + "', params=" + params + ", body=" + body + "}";
    }
}