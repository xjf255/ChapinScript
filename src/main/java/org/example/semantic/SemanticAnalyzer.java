package org.example.semantic;

import org.example.ast.*;

import java.util.ArrayList;
import java.util.List;

public class SemanticAnalyzer {

    private final SymbolTable symbolTable = new SymbolTable();
    private final List<SemanticError> errors = new ArrayList<>();
    private Type currentFunctionReturnType = null;

    public void analyze(ProgramNode program) {
        symbolTable.enterScope("global");
        visit(program);
        symbolTable.exitScope();
    }

    public List<SemanticError> getErrors() {
        return errors;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    private void report(String message, int line, int column) {
        errors.add(new SemanticError(message, line, column));
    }

    private void visit(ProgramNode node) {
        for (ASTNode stmt : node.getStatements()) {
            visitStatement(stmt);
        }
    }

    private void visitStatement(ASTNode node) {
        if (node instanceof VarDeclNode varDecl) {
            visitVarDeclaration(varDecl);
        } else if (node instanceof AssignmentNode assign) {
            visitAssignment(assign);
        } else if (node instanceof IfNode ifNode) {
            visitIf(ifNode);
        } else if (node instanceof WhileNode whileNode) {
            visitWhile(whileNode);
        } else if (node instanceof FunctionDeclNode fnNode) {
            visitFunctionDeclaration(fnNode);
        } else if (node instanceof ReturnNode returnNode) {
            visitReturn(returnNode);
        } else if (node instanceof ExpressionStmtNode exprStmt) {
            evaluateExpression(exprStmt.getExpression());
        }
    }
}