package org.example.reports;

import org.example.lexer.Token;
import org.example.lexer.TokenType;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Genera la tabla de símbolos a partir de la lista de tokens.
 *
 * Correcciones sobre la versión original:
 *  1. Scope stack (pila) — reemplaza el contador de llaves plano.
 *     Permite rastrear correctamente: global → clase → función → bloque.
 *  2. Detección de clases (CLASS IDENTIFIER { ... }).
 *  3. Bloques de control (if/while/for/do/try) no crean un nuevo scope
 *     nombrado; las variables dentro quedan con el scope de la función
 *     que los contiene, no como "global".
 *  4. El recorrido es defensivo — comprueba límites en cada paso para
 *     que tokens de código inválido no contaminen la tabla.
 */
public class SymbolTableGenerator {

    // ── Tipos de scope ────────────────────────────────────────────────────
    private enum ScopeKind { GLOBAL, CLASS, FUNCTION, BLOCK }

    private static class ScopeFrame {
        final String    name;
        final ScopeKind kind;
        ScopeFrame(String name, ScopeKind kind) {
            this.name = name;
            this.kind = kind;
        }
    }

    // ── Pila de scopes (instancia, para poder llamar generate() varias veces) ──
    private final Deque<ScopeFrame> scopeStack = new ArrayDeque<>();

    private void pushScope(String name, ScopeKind kind) {
        scopeStack.push(new ScopeFrame(name, kind));
    }

    private void popScope() {
        if (!scopeStack.isEmpty()) scopeStack.pop();
    }

    /**
     * Nombre del scope actual concatenando la pila de afuera hacia adentro.
     * Ejemplos: "global", "Vehiculo", "Vehiculo::arrancar"
     */
    private String currentScope() {
        if (scopeStack.isEmpty()) return "global";
        List<String> parts = new ArrayList<>();
        for (ScopeFrame f : scopeStack) parts.add(0, f.name);
        return String.join("::", parts);
    }

    // ════════════════════════════════════════════════════════════════════
    //  ENTRADA PÚBLICA
    // ════════════════════════════════════════════════════════════════════

