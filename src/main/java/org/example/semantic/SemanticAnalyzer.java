package org.example.semantic;

import org.example.ast.*;

import java.util.ArrayList;
import java.util.List;

public class SemanticAnalyzer implements ASTVisitor<Type> {

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

    private void visitVarDeclaration(VarDeclNode node) {
        Type declaredType = mapType(node.getType());

        SymbolInfo symbol = new SymbolInfo(
                node.getName(),
                declaredType,
                "variable",
                node.getLine(),
                node.getColumn()
        );

        if (!symbolTable.define(symbol)) {
            report("La variable '" + node.getName() + "' ya fue declarada en este scope",
                    node.getLine(), node.getColumn());
            return;
        }

        if (node.getInitializer() != null) {
            Type exprType = evaluateExpression(node.getInitializer());

            if (!isAssignable(declaredType, exprType)) {
                report("No se puede asignar un valor de tipo " + exprType +
                                " a una variable de tipo " + declaredType,
                        node.getLine(), node.getColumn());
            }
        }
    }

    private void visitAssignment(AssignmentNode node) {
        Symbol symbol = symbolTable.resolve(node.getVariableName());

        if (symbol == null) {
            report("La variable '" + node.getVariableName() + "' no ha sido declarada",
                    node.getLine(), node.getColumn());
            evaluateExpression(node.getExpression());
            return;
        }

        Type valueType = evaluateExpression(node.getExpression());

        if (!isAssignable(symbol.getType(), valueType)) {
            report("No se puede asignar un valor de tipo " + valueType +
                            " a la variable '" + node.getVariableName() +
                            "' de tipo " + symbol.getType(),
                    node.getLine(), node.getColumn());
        }
    }

    private Type evaluateExpression(ExpressionNode node) {
        if (node instanceof IntegerLiteralNode) {
            return Type.INT;
        }

        if (node instanceof DecimalLiteralNode) {
            return Type.DOUBLE;
        }

        if (node instanceof StringLiteralNode) {
            return Type.STRING;
        }

        if (node instanceof BooleanLiteralNode) {
            return Type.BOOL;
        }

        if (node instanceof CharLiteralNode) {
            return Type.CHAR;
        }

        if (node instanceof IdentifierNode idNode) {
            Symbol symbol = symbolTable.resolve(idNode.getName());

            if (symbol == null) {
                report("El identificador '" + idNode.getName() + "' no ha sido declarado",
                        idNode.getLine(), idNode.getColumn());
                return Type.ERROR;
            }

            return symbol.getType();
        }

        if (node instanceof BinaryExpressionNode binNode) {
            return evaluateBinaryExpression(binNode);
        }

        if (node instanceof UnaryExpressionNode unaryNode) {
            return evaluateUnaryExpression(unaryNode);
        }

        if (node instanceof FunctionCallNode callNode) {
            return evaluateFunctionCall(callNode);
        }

        return Type.ERROR;
    }

    private Type evaluateBinaryExpression(BinaryExpressionNode node) {
        Type left = evaluateExpression(node.getLeft());
        Type right = evaluateExpression(node.getRight());
        String op = node.getOperator();

        if (left == Type.ERROR || right == Type.ERROR) {
            return Type.ERROR;
        }

        switch (op) {
            case "+", "-", "*", "/" -> {
                if (isNumeric(left) && isNumeric(right)) {
                    return promoteNumericType(left, right);
                }

                if (op.equals("+") && (left == Type.STRING || right == Type.STRING)) {
                    return Type.STRING;
                }

                report("Operación aritmética inválida entre " + left + " y " + right,
                        node.getLine(), node.getColumn());
                return Type.ERROR;
            }

            case "==", "!=", "<", "<=", ">", ">=" -> {
                if (areComparable(left, right)) {
                    return Type.BOOL;
                }

                report("Comparación inválida entre " + left + " y " + right,
                        node.getLine(), node.getColumn());
                return Type.ERROR;
            }

            case "&&", "||" -> {
                if (left == Type.BOOL && right == Type.BOOL) {
                    return Type.BOOL;
                }

                report("Operación lógica inválida entre " + left + " y " + right,
                        node.getLine(), node.getColumn());
                return Type.ERROR;
            }

            default -> {
                report("Operador desconocido: " + op, node.getLine(), node.getColumn());
                return Type.ERROR;
            }
        }
    }

    private void visitIf(IfNode node) {
        Type conditionType = evaluateExpression(node.getCondition());

        if (conditionType != Type.BOOL && conditionType != Type.ERROR) {
            report("La condición del if debe ser de tipo BOOL",
                    node.getLine(), node.getColumn());
        }

        symbolTable.enterScope("if");
        for (ASTNode stmt : node.getThenBlock()) {
            visitStatement(stmt);
        }
        symbolTable.exitScope();

        if (node.getElseBlock() != null) {
            symbolTable.enterScope("else");
            for (ASTNode stmt : node.getElseBlock()) {
                visitStatement(stmt);
            }
            symbolTable.exitScope();
        }
    }

    private void visitWhile(WhileNode node) {
        Type conditionType = evaluateExpression(node.getCondition());

        if (conditionType != Type.BOOL && conditionType != Type.ERROR) {
            report("La condición del while debe ser de tipo BOOL",
                    node.getLine(), node.getColumn());
        }

        symbolTable.enterScope("while");
        for (ASTNode stmt : node.getBody()) {
            visitStatement(stmt);
        }
        symbolTable.exitScope();
    }

