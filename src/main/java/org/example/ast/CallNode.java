package org.example.ast;

import java.util.List;

public class CallNode extends ExpressionNode {
    private final String functionName;
    private final List<ExpressionNode> arguments;

    public CallNode(int line, int column, String functionName, List<ExpressionNode> arguments) {
        super(line, column);
        this.functionName = functionName;
        this.arguments = arguments;
    }

    public String getFunctionName() {
        return functionName;
    }

    public List<ExpressionNode> getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        return "CallNode{functionName='" + functionName + "', arguments=" + arguments + "}";
    }
}