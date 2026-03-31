package org.example.ast;

import java.util.List;

public class ProgramNode extends ASTNode {
    private final List<StatementNode> statements;

    public ProgramNode(List<StatementNode> statements) {
        super(1, 1);
        this.statements = statements;
    }

    public List<StatementNode> getStatements() {
        return statements;
    }

    @Override
    public String toString() {
        return "ProgramNode{statements=" + statements + "}";
    }
}