    public List<SymbolInfo> generate(List<Token> tokens) {
        scopeStack.clear();
        pushScope("global", ScopeKind.GLOBAL);

        List<SymbolInfo> symbols = new ArrayList<>();
        int i = 0;

        while (i < tokens.size()) {
            Token t = tokens.get(i);

            // ── EOF ──────────────────────────────────────────────────────
            if (t.getType() == TokenType.EOF) break;

            // ── Apertura de bloque { ─────────────────────────────────────
            if (t.getType() == TokenType.LEFT_BRACE) {
                ScopeFrame top = scopeStack.peek();
                // Si el tope ya es CLASS o FUNCTION, este { es el que
                // abre ese scope — no empujar otro frame.
                if (top != null &&
                        (top.kind == ScopeKind.CLASS || top.kind == ScopeKind.FUNCTION)) {
                    i++;
                    continue;
                }
                // Bloque anónimo: if, while, for, try, etc.
                // Hereda el nombre del scope padre para que las variables
                // queden con el nombre de la función contenedora.
                pushScope(currentScope(), ScopeKind.BLOCK);
                i++;
                continue;
            }

            // ── Cierre de bloque } ───────────────────────────────────────
            if (t.getType() == TokenType.RIGHT_BRACE) {
                popScope();
                i++;
                continue;
            }

            // ── Clase:  CLASS IDENTIFIER ─────────────────────────────────
            if (t.getType() == TokenType.CLASS) {
                if (i + 1 < tokens.size() &&
                        tokens.get(i + 1).getType() == TokenType.IDENTIFIER) {

                    Token idToken = tokens.get(i + 1);
                    symbols.add(new SymbolInfo(
                            idToken.getLexeme(), "clase", "-",
                            currentScope(),
                            idToken.getLine(), idToken.getColumn(), "-"
                    ));
                    pushScope(idToken.getLexeme(), ScopeKind.CLASS);
                    i += 2;
                } else {
                    i++;
                }
                continue;
            }

            // ── Modificadores de acceso (barrio / caquero) ───────────────
            // Solo los saltamos; el tipo viene después.
            if (t.getType() == TokenType.PUBLIC ||
                    t.getType() == TokenType.PRIVATE) {
                i++;
                continue;
            }

            // ── CONST type IDENTIFIER ────────────────────────────────────
            if (t.getType() == TokenType.CONST) {
                int ni = i + 1;
                if (ni + 1 < tokens.size() &&
                        isDataType(tokens.get(ni).getType()) &&
                        tokens.get(ni + 1).getType() == TokenType.IDENTIFIER) {

                    Token typeToken = tokens.get(ni);
                    Token idToken   = tokens.get(ni + 1);
                    String value    = extractAssignedValue(tokens, ni + 2);

                    symbols.add(new SymbolInfo(
                            idToken.getLexeme(), "constante", mapType(typeToken),
                            currentScope(),
                            idToken.getLine(), idToken.getColumn(), value
                    ));
                    i = ni + 2;
                } else {
                    i++;
                }
                continue;
            }

            // ── type IDENTIFIER ...  (variable / función / arreglo) ──────
            if (isDataType(t.getType()) || t.getType() == TokenType.VOID) {
                if (i + 1 < tokens.size() &&
                        tokens.get(i + 1).getType() == TokenType.IDENTIFIER) {

                    Token typeToken = t;
                    Token idToken   = tokens.get(i + 1);

                    // Función / método:  type IDENTIFIER (
                    if (i + 2 < tokens.size() &&
                            tokens.get(i + 2).getType() == TokenType.LEFT_PAREN) {

                        symbols.add(new SymbolInfo(
                                idToken.getLexeme(), "función", mapType(typeToken),
                                currentScope(),
                                idToken.getLine(), idToken.getColumn(), "-"
                        ));
                        pushScope(idToken.getLexeme(), ScopeKind.FUNCTION);

                        int closeIdx = findClosingParen(tokens, i + 2);
                        if (closeIdx != -1) {
                            symbols.addAll(
                                    extractParameters(tokens, i + 3, closeIdx - 1, currentScope())
                            );
                            i = closeIdx + 1;
                        } else {
                            i += 3;
                        }
                        continue;
                    }

                    // Arreglo:  type IDENTIFIER [
                    if (i + 2 < tokens.size() &&
                            tokens.get(i + 2).getType() == TokenType.LEFT_BRACKET) {

                        String value = extractAssignedValue(tokens, i + 2);
                        symbols.add(new SymbolInfo(
                                idToken.getLexeme(), "arreglo", mapType(typeToken),
                                currentScope(),
                                idToken.getLine(), idToken.getColumn(), value
                        ));
                        i += 3;
                        continue;
                    }

                    // Variable simple:  type IDENTIFIER [= expr] ;
                    String value = extractAssignedValue(tokens, i + 2);
                    symbols.add(new SymbolInfo(
                            idToken.getLexeme(), "variable", mapType(typeToken),
                            currentScope(),
                            idToken.getLine(), idToken.getColumn(), value
                    ));
                    i += 2;
                    continue;
                }
                i++;
                continue;
            }

            // ── Constructor dentro de clase:  IDENTIFIER ( ───────────────
            // Solo se activa cuando el scope actual es una clase.
            if (t.getType() == TokenType.IDENTIFIER) {
                ScopeFrame top = scopeStack.peek();
                if (top != null && top.kind == ScopeKind.CLASS &&
                        i + 1 < tokens.size() &&
                        tokens.get(i + 1).getType() == TokenType.LEFT_PAREN) {

                    symbols.add(new SymbolInfo(
                            t.getLexeme(), "constructor", top.name,
                            currentScope(),
                            t.getLine(), t.getColumn(), "-"
                    ));
                    pushScope(t.getLexeme(), ScopeKind.FUNCTION);

                    int closeIdx = findClosingParen(tokens, i + 1);
                    if (closeIdx != -1) {
                        symbols.addAll(
                                extractParameters(tokens, i + 2, closeIdx - 1, currentScope())
                        );
                        i = closeIdx + 1;
                    } else {
                        i += 2;
                    }
                    continue;
                }
            }

            i++;
        }

        scopeStack.clear();
        return deduplicate(symbols);
    }

    // ════════════════════════════════════════════════════════════════════
    //  UTILIDADES PRIVADAS
    // ════════════════════════════════════════════════════════════════════

