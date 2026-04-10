package org.example.semantic;
import org.example.ast.*;

public interface ASTVisitor<T> {

    T visit(ProgramNode node);
    T visit(BlockNode node);
    T visit(StatementNode node);
    T visit(ExpressionStmtNode node);
    T visit(IfNode node);
    T visit(WhileNode node);
    T visit(ForNode node);
    T visit(ReturnNode node);
    T visit(BreakNode node);
    T visit(ContinueNode node);
    T visit(SwitchNode node);
    T visit(CaseNode node);
    T visit(TryCatchNode node);
    T visit(ThrowNode node);
    T visit(VarDeclNode node);
    T visit(ConstDeclNode node);
    T visit(FunctionDeclNode node);
    T visit(ClassDeclNode node);
    T visit(ConstructorDeclNode node);
    T visit(ParamNode node);
    T visit(BinaryOpNode node);
    T visit(UnaryOpNode node);
    T visit(LiteralNode node);
    T visit(IdentifierNode node);
    T visit(CallNode node);
    T visit(MemberAccessNode node);
    T visit(MemberCallNode node);
    T visit(NewObjectNode node);
    T visit(TernaryNode node);
    T visit(ThisNode node);
    T visit(ArrayDeclNode node);
    T visit(DeclarationNode node);
    T visit(ExpressionNode node);
}