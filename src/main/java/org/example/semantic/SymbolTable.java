package org.example.semantic;

import java.util.ArrayDeque;
import java.util.Deque;

public class SymbolTable {
    private final Deque<Scope> scopes = new ArrayDeque<>();

    public void enterScope(String name) {
        Scope parent = scopes.peek();
        scopes.push(new Scope(name, parent));
    }

    public void exitScope() {
        if (!scopes.isEmpty()) {
            scopes.pop();
        }
    }

    public Scope getCurrentScope() {
        return scopes.peek();
    }

    public boolean define(SymbolInfo symbol) {
        if (scopes.isEmpty()) {
            throw new IllegalStateException("No hay scope activo");
        }
        return scopes.peek().define(symbol);
    }

    public SymbolInfo resolve(String name) {
        if (scopes.isEmpty()) {
            return null;
        }
        return scopes.peek().resolve(name);
    }

    public SymbolInfo resolveInCurrentScope(String name) {
        if (scopes.isEmpty()) {
            return null;
        }
        return scopes.peek().resolveInCurrentScope(name);
    }
}