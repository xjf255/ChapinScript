package org.example.semantic;

import org.example.lexer.Token;
import org.example.lexer.TokenType;

import java.util.*;

/**
 * Analizador semántico sobre la lista de tokens de ChapinScript.
 *
 * Validaciones implementadas:
 *  1. Variable / constante declarada más de una vez en el mismo scope.
 *  2. Variable usada (en expresión) sin haber sido declarada.
 *  3. Función declarada más de una vez.
 *  4. Llamada a función con número incorrecto de argumentos.
 *  5. Función con tipo de retorno distinto de void declarada sin
 *     ningún RETURN dentro de su cuerpo.
 *  6. Asignación de literal de tipo incompatible a variable ya declarada
 *     (entero ← string, string ← número, bool ← número, etc.).
 */
public class SemanticAnalyzer {

    // ── Entrada / salida ──────────────────────────────────────────────────
    private List<Token>         tokens;
    private List<SemanticError> errors;

    // ── Tabla de símbolos interna ─────────────────────────────────────────
    /**
     * Cada entry guarda el tipo del símbolo y su categoría
     * ("variable", "constante", "función", "parámetro").
     */
    private static class SymbolEntry {
        final String   type;       // "entero", "cadena", etc.
        final String   category;   // "variable" | "constante" | "función" | "parámetro"
        final int      paramCount; // solo para funciones; -1 si no aplica
        final Token    token;      // token de declaración (para mensajes de error)

        SymbolEntry(String type, String category, int paramCount, Token token) {
            this.type       = type;
            this.category   = category;
            this.paramCount = paramCount;
            this.token      = token;
        }
    }

    /**
     * Pila de tablas de símbolos — una por scope.
     * El tope (índice 0) es el scope actual.
     */
    private final Deque<Map<String, SymbolEntry>> scopeStack = new ArrayDeque<>();

    // ════════════════════════════════════════════════════════════════════
    //  ENTRADA PÚBLICA
    // ════════════════════════════════════════════════════════════════════