    private static boolean isDataType(TokenType type) {
        return type == TokenType.INT
                || type == TokenType.FLOAT
                || type == TokenType.DOUBLE
                || type == TokenType.CHAR
                || type == TokenType.BOOL
                || type == TokenType.STRING;
    }

    private static String mapType(Token token) {
        return switch (token.getType()) {
            case INT    -> "entero";
            case FLOAT  -> "decimal";
            case DOUBLE -> "doble";
            case CHAR   -> "letra";
            case BOOL   -> "casaca";
            case VOID   -> "vacio";
            case STRING -> "cadena";
            default     -> token.getLexeme();
        };
    }

    /**
     * Lee el valor después de DAR (=), hasta el primer FRENO (;),
     * ALMA ({), CUERPO (}), SEMILLA (,) o palabra de control.
     */
    private static String extractAssignedValue(List<Token> tokens, int startIndex) {
        if (startIndex >= tokens.size()) return "-";
        if (tokens.get(startIndex).getType() != TokenType.ASSIGN) return "-";

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
                    || tt == TokenType.EOF) break;
            value.append(tokens.get(i).getLexeme()).append(" ");
        }

        String result = value.toString().trim();
        return result.isEmpty() ? "-" : result;
    }

    /** Índice del ) que cierra el ( en openParenIndex, o -1 si no se encuentra. */
    private static int findClosingParen(List<Token> tokens, int openParenIndex) {
        int depth = 0;
        for (int i = openParenIndex; i < tokens.size(); i++) {
            if (tokens.get(i).getType() == TokenType.LEFT_PAREN)  depth++;
            if (tokens.get(i).getType() == TokenType.RIGHT_PAREN) {
                depth--;
                if (depth == 0) return i;
            }
        }
        return -1;
    }

    /**
     * Extrae parámetros formales entre los índices [start, end] (inclusivos).
     * Soporta:  type id  y  type id = valor_default
     */
    private static List<SymbolInfo> extractParameters(
            List<Token> tokens, int start, int end, String functionScope) {

        List<SymbolInfo> params = new ArrayList<>();
        int i = start;

        while (i <= end && i < tokens.size()) {
            TokenType tt = tokens.get(i).getType();

            // Saltar CONST y modificadores dentro de lista de params
            if (tt == TokenType.CONST ||
                    tt == TokenType.PUBLIC ||
                    tt == TokenType.PRIVATE) {
                i++;
                continue;
            }

            boolean isType = isDataType(tt) || tt == TokenType.VOID;
            if (isType &&
                    i + 1 <= end &&
                    tokens.get(i + 1).getType() == TokenType.IDENTIFIER) {

                Token typeToken = tokens.get(i);
                Token idToken   = tokens.get(i + 1);
                String value    = "-";

                if (i + 2 <= end &&
                        tokens.get(i + 2).getType() == TokenType.ASSIGN) {

                    StringBuilder pv = new StringBuilder();
                    int j = i + 3;
                    while (j <= end &&
                            tokens.get(j).getType() != TokenType.COMMA) {
                        pv.append(tokens.get(j).getLexeme()).append(" ");
                        j++;
                    }
                    value = pv.toString().trim().isEmpty() ? "-" : pv.toString().trim();
                }

                params.add(new SymbolInfo(
                        idToken.getLexeme(), "parámetro", mapType(typeToken),
                        functionScope,
                        idToken.getLine(), idToken.getColumn(), value
                ));
            }
            i++;
        }

        return params;
    }

    /** Elimina duplicados exactos (mismo nombre + categoría + scope + línea + columna). */
    private static List<SymbolInfo> deduplicate(List<SymbolInfo> symbols) {
        List<SymbolInfo> unique = new ArrayList<>();
        for (SymbolInfo s : symbols) {
            boolean exists = false;
            for (SymbolInfo u : unique) {
                if (u.getName().equals(s.getName())
                        && u.getCategory().equals(s.getCategory())
                        && u.getScope().equals(s.getScope())
                        && u.getLine()   == s.getLine()
                        && u.getColumn() == s.getColumn()) {
                    exists = true;
                    break;
                }
            }
            if (!exists) unique.add(s);
        }
        return unique;
    }
}