    private void visitFunctionDeclaration(FunctionDeclarationNode node) {
        Type returnType = mapType(node.getReturnTypeName());

        List<Type> paramTypes = new ArrayList<>();
        for (ParameterNode param : node.getParameters()) {
            paramTypes.add(mapType(param.getTypeName()));
        }

        Symbol functionSymbol = new Symbol(
                node.getName(),
                returnType,
                "function",
                node.getLine(),
                node.getColumn(),
                paramTypes
        );

        if (!symbolTable.define(functionSymbol)) {
            report("La función '" + node.getName() + "' ya fue declarada",
                    node.getLine(), node.getColumn());
            return;
        }

        symbolTable.enterScope("function:" + node.getName());

        for (ParameterNode param : node.getParameters()) {
            Type paramType = mapType(param.getTypeName());
            Symbol paramSymbol = new Symbol(
                    param.getName(),
                    paramType,
                    "parameter",
                    param.getLine(),
                    param.getColumn()
            );

            if (!symbolTable.define(paramSymbol)) {
                report("El parámetro '" + param.getName() + "' ya fue declarado",
                        param.getLine(), param.getColumn());
            }
        }

        Type previousReturnType = currentFunctionReturnType;
        currentFunctionReturnType = returnType;

        for (ASTNode stmt : node.getBody()) {
            visitStatement(stmt);
        }

        currentFunctionReturnType = previousReturnType;
        symbolTable.exitScope();
    }

    private void visitFunctionDeclaration(FunctionDeclarationNode node) {
        Type returnType = mapType(node.getReturnTypeName());

        List<Type> paramTypes = new ArrayList<>();
        for (ParameterNode param : node.getParameters()) {
            paramTypes.add(mapType(param.getTypeName()));
        }

        Symbol functionSymbol = new Symbol(
                node.getName(),
                returnType,
                "function",
                node.getLine(),
                node.getColumn(),
                paramTypes
        );

        if (!symbolTable.define(functionSymbol)) {
            report("La función '" + node.getName() + "' ya fue declarada",
                    node.getLine(), node.getColumn());
            return;
        }

        symbolTable.enterScope("function:" + node.getName());

        for (ParameterNode param : node.getParameters()) {
            Type paramType = mapType(param.getTypeName());
            Symbol paramSymbol = new Symbol(
                    param.getName(),
                    paramType,
                    "parameter",
                    param.getLine(),
                    param.getColumn()
            );

            if (!symbolTable.define(paramSymbol)) {
                report("El parámetro '" + param.getName() + "' ya fue declarado",
                        param.getLine(), param.getColumn());
            }
        }

        Type previousReturnType = currentFunctionReturnType;
        currentFunctionReturnType = returnType;

        for (ASTNode stmt : node.getBody()) {
            visitStatement(stmt);
        }

        currentFunctionReturnType = previousReturnType;
        symbolTable.exitScope();
    }

    private Type evaluateFunctionCall(FunctionCallNode node) {
        Symbol symbol = symbolTable.resolve(node.getFunctionName());

        if (symbol == null || !symbol.isFunction()) {
            report("La función '" + node.getFunctionName() + "' no ha sido declarada",
                    node.getLine(), node.getColumn());
            return Type.ERROR;
        }

        List<Type> expectedParams = symbol.getParameterTypes();
        List<ExpressionNode> args = node.getArguments();

        if (expectedParams.size() != args.size()) {
            report("La función '" + node.getFunctionName() + "' esperaba " +
                            expectedParams.size() + " parámetros pero recibió " + args.size(),
                    node.getLine(), node.getColumn());
            return symbol.getType();
        }

        for (int i = 0; i < args.size(); i++) {
            Type argType = evaluateExpression(args.get(i));
            Type expectedType = expectedParams.get(i);

            if (!isAssignable(expectedType, argType)) {
                report("Tipo inválido en parámetro " + (i + 1) +
                                " de la función '" + node.getFunctionName() +
                                "'. Se esperaba " + expectedType + " pero se recibió " + argType,
                        node.getLine(), node.getColumn());
            }
        }

        return symbol.getType();
    }

    private Type mapType(String typeName) {
        return switch (typeName.toLowerCase()) {
            case "int" -> Type.INT;
            case "float" -> Type.FLOAT;
            case "double" -> Type.DOUBLE;
            case "char" -> Type.CHAR;
            case "bool" -> Type.BOOL;
            case "string" -> Type.STRING;
            case "void" -> Type.VOID;
            default -> Type.ERROR;
        };
    }

    private boolean isNumeric(Type type) {
        return type == Type.INT || type == Type.FLOAT || type == Type.DOUBLE;
    }

    private Type promoteNumericType(Type a, Type b) {
        if (a == Type.DOUBLE || b == Type.DOUBLE) return Type.DOUBLE;
        if (a == Type.FLOAT || b == Type.FLOAT) return Type.FLOAT;
        return Type.INT;
    }

    private boolean areComparable(Type a, Type b) {
        if (a == b) return true;
        return isNumeric(a) && isNumeric(b);
    }
}