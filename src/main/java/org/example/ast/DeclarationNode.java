package org.example.ast;

public abstract class DeclarationNode extends StatementNode {
    protected DeclarationNode(int line, int column) {
        super(line, column);
    }
}