    public List<SemanticError> analyze(List<Token> tokenList) {
        this.tokens = tokenList;
        this.errors = new ArrayList<>();
        scopeStack.clear();

        pushScope(); // scope global

        int i = 0;
        while (i < tokens.size()) {
            Token t = tokens.get(i);
            if (t.getType() == TokenType.EOF) break;

            // ── Apertura de bloque ───────────────────────────────────────
            if (t.getType() == TokenType.LEFT_BRACE) {
                pushScope();
                i++;
                continue;
            }

            // ── Cierre de bloque ─────────────────────────────────────────
            if (t.getType() == TokenType.RIGHT_BRACE) {
                popScope();
                i++;
                continue;
            }

            // ── Clase — saltar nombre, el cuerpo lo maneja el { ──────────
            if (t.getType() == TokenType.CLASS) {
                i++;
                continue;
            }

            // ── Modificadores de acceso ───────────────────────────────────
            if (t.getType() == TokenType.PUBLIC ||
                t.getType() == TokenType.PRIVATE) {
                i++;
                continue;
            }

            // ── CONST type IDENTIFIER [= expr] ; ─────────────────────────
            if (t.getType() == TokenType.CONST) {
                int ni = i + 1;
                if (ni + 1 < tokens.size() &&
                    isDataType(tokens.get(ni).getType()) &&
                    tokens.get(ni + 1).getType() == TokenType.IDENTIFIER) {

                    Token typeToken = tokens.get(ni);
                    Token idToken   = tokens.get(ni + 1);
                    String type     = mapType(typeToken);

                    checkDuplicateInCurrentScope(idToken, "constante");
                    declareInCurrentScope(idToken.getLexeme(),
                        new SymbolEntry(type, "constante", -1, idToken));

                    // Validar valor asignado
                    if (ni + 2 < tokens.size() &&
                        tokens.get(ni + 2).getType() == TokenType.ASSIGN) {
                        checkAssignmentType(type, tokens, ni + 3, idToken);
                    }
                    i = ni + 2;
                } else {
                    i++;
                }
                continue;
            }

            // ── type IDENTIFIER ... (variable / función) ─────────────────
            if (isDataType(t.getType()) || t.getType() == TokenType.VOID) {
                if (i + 1 < tokens.size() &&
                    tokens.get(i + 1).getType() == TokenType.IDENTIFIER) {

                    Token typeToken = t;
                    Token idToken   = tokens.get(i + 1);
                    String type     = mapType(typeToken);

                    // Función:  type IDENTIFIER (
                    if (i + 2 < tokens.size() &&
                        tokens.get(i + 2).getType() == TokenType.LEFT_PAREN) {

                        int closeIdx  = findClosingParen(tokens, i + 2);
                        int paramCount = closeIdx == -1 ? 0
                            : countParams(tokens, i + 3, closeIdx - 1);

                        checkDuplicateFunctionInGlobal(idToken);
                        declareInGlobalScope(idToken.getLexeme(),
                            new SymbolEntry(type, "función", paramCount, idToken));

                        // Registrar parámetros en un nuevo scope (la función)
                        pushScope();
                        if (closeIdx != -1) {
                            declareParams(tokens, i + 3, closeIdx - 1);
                        }

                        // Validar que tenga return si no es void
                        if (!type.equals("vacio")) {
                            int braceOpen = closeIdx == -1 ? i + 3 : closeIdx + 1;
                            checkFunctionHasReturn(idToken, tokens, braceOpen);
                        }

                        i = closeIdx == -1 ? i + 3 : closeIdx + 1;
                        continue;
                    }

                    // Variable simple:  type IDENTIFIER [= expr] ;
                    checkDuplicateInCurrentScope(idToken, "variable");
                    declareInCurrentScope(idToken.getLexeme(),
                        new SymbolEntry(type, "variable", -1, idToken));

                    // Validar valor asignado
                    if (i + 2 < tokens.size() &&
                        tokens.get(i + 2).getType() == TokenType.ASSIGN) {
                        checkAssignmentType(type, tokens, i + 3, idToken);
                    }

                    i += 2;
                    continue;
                }
                i++;
                continue;
            }

            // ── Uso de IDENTIFIER (asignación o llamada) ─────────────────
            if (t.getType() == TokenType.IDENTIFIER) {

                // Llamada a función:  IDENTIFIER (
                if (i + 1 < tokens.size() &&
                    tokens.get(i + 1).getType() == TokenType.LEFT_PAREN) {

                    checkFunctionCall(t, tokens, i + 1);
                    int closeIdx = findClosingParen(tokens, i + 1);
                    i = closeIdx == -1 ? i + 2 : closeIdx + 1;
                    continue;
                }

                // Uso como variable (lado derecho de asignación, condición, etc.)
                // Solo validamos si NO es la parte izquierda de una declaración
                // (eso ya se manejó arriba).
                checkVariableDeclared(t);
                i++;
                continue;
            }

            i++;
        }

        scopeStack.clear();
        return errors;
    }

    // ════════════════════════════════════════════════════════════════════
    //  VALIDACIONES
    // ════════════════════════════════════════════════════════════════════

    /** 1. Duplicado en el scope actual */
    private void checkDuplicateInCurrentScope(Token idToken, String category) {
        Map<String, SymbolEntry> current = scopeStack.peek();
        if (current != null && current.containsKey(idToken.getLexeme())) {
            SymbolEntry existing = current.get(idToken.getLexeme());
            errors.add(new SemanticError(
                idToken.getLexeme(),
                "'" + idToken.getLexeme() + "' ya fue declarado como " +
                existing.category + " en este ámbito (línea " +
                existing.token.getLine() + ")",
                idToken.getLine(),
                idToken.getColumn()
            ));
        }
    }

    /** 1b. Duplicado de función en scope global */
    private void checkDuplicateFunctionInGlobal(Token idToken) {
        Map<String, SymbolEntry> global = getGlobalScope();
        if (global.containsKey(idToken.getLexeme())) {
            SymbolEntry existing = global.get(idToken.getLexeme());
            if (existing.category.equals("función")) {
                errors.add(new SemanticError(
                    idToken.getLexeme(),
                    "La función '" + idToken.getLexeme() +
                    "' ya fue declarada (línea " + existing.token.getLine() + ")",
                    idToken.getLine(),
                    idToken.getColumn()
                ));
            }
        }
    }

