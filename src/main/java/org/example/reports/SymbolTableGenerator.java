package org.example.reports;

import org.example.lexer.Token;
import org.example.lexer.TokenType;

import java.util.ArrayList;
import java.util.List;

public class SymbolTableGenerator {

    public static List<SymbolInfo> generate(List<Token> tokens) {
        List<SymbolInfo> symbols = new ArrayList<>();

        String currentScope = "global";
        int braceDepth = 0;

        for (int i = 0; i < tokens.size(); i++) {
            Token current = tokens.get(i);

            // Manejo básico de scopes por llaves
            if (current.getType() == TokenType.LEFT_BRACE) {
                braceDepth++;
            } else if (current.getType() == TokenType.RIGHT_BRACE) {
                braceDepth--;
                if (braceDepth <= 0) {
                    currentScope = "global";
                    braceDepth = Math.max(braceDepth, 0);
                }
            }

            // Caso 1: const type IDENTIFIER ...
            if (current.getType() == TokenType.CONST) {
                if (i + 2 < tokens.size()
                        && isDataType(tokens.get(i + 1).getType())
                        && tokens.get(i + 2).getType() == TokenType.IDENTIFIER) {

                    Token typeToken = tokens.get(i + 1);
                    Token idToken = tokens.get(i + 2);

                    String value = extractAssignedValue(tokens, i + 3);

                    symbols.add(new SymbolInfo(
                            idToken.getLexeme(),
                            "constante",
                            mapType(typeToken),
                            currentScope,
                            idToken.getLine(),
                            idToken.getColumn(),
                            value
                    ));
                }
                continue;
            }

            // Caso 2: type IDENTIFIER ...
            if (isDataType(current.getType())) {
                if (i + 1 < tokens.size() && tokens.get(i + 1).getType() == TokenType.IDENTIFIER) {
                    Token typeToken = current;
                    Token idToken = tokens.get(i + 1);

                    // ¿Es función o variable?
                    if (i + 2 < tokens.size() && tokens.get(i + 2).getType() == TokenType.LEFT_PAREN) {
                        symbols.add(new SymbolInfo(
                                idToken.getLexeme(),
                                "función",
                                mapType(typeToken),
                                "global",
                                idToken.getLine(),
                                idToken.getColumn(),
                                "-"
                        ));

                        currentScope = idToken.getLexeme();

                        // Extraer parámetros
                        int closeParenIndex = findClosingParen(tokens, i + 2);
                        if (closeParenIndex != -1) {
                            symbols.addAll(extractParameters(tokens, i + 3, closeParenIndex - 1, currentScope));
                        }
                    } else {
                        String value = extractAssignedValue(tokens, i + 2);

                        symbols.add(new SymbolInfo(
                                idToken.getLexeme(),
                                "variable",
                                mapType(typeToken),
                                currentScope,
                                idToken.getLine(),
                                idToken.getColumn(),
                                value
                        ));
                    }
                }
            }
        }

        return deduplicate(symbols);
    }

    private static boolean isDataType(TokenType type) {
        return type == TokenType.INT
                || type == TokenType.FLOAT
                || type == TokenType.DOUBLE
                || type == TokenType.CHAR
                || type == TokenType.BOOL
                || type == TokenType.STRING
                || type == TokenType.VOID;
    }

    private static String mapType(Token token) {
        return switch (token.getType()) {
            case INT -> "entero";
            case FLOAT -> "decimal";
            case DOUBLE -> "doble";
            case CHAR -> "letra";
            case BOOL -> "casaca";
            case VOID -> "vacio";
            case STRING -> "cadena";
            default -> token.getLexeme();
        };
    }

    private static String extractAssignedValue(List<Token> tokens, int startIndex) {
        if (startIndex < tokens.size() && tokens.get(startIndex).getType() == TokenType.ASSIGN) {
            StringBuilder value = new StringBuilder();
            for (int i = startIndex + 1; i < tokens.size(); i++) {
                Token t = tokens.get(i);
                if (t.getType() == TokenType.SEMICOLON
                        || t.getType() == TokenType.COMMA
                        || t.getType() == TokenType.LEFT_BRACE
                        || t.getType() == TokenType.RIGHT_BRACE
                        || t.getType() == TokenType.IF
                        || t.getType() == TokenType.WHILE
                        || t.getType() == TokenType.FOR) {
                    break;
                }
                value.append(t.getLexeme()).append(" ");
            }
            return value.toString().trim().isEmpty() ? "-" : value.toString().trim();
        }
        return "-";
    }

    private static int findClosingParen(List<Token> tokens, int openParenIndex) {
        int depth = 0;
        for (int i = openParenIndex; i < tokens.size(); i++) {
            if (tokens.get(i).getType() == TokenType.LEFT_PAREN) {
                depth++;
            } else if (tokens.get(i).getType() == TokenType.RIGHT_PAREN) {
                depth--;
                if (depth == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    private static List<SymbolInfo> extractParameters(List<Token> tokens, int start, int end, String functionScope) {
        List<SymbolInfo> params = new ArrayList<>();

        int i = start;
        while (i <= end && i < tokens.size()) {
            if (isDataType(tokens.get(i).getType())) {
                if (i + 1 <= end && tokens.get(i + 1).getType() == TokenType.IDENTIFIER) {
                    Token typeToken = tokens.get(i);
                    Token idToken = tokens.get(i + 1);

                    String value = "-";

                    if (i + 2 <= end && tokens.get(i + 2).getType() == TokenType.ASSIGN) {
                        StringBuilder paramValue = new StringBuilder();
                        int j = i + 3;
                        while (j <= end && tokens.get(j).getType() != TokenType.COMMA) {
                            paramValue.append(tokens.get(j).getLexeme()).append(" ");
                            j++;
                        }
                        value = paramValue.toString().trim().isEmpty() ? "-" : paramValue.toString().trim();
                    }

                    params.add(new SymbolInfo(
                            idToken.getLexeme(),
                            "parámetro",
                            mapType(typeToken),
                            functionScope,
                            idToken.getLine(),
                            idToken.getColumn(),
                            value
                    ));
                }
            }
            i++;
        }

        return params;
    }

    private static List<SymbolInfo> deduplicate(List<SymbolInfo> symbols) {
        List<SymbolInfo> unique = new ArrayList<>();

        for (SymbolInfo symbol : symbols) {
            boolean exists = false;

            for (SymbolInfo saved : unique) {
                if (saved.getName().equals(symbol.getName())
                        && saved.getCategory().equals(symbol.getCategory())
                        && saved.getScope().equals(symbol.getScope())
                        && saved.getLine() == symbol.getLine()
                        && saved.getColumn() == symbol.getColumn()) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                unique.add(symbol);
            }
        }

        return unique;
    }
}