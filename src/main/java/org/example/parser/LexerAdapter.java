package org.example.parser;

import java_cup.runtime.Scanner;
import java_cup.runtime.Symbol;
import org.example.lexer.Lexer;
import org.example.lexer.Token;
import org.example.lexer.TokenType;

public class LexerAdapter implements Scanner {

    private final Lexer lexer;

    public LexerAdapter(Lexer lexer) {
        this.lexer = lexer;
    }

    @Override
    public Symbol next_token() throws Exception {
        Token token = lexer.yylex();

        if (token == null || token.getType() == TokenType.EOF) {
            return new Symbol(sym.EOF,
                    token != null ? token.getLine() : -1,
                    token != null ? token.getColumn() : -1,
                    null);
        }

        return switch (token.getType()) {
            // Palabras reservadas
            case PRINT    -> new Symbol(sym.PRINT, token.getLine(), token.getColumn(),token.getLexeme());
            case IF       -> new Symbol(sym.IF, token.getLine(), token.getColumn(),token.getLexeme());
            case ELSE     -> new Symbol(sym.ELSE, token.getLine(), token.getColumn(), token.getLexeme());
            case SWITCH   -> new Symbol(sym.SWITCH, token.getLine(), token.getColumn(), token.getLexeme());
            case CASE     -> new Symbol(sym.CASE, token.getLine(), token.getColumn(),token.getLexeme());
            case DEFAULT  -> new Symbol(sym.DEFAULT, token.getLine(), token.getColumn(),token.getLexeme());
            case FOR      -> new Symbol(sym.FOR, token.getLine(), token.getColumn(),token.getLexeme());
            case WHILE    -> new Symbol(sym.WHILE, token.getLine(), token.getColumn(),token.getLexeme());
            case DO       -> new Symbol(sym.DO, token.getLine(), token.getColumn(),token.getLexeme());
            case BREAK    -> new Symbol(sym.BREAK, token.getLine(), token.getColumn(),token.getLexeme());
            case CONTINUE -> new Symbol(sym.CONTINUE, token.getLine(), token.getColumn(),token.getLexeme());
            case RETURN   -> new Symbol(sym.RETURN, token.getLine(), token.getColumn(),token.getLexeme());
            case INT      -> new Symbol(sym.INT, token.getLine(), token.getColumn(),token.getLexeme());
            case FLOAT    -> new Symbol(sym.FLOAT, token.getLine(), token.getColumn(),token.getLexeme());
            case DOUBLE   -> new Symbol(sym.DOUBLE, token.getLine(), token.getColumn(),token.getLexeme());
            case CHAR     -> new Symbol(sym.CHAR, token.getLine(), token.getColumn(),token.getLexeme());
            case BOOL     -> new Symbol(sym.BOOL, token.getLine(), token.getColumn(),token.getLexeme());
            case VOID     -> new Symbol(sym.VOID, token.getLine(), token.getColumn(),token.getLexeme());
            case STRING   -> new Symbol(sym.STRING, token.getLine(), token.getColumn(),token.getLexeme());
            case NEW      -> new Symbol(sym.NEW, token.getLine(), token.getColumn(),token.getLexeme());
            case UMINUS   -> new Symbol(sym.UMINUS, token.getLine(), token.getColumn(),token.getLexeme());
            case NULL     -> new Symbol(sym.NULL, token.getLine(), token.getColumn(),token.getLexeme());
            case CONST    -> new Symbol(sym.CONST, token.getLine(), token.getColumn(),token.getLexeme());
            case CLASS    -> new Symbol(sym.CLASS, token.getLine(), token.getColumn(),token.getLexeme());
            case THIS     -> new Symbol(sym.THIS, token.getLine(), token.getColumn(),token.getLexeme());
            case TRY      -> new Symbol(sym.TRY, token.getLine(), token.getColumn(),token.getLexeme());
            case CATCH    -> new Symbol(sym.CATCH, token.getLine(), token.getColumn(),token.getLexeme());
            case THROW    -> new Symbol(sym.THROW, token.getLine(), token.getColumn(),token.getLexeme());
            case PUBLIC   -> new Symbol(sym.PUBLIC, token.getLine(), token.getColumn(),token.getLexeme());
            case PRIVATE  -> new Symbol(sym.PRIVATE, token.getLine(), token.getColumn(),token.getLexeme());
            case TRUE     -> new Symbol(sym.TRUE, token.getLine(), token.getColumn(),token.getLexeme());
            case FALSE    -> new Symbol(sym.FALSE, token.getLine(), token.getColumn(), token.getLexeme());
            // Literales con valor
            case IDENTIFIER      -> new Symbol(sym.IDENTIFIER, token.getLine(), token.getColumn()  ,   token.getLexeme());
            case STRING_LITERAL  -> new Symbol(sym.STRING_LITERAL, token.getLine(), token.getColumn(),  token.getLexeme());
            case INTEGER_LITERAL -> new Symbol(sym.INTEGER_LITERAL, token.getLine(), token.getColumn(), Integer.parseInt(token.getLexeme()));
            case DECIMAL_LITERAL -> new Symbol(sym.DECIMAL_LITERAL, token.getLine(), token.getColumn(), Double.parseDouble(token.getLexeme()));
            // Operadores
            case EQUALS        -> new Symbol(sym.EQUALS, token.getLine(), token.getColumn(),token.getLexeme());
            case NOT_EQUALS    -> new Symbol(sym.NOT_EQUALS, token.getLine(), token.getColumn(),token.getLexeme());
            case LESS_EQUAL    -> new Symbol(sym.LESS_EQUAL, token.getLine(), token.getColumn(),token.getLexeme());
            case GREATER_EQUAL -> new Symbol(sym.GREATER_EQUAL, token.getLine(), token.getColumn(),token.getLexeme());
            case AND           -> new Symbol(sym.AND, token.getLine(), token.getColumn(),token.getLexeme());
            case OR            -> new Symbol(sym.OR, token.getLine(), token.getColumn(),token.getLexeme());
            case ASSIGN        -> new Symbol(sym.ASSIGN, token.getLine(), token.getColumn(),token.getLexeme());
            case LESS_THAN     -> new Symbol(sym.LESS_THAN, token.getLine(), token.getColumn(),token.getLexeme());
            case GREATER_THAN  -> new Symbol(sym.GREATER_THAN, token.getLine(), token.getColumn(),token.getLexeme());
            case PLUS          -> new Symbol(sym.PLUS, token.getLine(), token.getColumn(),token.getLexeme());
            case MINUS         -> new Symbol(sym.MINUS, token.getLine(), token.getColumn(),token.getLexeme());
            case MULTIPLY      -> new Symbol(sym.MULTIPLY, token.getLine(), token.getColumn(),token.getLexeme());
            case DIVIDE        -> new Symbol(sym.DIVIDE, token.getLine(), token.getColumn(),token.getLexeme());
            case NOT           -> new Symbol(sym.NOT, token.getLine(), token.getColumn(),token.getLexeme());
            // Delimitadores
            case SEMICOLON     -> new Symbol(sym.SEMICOLON, token.getLine(), token.getColumn(),token.getLexeme());
            case COMMA         -> new Symbol(sym.COMMA, token.getLine(), token.getColumn(),token.getLexeme());
            case DOT           -> new Symbol(sym.DOT, token.getLine(), token.getColumn(),token.getLexeme());
            case QUESTION      -> new Symbol(sym.QUESTION, token.getLine(), token.getColumn(),token.getLexeme());
            case COLON         -> new Symbol(sym.COLON, token.getLine(), token.getColumn(),token.getLexeme());
            case LEFT_PAREN    -> new Symbol(sym.LEFT_PAREN, token.getLine(), token.getColumn(),token.getLexeme());
            case RIGHT_PAREN   -> new Symbol(sym.RIGHT_PAREN, token.getLine(), token.getColumn(),token.getLexeme());
            case LEFT_BRACE    -> new Symbol(sym.LEFT_BRACE, token.getLine(), token.getColumn(),token.getLexeme());
            case RIGHT_BRACE   -> new Symbol(sym.RIGHT_BRACE, token.getLine(), token.getColumn(),token.getLexeme());
            case LEFT_BRACKET  -> new Symbol(sym.LEFT_BRACKET, token.getLine(), token.getColumn(),token.getLexeme());
            case RIGHT_BRACKET -> new Symbol(sym.RIGHT_BRACKET, token.getLine(), token.getColumn(),token.getLexeme());
            // Errores léxicos — el parser no debería verlos
            case ERROR -> next_token(); // saltar y pedir el siguiente
            default    -> new Symbol(sym.error, token.getLine(), token.getColumn(),token.getLexeme());
        };
    }
}