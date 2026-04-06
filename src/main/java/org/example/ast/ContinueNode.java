package org.example.ast;

public class ContinueNode extends StatementNode {

    public ContinueNode(int line, int column) {
        super(line, column);
    }

    @Override
    public String toString() {
        return "ContinueNode{}";
    }
}