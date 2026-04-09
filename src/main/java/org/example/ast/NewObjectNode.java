package org.example.ast;

import java.util.List;

public class NewObjectNode extends ExpressionNode {
    private final String className;
    private final List<ExpressionNode> arguments;

    public NewObjectNode(int line, int column, String className, List<ExpressionNode> arguments) {
        super(line, column);
        this.className = className;
        this.arguments = arguments;
    }

    public String getClassName() {
        return className;
    }

    public List<ExpressionNode> getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        return "NewObjectNode{" +
                "className='" + className + '\'' +
                ", arguments=" + arguments +
                '}';
    }
}