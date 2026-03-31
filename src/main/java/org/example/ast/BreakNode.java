package org.example.ast;

public class BreakNode extends StatementNode {

    protected BreakNode(int line, int column) {
        super(line, column);
    }

    @Override
    public String toString() {
        return "BreakNode{}";
    }
}