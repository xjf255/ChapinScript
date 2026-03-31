package org.example.ast;

public class ArrayAccessNode extends ExpressionNode {
    private final ExpressionNode array;
    private final ExpressionNode index;

    public ArrayAccessNode(int line,int column,ExpressionNode array, ExpressionNode index) {
        super(line,column);
        this.array = array;
        this.index = index;
    }

    public ExpressionNode getArray() {
        return array;
    }

    public ExpressionNode getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "ArrayAccessNode{array=" + array + ", index=" + index + "}";
    }
}