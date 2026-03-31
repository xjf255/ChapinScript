package org.example.ast;

public abstract class StatementNode extends ASTNode {
    protected StatementNode(int line, int column) {
        super(line, column);
    }
}