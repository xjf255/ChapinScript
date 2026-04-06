package org.example.ast;

public class ErrorStmtNode extends StatementNode {

    public ErrorStmtNode(int line, int column) {
        super(line, column);
    }

    @Override
    public String toString() {
        return "ErrorStmtNode{}";
    }
}