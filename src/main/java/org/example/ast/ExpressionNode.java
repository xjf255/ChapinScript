package org.example.ast;

public abstract class ExpressionNode extends ASTNode {
    protected ExpressionNode(int line, int column) {
        super(line, column);
    }
}