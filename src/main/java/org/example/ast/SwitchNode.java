package org.example.ast;

import java.util.List;

public class SwitchNode extends StatementNode {
    private final ExpressionNode expression;
    private final List<CaseNode> cases;

    public SwitchNode(int line, int column,ExpressionNode expression, List<CaseNode> cases) {
        super(line, column);
        this.expression = expression;
        this.cases = cases;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    public List<CaseNode> getCases() {
        return cases;
    }

    @Override
    public String toString() {
        return "SwitchNode{expression=" + expression + ", cases=" + cases + "}";
    }
}