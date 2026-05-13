package org.example.semantic;

public class SymbolInfo {

    private final String name;
    private final String category;
    private final Type dataType;
    private final String scope;
    private final int line;
    private final int column;
    private final String value;

    public SymbolInfo(
            String name,
            String category,
            Type dataType,
            String scope,
            int line,
            int column,
            String value
    ) {
        this.name = name;
        this.category = category;
        this.dataType = dataType;
        this.scope = scope;
        this.line = line;
        this.column = column;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public Type getDataType() {
        return dataType;
    }

    public String getScope() {
        return scope;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public String getValue() {
        return value;
    }
}