    /** 2. Variable usada sin declarar */
    private void checkVariableDeclared(Token idToken) {
        if (!existsInAnyScope(idToken.getLexeme())) {
            errors.add(new SemanticError(
                idToken.getLexeme(),
                "Variable '" + idToken.getLexeme() + "' usada sin ser declarada",
                idToken.getLine(),
                idToken.getColumn()
            ));
        }
    }

    /** 4. Llamada a función con número incorrecto de argumentos */
    private void checkFunctionCall(Token idToken, List<Token> tokens, int openParenIdx) {
        SymbolEntry entry = findInAnyScope(idToken.getLexeme());
        if (entry == null) {
            // Función no declarada
            errors.add(new SemanticError(
                idToken.getLexeme(),
                "Función '" + idToken.getLexeme() + "' llamada sin haber sido declarada",
                idToken.getLine(),
                idToken.getColumn()
            ));
            return;
        }
        if (!entry.category.equals("función")) return; // es variable, no función

        int closeIdx = findClosingParen(tokens, openParenIdx);
        if (closeIdx == -1) return;

        int argCount = countArgs(tokens, openParenIdx + 1, closeIdx - 1);

        // Permitir funciones con parámetros opcionales (entry.paramCount es mínimo)
        if (entry.paramCount >= 0 && argCount != entry.paramCount) {
            errors.add(new SemanticError(
                idToken.getLexeme(),
                "La función '" + idToken.getLexeme() + "' espera " +
                entry.paramCount + " argumento(s) pero recibió " + argCount,
                idToken.getLine(),
                idToken.getColumn()
            ));
        }
    }

    /** 5. Función no-void sin RETURN en su cuerpo */
    private void checkFunctionHasReturn(Token funcToken,
                                        List<Token> tokens, int braceOpenIdx) {
        // Buscar el { que abre el cuerpo
        int start = braceOpenIdx;
        while (start < tokens.size() &&
               tokens.get(start).getType() != TokenType.LEFT_BRACE) {
            start++;
        }
        if (start >= tokens.size()) return;

        // Recorrer el cuerpo hasta el } que lo cierra
        int depth = 0;
        boolean hasReturn = false;
        for (int i = start; i < tokens.size(); i++) {
            TokenType tt = tokens.get(i).getType();
            if (tt == TokenType.LEFT_BRACE)  depth++;
            if (tt == TokenType.RIGHT_BRACE) { depth--; if (depth == 0) break; }
            if (tt == TokenType.RETURN)      hasReturn = true;
        }

        if (!hasReturn) {
            errors.add(new SemanticError(
                funcToken.getLexeme(),
                "La función '" + funcToken.getLexeme() +
                "' no tiene sentencia 'vonos' (return)",
                funcToken.getLine(),
                funcToken.getColumn()
            ));
        }
    }

    /** 6. Tipo incompatible en asignación */
    private void checkAssignmentType(String declaredType,
                                     List<Token> tokens,
                                     int valueStartIdx,
                                     Token idToken) {
        if (valueStartIdx >= tokens.size()) return;

        Token valueToken = tokens.get(valueStartIdx);
        String assignedType = inferLiteralType(valueToken);

        if (assignedType == null) return; // no es literal, no podemos inferir

        if (!typesCompatible(declaredType, assignedType)) {
            errors.add(new SemanticError(
                idToken.getLexeme(),
                "Tipo incompatible: '" + idToken.getLexeme() +
                "' es de tipo " + declaredType +
                " pero se le asigna un valor de tipo " + assignedType,
                valueToken.getLine(),
                valueToken.getColumn()
            ));
        }
    }

    // ════════════════════════════════════════════════════════════════════
    //  GESTIÓN DE SCOPES
    // ════════════════════════════════════════════════════════════════════

    private void pushScope() {
        scopeStack.push(new LinkedHashMap<>());
    }

    private void popScope() {
        if (!scopeStack.isEmpty()) scopeStack.pop();
    }

    private void declareInCurrentScope(String name, SymbolEntry entry) {
        if (!scopeStack.isEmpty()) scopeStack.peek().put(name, entry);
    }

    private void declareInGlobalScope(String name, SymbolEntry entry) {
        getGlobalScope().put(name, entry);
    }

