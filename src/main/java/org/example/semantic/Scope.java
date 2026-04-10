package org.example.semantic;

import java.util.HashMap;
import java.util.Map;

public class Scope {
    private final String name;
    private final Scope parent;
    private final Map<String, SymbolInfo> symbols;

    public Scope(String name, Scope parent) {
        this.name = name;
        this.parent = parent;
        this.symbols = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public Scope getParent() {
        return parent;
    }

    public boolean define(SymbolInfo symbol) {
        if (symbols.containsKey(symbol.getName())) {
            return false;
        }
        symbols.put(symbol.getName(), symbol);
        return true;
    }

    public SymbolInfo resolve(String name) {
        if (symbols.containsKey(name)) {
            return symbols.get(name);
        }
        if (parent != null) {
            return parent.resolve(name);
        }
        return null;
    }

    public SymbolInfo resolveInCurrentScope(String name) {
        return symbols.get(name);
    }

    public Map<String, SymbolInfo> getSymbols() {
        return symbols;
    }
}