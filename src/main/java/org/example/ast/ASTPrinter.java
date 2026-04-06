package org.example.ast;

import java.util.List;

public final class ASTPrinter {

    private ASTPrinter() {
    }

    public static void print(ASTNode node) {
        print(node, 0);
    }

    public static String format(ASTNode node, int indent) {
        StringBuilder sb = new StringBuilder();
        format(node, indent, sb);
        return sb.toString();
    }

    private static void print(ASTNode node, int indent) {
        System.out.print(format(node, indent));
    }

    private static void format(ASTNode node, int indent, StringBuilder sb) {
        if (node == null) {
            line(sb, indent, "null");
            return;
        }

        if (node instanceof ProgramNode n) {
            line(sb, indent, label("Program", n));
            for (StatementNode stmt : n.getStatements()) {
                format(stmt, indent + 1, sb);
            }
            return;
        }

        if (node instanceof BlockNode n) {
            line(sb, indent, label("Block", n));
            for (StatementNode stmt : n.getStatements()) {
                format(stmt, indent + 1, sb);
            }
            return;
        }

        if (node instanceof VarDeclNode n) {
            line(sb, indent, label(
                    "VarDecl type=" + safe(n.getType()) +
                            " name=" + safe(n.getName()) +
                            " access=" + safe(n.getAccessModifier()), n));
            if (n.getInitializer() != null) {
                line(sb, indent + 1, "Initializer:");
                format(n.getInitializer(), indent + 2, sb);
            }
            return;
        }

        if (node instanceof ConstDeclNode n) {
            line(sb, indent, label(
                    "ConstDecl type=" + safe(n.getType()) +
                            " name=" + safe(n.getName()), n));
            line(sb, indent + 1, "Value:");
            format(n.getValue(), indent + 2, sb);
            return;
        }

        if (node instanceof FunctionDeclNode n) {
            line(sb, indent, label(
                    "FunctionDecl returnType=" + safe(n.getReturnType()) +
                            " name=" + safe(n.getName()) +
                            " access=" + safe(n.getAccessModifier()), n));

            line(sb, indent + 1, "Params:");
            if (n.getParams() == null || n.getParams().isEmpty()) {
                line(sb, indent + 2, "(empty)");
            } else {
                for (ParamNode param : n.getParams()) {
                    format(param, indent + 2, sb);
                }
            }

            line(sb, indent + 1, "Body:");
            format(n.getBody(), indent + 2, sb);
            return;
        }

        if (node instanceof ArrayDeclNode n) {
            line(sb, indent, label(
                    "ArrayDecl type=" + safe(n.getType()) +
                            " name=" + safe(n.getName()) +
                            " size=" + n.getSize() +
                            " access=" + safe(n.getAccessModifier()), n));

            line(sb, indent + 1, "Values:");
            if (n.getValues() == null || n.getValues().isEmpty()) {
                line(sb, indent + 2, "(empty)");
            } else {
                for (ExpressionNode value : n.getValues()) {
                    format(value, indent + 2, sb);
                }
            }
            return;
        }

        if (node instanceof ClassDeclNode n) {
            line(sb, indent, label("ClassDecl name=" + safe(n.getName()), n));
            line(sb, indent + 1, "Members:");
            if (n.getMembers() == null || n.getMembers().isEmpty()) {
                line(sb, indent + 2, "(empty)");
            } else {
                for (ASTNode member : n.getMembers()) {
                    format(member, indent + 2, sb);
                }
            }
            return;
        }

        if (node instanceof ConstructorDeclNode n) {
            line(sb, indent, label("ConstructorDecl name=" + safe(n.getName()), n));

            line(sb, indent + 1, "Params:");
            if (n.getParams() == null || n.getParams().isEmpty()) {
                line(sb, indent + 2, "(empty)");
            } else {
                for (ParamNode param : n.getParams()) {
                    format(param, indent + 2, sb);
                }
            }

            line(sb, indent + 1, "Body:");
            format(n.getBody(), indent + 2, sb);
            return;
        }

        if (node instanceof ParamNode n) {
            line(sb, indent, label(
                    "Param type=" + safe(n.getType()) +
                            " name=" + safe(n.getName()), n));
            if (n.getDefaultValue() != null) {
                line(sb, indent + 1, "DefaultValue:");
                format(n.getDefaultValue(), indent + 2, sb);
            }
            return;
        }

        if (node instanceof IfNode n) {
            line(sb, indent, label("If", n));

            line(sb, indent + 1, "Condition:");
            format(n.getCondition(), indent + 2, sb);

            line(sb, indent + 1, "Then:");
            format(n.getThenBlock(), indent + 2, sb);

            if (n.getElseBranch() != null) {
                line(sb, indent + 1, "Else:");
                format(n.getElseBranch(), indent + 2, sb);
            }
            return;
        }

        if (node instanceof WhileNode n) {
            line(sb, indent, label("While", n));

            line(sb, indent + 1, "Condition:");
            format(n.getCondition(), indent + 2, sb);

            line(sb, indent + 1, "Body:");
            format(n.getBody(), indent + 2, sb);
            return;
        }

        if (node instanceof ForNode n) {
            line(sb, indent, label("For", n));

            line(sb, indent + 1, "Init:");
            format(n.getInit(), indent + 2, sb);

            line(sb, indent + 1, "Condition:");
            format(n.getCondition(), indent + 2, sb);

            line(sb, indent + 1, "Update:");
            format(n.getUpdate(), indent + 2, sb);

            line(sb, indent + 1, "Body:");
            format(n.getBody(), indent + 2, sb);
            return;
        }

        if (node instanceof DoWhileNode n) {
            line(sb, indent, label("DoWhile", n));

            line(sb, indent + 1, "Body:");
            format(n.getBody(), indent + 2, sb);

            line(sb, indent + 1, "Condition:");
            format(n.getCondition(), indent + 2, sb);
            return;
        }

        if (node instanceof SwitchNode n) {
            line(sb, indent, label("Switch", n));

            line(sb, indent + 1, "Expression:");
            format(n.getExpression(), indent + 2, sb);

            line(sb, indent + 1, "Cases:");
            if (n.getCases() == null || n.getCases().isEmpty()) {
                line(sb, indent + 2, "(empty)");
            } else {
                for (CaseNode c : n.getCases()) {
                    format(c, indent + 2, sb);
                }
            }
            return;
        }

        if (node instanceof CaseNode n) {
            line(sb, indent, label("Case default=" + n.isDefault(), n));

            line(sb, indent + 1, "Value:");
            format(n.getValue(), indent + 2, sb);

            line(sb, indent + 1, "Statements:");
            if (n.getStatements() == null || n.getStatements().isEmpty()) {
                line(sb, indent + 2, "(empty)");
            } else {
                for (StatementNode stmt : n.getStatements()) {
                    format(stmt, indent + 2, sb);
                }
            }
            return;
        }

        if (node instanceof TryCatchNode n) {
            line(sb, indent, label("TryCatch", n));

            line(sb, indent + 1, "TryBlock:");
            format(n.getTryBlock(), indent + 2, sb);

            line(sb, indent + 1, "CatchParam:");
            format(n.getCatchParam(), indent + 2, sb);

            line(sb, indent + 1, "CatchBlock:");
            format(n.getCatchBlock(), indent + 2, sb);
            return;
        }

        if (node instanceof ThrowNode n) {
            line(sb, indent, label("Throw", n));
            line(sb, indent + 1, "Expression:");
            format(n.getExpression(), indent + 2, sb);
            return;
        }

        if (node instanceof ReturnNode n) {
            line(sb, indent, label("Return", n));
            if (n.getExpression() != null) {
                line(sb, indent + 1, "Expression:");
                format(n.getExpression(), indent + 2, sb);
            }
            return;
        }

        if (node instanceof BreakNode n) {
            line(sb, indent, label("Break", n));
            return;
        }

        if (node instanceof ContinueNode n) {
            line(sb, indent, label("Continue", n));
            return;
        }

        if (node instanceof PrintNode n) {
            line(sb, indent, label("Print", n));
            line(sb, indent + 1, "Expression:");
            format(n.getExpression(), indent + 2, sb);
            return;
        }

        if (node instanceof ExpressionStmtNode n) {
            line(sb, indent, label("ExpressionStmt", n));
            line(sb, indent + 1, "Expression:");
            format(n.getExpression(), indent + 2, sb);
            return;
        }

        if (node instanceof ErrorStmtNode n) {
            line(sb, indent, label("ErrorStmt", n));
            return;
        }

        if (node instanceof AssignmentNode n) {
            line(sb, indent, label("Assignment", n));

            line(sb, indent + 1, "Target:");
            format(n.getTarget(), indent + 2, sb);

            line(sb, indent + 1, "Value:");
            format(n.getValue(), indent + 2, sb);
            return;
        }

        if (node instanceof TernaryNode n) {
            line(sb, indent, label("Ternary", n));

            line(sb, indent + 1, "Condition:");
            format(n.getCondition(), indent + 2, sb);

            line(sb, indent + 1, "TrueExpr:");
            format(n.getTrueExpr(), indent + 2, sb);

            line(sb, indent + 1, "FalseExpr:");
            format(n.getFalseExpr(), indent + 2, sb);
            return;
        }

        if (node instanceof BinaryOpNode n) {
            line(sb, indent, label("BinaryOp operator=" + safe(n.getOperator()), n));

            line(sb, indent + 1, "Left:");
            format(n.getLeft(), indent + 2, sb);

            line(sb, indent + 1, "Right:");
            format(n.getRight(), indent + 2, sb);
            return;
        }

        if (node instanceof UnaryOpNode n) {
            line(sb, indent, label("UnaryOp operator=" + safe(n.getOperator()), n));

            line(sb, indent + 1, "Expression:");
            format(n.getExpression(), indent + 2, sb);
            return;
        }

        if (node instanceof LiteralNode n) {
            line(sb, indent, label("Literal value=" + safeValue(n.getValue()), n));
            return;
        }

        if (node instanceof IdentifierNode n) {
            line(sb, indent, label("Identifier name=" + safe(n.getName()), n));
            return;
        }

        if (node instanceof ThisNode n) {
            line(sb, indent, label("This", n));
            return;
        }

        if (node instanceof MemberAccessNode n) {
            line(sb, indent, label("MemberAccess member=" + safe(n.getMember()), n));

            line(sb, indent + 1, "Target:");
            format(n.getTarget(), indent + 2, sb);
            return;
        }

        if (node instanceof CallNode n) {
            line(sb, indent, label("Call function=" + safe(n.getFunctionName()), n));

            line(sb, indent + 1, "Arguments:");
            if (n.getArguments() == null || n.getArguments().isEmpty()) {
                line(sb, indent + 2, "(empty)");
            } else {
                for (ExpressionNode arg : n.getArguments()) {
                    format(arg, indent + 2, sb);
                }
            }
            return;
        }

        if (node instanceof MemberCallNode n) {
            line(sb, indent, label("MemberCall", n));

            line(sb, indent + 1, "Target:");
            format(n.getTarget(), indent + 2, sb);

            line(sb, indent + 1, "Call:");
            format(n.getCall(), indent + 2, sb);
            return;
        }

        if (node instanceof ArrayAccessNode n) {
            line(sb, indent, label("ArrayAccess", n));

            line(sb, indent + 1, "Array:");
            format(n.getArray(), indent + 2, sb);

            line(sb, indent + 1, "Index:");
            format(n.getIndex(), indent + 2, sb);
            return;
        }

        line(sb, indent, label(node.getClass().getSimpleName(), node));
    }

    private static void line(StringBuilder sb, int indent, String text) {
        sb.append("  ".repeat(Math.max(0, indent)))
                .append(text)
                .append(System.lineSeparator());
    }

    private static String label(String name, ASTNode node) {
        return name + pos(node);
    }

    private static String pos(ASTNode node) {
        return " [line=" + node.getLine() + ", col=" + node.getColumn() + "]";
    }

    private static String safe(String value) {
        return value == null ? "null" : value;
    }

    private static String safeValue(Object value) {
        if (value == null) return "null";
        if (value instanceof String s) return "\"" + s + "\"";
        return value.toString();
    }
}