package org.example.ast;

import java.util.List;

public class ConstructorDeclNode extends ASTNode {
    private final String name;
    private final List<ParamNode> params;
    private final BlockNode body;

    public ConstructorDeclNode(int line, int column,String name, List<ParamNode> params, BlockNode body) {
        super(line, column);
        this.name = name;
        this.params = params;
        this.body = body;
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
        return "ConstructorDeclNode{name='" + name + "', params=" + params + ", body=" + body + "}";
    }
}