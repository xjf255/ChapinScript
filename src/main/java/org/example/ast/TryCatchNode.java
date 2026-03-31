package org.example.ast;

public class TryCatchNode extends StatementNode {
    private final BlockNode tryBlock;
    private final ParamNode catchParam;
    private final BlockNode catchBlock;

    public TryCatchNode(int line, int column,BlockNode tryBlock, ParamNode catchParam, BlockNode catchBlock) {
        super(line,column);
        this.tryBlock = tryBlock;
        this.catchParam = catchParam;
        this.catchBlock = catchBlock;
    }

    public BlockNode getTryBlock() {
        return tryBlock;
    }

    public ParamNode getCatchParam() {
        return catchParam;
    }

    public BlockNode getCatchBlock() {
        return catchBlock;
    }

    @Override
    public String toString() {
        return "TryCatchNode{tryBlock=" + tryBlock + ", catchParam=" + catchParam + ", catchBlock=" + catchBlock + "}";
    }
}