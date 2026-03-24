package org.example.lexer;

public class LexicalError {
    private final String lexeme;
    private final String description;
    private final int line;
    private final int column;

    public LexicalError(String lexeme, String description, int line, int column) {
        this.lexeme = lexeme;
        this.description = description;
        this.line = line;
        this.column = column;
    }

    public String getLexeme() {
        return lexeme;
    }

    public String getDescription() {
        return description;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return "<" +
                "'" + lexeme + '\'' +
                ", description='" + description + '\'' +
                "," + line +
                "," + column +
                ">\n";
    }
}
