package org.example.lexer;

public class Token {
    private final String lexeme;
    private final TokenType type;
    private final int line;
    private final int column;

    public Token(String lexeme, TokenType type, int line, int column) {
        this.lexeme = lexeme;
        this.type = type;
        this.line = line;
        this.column = column;
    }

    public String getLexeme() {
        return lexeme;
    }

    public TokenType getType() {
        return type;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return "<" + lexeme + ", " + type + "> position: [" + line + ":" + column + "]\n";
    }
}
