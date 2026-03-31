package org.example.ast;

import java.util.List;

public class ClassDeclNode extends StatementNode {
    private final String name;
    private final List<ASTNode> members;

    public ClassDeclNode(int line, int column,String name, List<ASTNode> members) {
        super(line, column);
        this.name = name;
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public List<ASTNode> getMembers() {
        return members;
    }

    @Override
    public String toString() {
        return "ClassDeclNode{name='" + name + "', members=" + members + "}";
    }
}