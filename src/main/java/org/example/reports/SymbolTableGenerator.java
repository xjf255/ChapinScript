package org.example.reports;

import org.example.lexer.Token;
import org.example.lexer.TokenType;
import org.example.semantic.SymbolInfo;
import org.example.semantic.Type;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class SymbolTableGenerator {

    // ─────────────────────────────────────────────
    // Scope types
    // ─────────────────────────────────────────────

    private enum ScopeKind {
        GLOBAL,
        CLASS,
        FUNCTION,
        BLOCK
    }

    private static class ScopeFrame {

        final String name;
        final ScopeKind kind;

        ScopeFrame(String name, ScopeKind kind) {
            this.name = name;
            this.kind = kind;
        }
    }

    // ─────────────────────────────────────────────
    // Scope stack
    // ─────────────────────────────────────────────

    private final Deque<ScopeFrame> scopeStack = new ArrayDeque<>();

    private void pushScope(String name, ScopeKind kind) {
        scopeStack.push(new ScopeFrame(name, kind));
    }

    private void popScope() {

        // Nunca sacar GLOBAL
        if (scopeStack.size() > 1) {
            scopeStack.pop();
        }
    }

    private String currentScope() {

        if (scopeStack.isEmpty()) {
            return "global";
        }

        List<String> parts = new ArrayList<>();

        for (ScopeFrame frame : scopeStack) {
            parts.add(0, frame.name);
        }

        return String.join("::", parts);
    }

    // ─────────────────────────────────────────────
    // Public API
    // ─────────────────────────────────────────────

    public List<SymbolInfo> generate(List<Token> tokens) {

        scopeStack.clear();
        pushScope("global", ScopeKind.GLOBAL);

        List<SymbolInfo> symbols = new ArrayList<>();

        int i = 0;

        while (i < tokens.size()) {

            Token token = tokens.get(i);

            // EOF
            if (token.getType() == TokenType.EOF) {
                break;
            }

            // ─────────────────────────────────────
            // OPEN {
            // ─────────────────────────────────────

            if (token.getType() == TokenType.LEFT_BRACE) {

                ScopeFrame top = scopeStack.peek();

                // Si es class o function
                // NO crear nuevo block
                if (top != null &&
                        (top.kind == ScopeKind.CLASS
                                || top.kind == ScopeKind.FUNCTION)) {

                    i++;
                    continue;
                }

                // Bloque anónimo
                pushScope("block", ScopeKind.BLOCK);

                i++;
                continue;
            }

            // ─────────────────────────────────────
            // CLOSE }
            // ─────────────────────────────────────

            if (token.getType() == TokenType.RIGHT_BRACE) {

                popScope();

                i++;
                continue;
            }

            // ─────────────────────────────────────
            // CLASS
            // ─────────────────────────────────────

            if (token.getType() == TokenType.CLASS) {

                if (i + 1 < tokens.size()
                        && tokens.get(i + 1).getType() == TokenType.IDENTIFIER) {

                    Token classToken = tokens.get(i + 1);

                    symbols.add(
                            new SymbolInfo(
                                    classToken.getLexeme(),
                                    "clase",
                                    Type.VOID,
                                    currentScope(),
                                    classToken.getLine(),
                                    classToken.getColumn(),
                                    "-"
                            )
                    );

                    pushScope(classToken.getLexeme(), ScopeKind.CLASS);

                    i += 2;
                    continue;
                }
            }

            // ─────────────────────────────────────
            // PUBLIC / PRIVATE
            // ─────────────────────────────────────

            if (token.getType() == TokenType.PUBLIC
                    || token.getType() == TokenType.PRIVATE) {

                i++;
                continue;
            }

            // ─────────────────────────────────────
            // CONST
            // ─────────────────────────────────────

            if (token.getType() == TokenType.CONST) {

                int ni = i + 1;

                if (ni + 1 < tokens.size()
                        && isDataType(tokens.get(ni).getType())
                        && tokens.get(ni + 1).getType() == TokenType.IDENTIFIER) {

                    Token typeToken = tokens.get(ni);
                    Token idToken = tokens.get(ni + 1);

                    String value = extractAssignedValue(tokens, ni + 2);

                    symbols.add(
                            new SymbolInfo(
                                    idToken.getLexeme(),
                                    "constante",
                                    mapType(typeToken),
                                    currentScope(),
                                    idToken.getLine(),
                                    idToken.getColumn(),
                                    value
                            )
                    );

                    i = ni + 2;
                    continue;
                }
            }

            // ─────────────────────────────────────
            // VARIABLES / FUNCIONES / ARREGLOS
            // ─────────────────────────────────────

            if (isDataType(token.getType())
                    || token.getType() == TokenType.VOID) {

                if (i + 1 < tokens.size()
                        && tokens.get(i + 1).getType() == TokenType.IDENTIFIER) {

                    Token typeToken = token;
                    Token idToken = tokens.get(i + 1);

                    // ─────────────────────────────
                    // FUNCTION
                    // ─────────────────────────────

                    if (i + 2 < tokens.size()
                            && tokens.get(i + 2).getType() == TokenType.LEFT_PAREN) {

                        symbols.add(
                                new SymbolInfo(
                                        idToken.getLexeme(),
                                        "función",
                                        mapType(typeToken),
                                        currentScope(),
                                        idToken.getLine(),
                                        idToken.getColumn(),
                                        "-"
                                )
                        );

                        pushScope(idToken.getLexeme(), ScopeKind.FUNCTION);

                        int closeIdx = findClosingParen(tokens, i + 2);

                        if (closeIdx != -1) {

                            symbols.addAll(
                                    extractParameters(
                                            tokens,
                                            i + 3,
                                            closeIdx - 1,
                                            currentScope()
                                    )
                            );

                            i = closeIdx + 1;
                            continue;
                        }
                    }

                    // ─────────────────────────────
                    // ARRAY
                    // ─────────────────────────────

                    if (i + 2 < tokens.size()
                            && tokens.get(i + 2).getType() == TokenType.LEFT_BRACKET) {

                        String value = extractAssignedValue(tokens, i + 2);

                        symbols.add(
                                new SymbolInfo(
                                        idToken.getLexeme(),
                                        "arreglo",
                                        mapType(typeToken),
                                        currentScope(),
                                        idToken.getLine(),
                                        idToken.getColumn(),
                                        value
                                )
                        );

                        i += 3;
                        continue;
                    }

                    // ─────────────────────────────
                    // VARIABLE
                    // ─────────────────────────────

                    String value = extractAssignedValue(tokens, i + 2);

                    symbols.add(
                            new SymbolInfo(
                                    idToken.getLexeme(),
                                    "variable",
                                    mapType(typeToken),
                                    currentScope(),
                                    idToken.getLine(),
                                    idToken.getColumn(),
                                    value
                            )
                    );

                    i += 2;
                    continue;
                }
            }

            // ─────────────────────────────────────
            // CONSTRUCTOR
            // ─────────────────────────────────────

            if (token.getType() == TokenType.IDENTIFIER) {

                ScopeFrame top = scopeStack.peek();

                if (top != null
                        && top.kind == ScopeKind.CLASS
                        && token.getLexeme().equals(top.name)
                        && i + 1 < tokens.size()
                        && tokens.get(i + 1).getType() == TokenType.LEFT_PAREN) {

                    symbols.add(
                            new SymbolInfo(
                                    token.getLexeme(),
                                    "constructor",
                                    Type.VOID,
                                    currentScope(),
                                    token.getLine(),
                                    token.getColumn(),
                                    "-"
                            )
                    );

                    pushScope(token.getLexeme(), ScopeKind.FUNCTION);

                    int closeIdx = findClosingParen(tokens, i + 1);

                    if (closeIdx != -1) {

                        symbols.addAll(
                                extractParameters(
                                        tokens,
                                        i + 2,
                                        closeIdx - 1,
                                        currentScope()
                                )
                        );

                        i = closeIdx + 1;
                        continue;
                    }
                }
            }

            i++;
        }

        scopeStack.clear();

        return deduplicate(symbols);
    }

    // ─────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────

    private static boolean isDataType(TokenType type) {

        return type == TokenType.INT
                || type == TokenType.FLOAT
                || type == TokenType.DOUBLE
                || type == TokenType.CHAR
                || type == TokenType.BOOL
                || type == TokenType.STRING;
    }

    private static Type mapType(Token token) {

        return switch (token.getType()) {

            case INT -> Type.INT;

            case FLOAT -> Type.FLOAT;

            case DOUBLE -> Type.DOUBLE;

            case CHAR -> Type.CHAR;

            case BOOL -> Type.BOOL;

            case VOID -> Type.VOID;

            case STRING -> Type.STRING;

            default -> Type.NULL;
        };
    }

    private static String extractAssignedValue(
            List<Token> tokens,
            int startIndex
    ) {

        if (startIndex >= tokens.size()) {
            return "-";
        }

        if (tokens.get(startIndex).getType() != TokenType.ASSIGN) {
            return "-";
        }

        StringBuilder value = new StringBuilder();

        for (int i = startIndex + 1; i < tokens.size(); i++) {

            TokenType tt = tokens.get(i).getType();

            if (tt == TokenType.SEMICOLON
                    || tt == TokenType.COMMA
                    || tt == TokenType.LEFT_BRACE
                    || tt == TokenType.RIGHT_BRACE
                    || tt == TokenType.IF
                    || tt == TokenType.WHILE
                    || tt == TokenType.FOR
                    || tt == TokenType.DO
                    || tt == TokenType.EOF) {

                break;
            }

            value.append(tokens.get(i).getLexeme()).append(" ");
        }

        String result = value.toString().trim();

        return result.isEmpty()
                ? "-"
                : result;
    }

    private static int findClosingParen(
            List<Token> tokens,
            int openParenIndex
    ) {

        int depth = 0;

        for (int i = openParenIndex; i < tokens.size(); i++) {

            if (tokens.get(i).getType() == TokenType.LEFT_PAREN) {
                depth++;
            }

            if (tokens.get(i).getType() == TokenType.RIGHT_PAREN) {

                depth--;

                if (depth == 0) {
                    return i;
                }
            }
        }

        return -1;
    }

    private static List<SymbolInfo> extractParameters(
            List<Token> tokens,
            int start,
            int end,
            String functionScope
    ) {

        List<SymbolInfo> params = new ArrayList<>();

        int i = start;

        while (i <= end && i < tokens.size()) {

            TokenType tt = tokens.get(i).getType();

            // saltar modifiers
            if (tt == TokenType.CONST
                    || tt == TokenType.PUBLIC
                    || tt == TokenType.PRIVATE) {

                i++;
                continue;
            }

            boolean isType =
                    isDataType(tt)
                            || tt == TokenType.VOID;

            if (isType
                    && i + 1 <= end
                    && tokens.get(i + 1).getType() == TokenType.IDENTIFIER) {

                Token typeToken = tokens.get(i);
                Token idToken = tokens.get(i + 1);

                String value = "-";

                if (i + 2 <= end
                        && tokens.get(i + 2).getType() == TokenType.ASSIGN) {

                    StringBuilder pv = new StringBuilder();

                    int j = i + 3;

                    while (j <= end
                            && tokens.get(j).getType() != TokenType.COMMA) {

                        pv.append(tokens.get(j).getLexeme()).append(" ");

                        j++;
                    }

                    value = pv.toString().trim().isEmpty()
                            ? "-"
                            : pv.toString().trim();
                }

                params.add(
                        new SymbolInfo(
                                idToken.getLexeme(),
                                "parámetro",
                                mapType(typeToken),
                                functionScope,
                                idToken.getLine(),
                                idToken.getColumn(),
                                value
                        )
                );
            }

            i++;
        }

        return params;
    }

    private static List<SymbolInfo> deduplicate(List<SymbolInfo> symbols) {

        List<SymbolInfo> unique = new ArrayList<>();

        for (SymbolInfo s : symbols) {

            boolean exists = false;

            for (SymbolInfo u : unique) {

                if (u.getName().equals(s.getName())
                        && u.getCategory().equals(s.getCategory())
                        && u.getScope().equals(s.getScope())
                        && u.getLine() == s.getLine()
                        && u.getColumn() == s.getColumn()) {

                    exists = true;
                    break;
                }
            }

            if (!exists) {
                unique.add(s);
            }
        }

        return unique;
    }
}