    private Map<String, SymbolEntry> getGlobalScope() {
        Map<String, SymbolEntry> global = null;
        for (Map<String, SymbolEntry> scope : scopeStack) global = scope;
        return global != null ? global : scopeStack.peek();
    }

    private boolean existsInAnyScope(String name) {
        return findInAnyScope(name) != null;
    }

    private SymbolEntry findInAnyScope(String name) {
        for (Map<String, SymbolEntry> scope : scopeStack) {
            if (scope.containsKey(name)) return scope.get(name);
        }
        return null;
    }

    private void declareParams(List<Token> tokens, int start, int end) {
        int i = start;
        while (i <= end && i < tokens.size()) {
            if ((isDataType(tokens.get(i).getType()) ||
                 tokens.get(i).getType() == TokenType.VOID) &&
                i + 1 <= end &&
                tokens.get(i + 1).getType() == TokenType.IDENTIFIER) {

                Token typeToken = tokens.get(i);
                Token idToken   = tokens.get(i + 1);
                declareInCurrentScope(idToken.getLexeme(),
                    new SymbolEntry(mapType(typeToken), "parámetro", -1, idToken));
            }
            i++;
        }
    }

    // ════════════════════════════════════════════════════════════════════
    //  UTILIDADES
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

    /** Infiere el tipo de un token literal. Devuelve null si no es literal. */
    private static String inferLiteralType(Token token) {
        return switch (token.getType()) {
            case INTEGER_LITERAL -> "entero";
            case DECIMAL_LITERAL -> "decimal";
            case STRING_LITERAL  -> "cadena";
            case CHAR_LITERAL    -> "letra";
            case TRUE, FALSE     -> "casaca";
            case NULL            -> "nulo";
            default              -> null;
        };
    }

    /**
     * Compatibilidad de tipos (permisiva con numéricos).
     * entero ↔ decimal ↔ doble son compatibles entre sí.
     */
    private static boolean typesCompatible(String declared, String assigned) {
        if (declared.equals(assigned)) return true;
        Set<String> numerics = Set.of("entero", "decimal", "doble");
        if (numerics.contains(declared) && numerics.contains(assigned)) return true;
        // nulo es compatible con cualquier tipo referencia (cadena, clases)
        if (assigned.equals("nulo")) return true;
        return false;
    }

    private static int findClosingParen(List<Token> tokens, int openParenIdx) {
        int depth = 0;
        for (int i = openParenIdx; i < tokens.size(); i++) {
            if (tokens.get(i).getType() == TokenType.LEFT_PAREN)  depth++;
            if (tokens.get(i).getType() == TokenType.RIGHT_PAREN) {
                depth--;
                if (depth == 0) return i;
            }
        }
        return -1;
    }

    /** Cuenta parámetros formales entre [start, end] — pares type IDENTIFIER. */
    private static int countParams(List<Token> tokens, int start, int end) {
        if (start > end) return 0;
        // Contar las COMAS de nivel 0 + 1 si hay al menos un tipo
        int count = 0;
        boolean hasContent = false;
        int depth = 0;
        for (int i = start; i <= end && i < tokens.size(); i++) {
            TokenType tt = tokens.get(i).getType();
            if (tt == TokenType.LEFT_PAREN)  depth++;
            if (tt == TokenType.RIGHT_PAREN) depth--;
            if (depth == 0) {
                if (isDataType(tt) || tt == TokenType.VOID) hasContent = true;
                if (tt == TokenType.COMMA && hasContent) count++;
            }
        }
        return hasContent ? count + 1 : 0;
    }

    /** Cuenta argumentos en una llamada entre [start, end]. */
    private static int countArgs(List<Token> tokens, int start, int end) {
        if (start > end) return 0;
        int count = 0;
        boolean hasContent = false;
        int depth = 0;
        for (int i = start; i <= end && i < tokens.size(); i++) {
            TokenType tt = tokens.get(i).getType();
            if (tt == TokenType.LEFT_PAREN)  depth++;
            if (tt == TokenType.RIGHT_PAREN) depth--;
            if (depth == 0) {
                if (tt != TokenType.COMMA) hasContent = true;
                if (tt == TokenType.COMMA && hasContent) { count++; hasContent = false; }
            }
        }
        return hasContent ? count + 1 : count;
    }
}
