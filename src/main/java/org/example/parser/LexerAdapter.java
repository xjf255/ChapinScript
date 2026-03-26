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
            return new Symbol(sym.EOF);
        }

        return switch (token.getType()) {
            // Palabras reservadas
            case PRINT    -> new Symbol(sym.PRINT, token.getLine(), token.getColumn());
            case IF       -> new Symbol(sym.IF, token.getLine(), token.getColumn());
            case ELSE     -> new Symbol(sym.ELSE, token.getLine(), token.getColumn());
            case SWITCH   -> new Symbol(sym.SWITCH, token.getLine(), token.getColumn());
            case CASE     -> new Symbol(sym.CASE, token.getLine(), token.getColumn());
            case DEFAULT  -> new Symbol(sym.DEFAULT, token.getLine(), token.getColumn());
            case FOR      -> new Symbol(sym.FOR, token.getLine(), token.getColumn());
            case WHILE    -> new Symbol(sym.WHILE, token.getLine(), token.getColumn());
            case DO       -> new Symbol(sym.DO, token.getLine(), token.getColumn());
            case BREAK    -> new Symbol(sym.BREAK, token.getLine(), token.getColumn());
            case CONTINUE -> new Symbol(sym.CONTINUE, token.getLine(), token.getColumn());
            case RETURN   -> new Symbol(sym.RETURN, token.getLine(), token.getColumn());
            case INT      -> new Symbol(sym.INT, token.getLine(), token.getColumn());
            case FLOAT    -> new Symbol(sym.FLOAT, token.getLine(), token.getColumn());
            case DOUBLE   -> new Symbol(sym.DOUBLE, token.getLine(), token.getColumn());
            case CHAR     -> new Symbol(sym.CHAR, token.getLine(), token.getColumn());
            case BOOL     -> new Symbol(sym.BOOL, token.getLine(), token.getColumn());
            case VOID     -> new Symbol(sym.VOID, token.getLine(), token.getColumn());
            case STRING   -> new Symbol(sym.STRING, token.getLine(), token.getColumn());
            case CONST    -> new Symbol(sym.CONST, token.getLine(), token.getColumn());
            case CLASS    -> new Symbol(sym.CLASS, token.getLine(), token.getColumn());
            case THIS     -> new Symbol(sym.THIS, token.getLine(), token.getColumn());
            case TRY      -> new Symbol(sym.TRY, token.getLine(), token.getColumn());
            case CATCH    -> new Symbol(sym.CATCH, token.getLine(), token.getColumn());
            case THROW    -> new Symbol(sym.THROW, token.getLine(), token.getColumn());
            case PUBLIC   -> new Symbol(sym.PUBLIC, token.getLine(), token.getColumn());
            case PRIVATE  -> new Symbol(sym.PRIVATE, token.getLine(), token.getColumn());
            case TRUE     -> new Symbol(sym.TRUE, token.getLine(), token.getColumn());
            case FALSE    -> new Symbol(sym.FALSE, token.getLine(), token.getColumn());
            // Literales con valor
            case IDENTIFIER      -> new Symbol(sym.IDENTIFIER, token.getLine(), token.getColumn()  ,   token.getLexeme());
            case STRING_LITERAL  -> new Symbol(sym.STRING_LITERAL, token.getLine(), token.getColumn(),  token.getLexeme());
            case INTEGER_LITERAL -> new Symbol(sym.INTEGER_LITERAL, token.getLine(), token.getColumn(), Integer.parseInt(token.getLexeme()));
            case DECIMAL_LITERAL -> new Symbol(sym.DECIMAL_LITERAL, token.getLine(), token.getColumn(), Double.parseDouble(token.getLexeme()));
            // Operadores
            case EQUALS        -> new Symbol(sym.EQUALS, token.getLine(), token.getColumn());
            case NOT_EQUALS    -> new Symbol(sym.NOT_EQUALS, token.getLine(), token.getColumn());
            case LESS_EQUAL    -> new Symbol(sym.LESS_EQUAL, token.getLine(), token.getColumn());
            case GREATER_EQUAL -> new Symbol(sym.GREATER_EQUAL, token.getLine(), token.getColumn());
            case AND           -> new Symbol(sym.AND, token.getLine(), token.getColumn());
            case OR            -> new Symbol(sym.OR, token.getLine(), token.getColumn());
            case ASSIGN        -> new Symbol(sym.ASSIGN, token.getLine(), token.getColumn());
            case LESS_THAN     -> new Symbol(sym.LESS_THAN, token.getLine(), token.getColumn());
            case GREATER_THAN  -> new Symbol(sym.GREATER_THAN, token.getLine(), token.getColumn());
            case PLUS          -> new Symbol(sym.PLUS, token.getLine(), token.getColumn());
            case MINUS         -> new Symbol(sym.MINUS, token.getLine(), token.getColumn());
            case MULTIPLY      -> new Symbol(sym.MULTIPLY, token.getLine(), token.getColumn());
            case DIVIDE        -> new Symbol(sym.DIVIDE, token.getLine(), token.getColumn());
            case NOT           -> new Symbol(sym.NOT, token.getLine(), token.getColumn());
            // Delimitadores
            case SEMICOLON     -> new Symbol(sym.SEMICOLON, token.getLine(), token.getColumn());
            case COMMA         -> new Symbol(sym.COMMA, token.getLine(), token.getColumn());
            case DOT           -> new Symbol(sym.DOT, token.getLine(), token.getColumn());
            case QUESTION      -> new Symbol(sym.QUESTION, token.getLine(), token.getColumn());
            case COLON         -> new Symbol(sym.COLON, token.getLine(), token.getColumn());
            case LEFT_PAREN    -> new Symbol(sym.LEFT_PAREN, token.getLine(), token.getColumn());
            case RIGHT_PAREN   -> new Symbol(sym.RIGHT_PAREN, token.getLine(), token.getColumn());
            case LEFT_BRACE    -> new Symbol(sym.LEFT_BRACE, token.getLine(), token.getColumn());
            case RIGHT_BRACE   -> new Symbol(sym.RIGHT_BRACE, token.getLine(), token.getColumn());
            case LEFT_BRACKET  -> new Symbol(sym.LEFT_BRACKET, token.getLine(), token.getColumn());
            case RIGHT_BRACKET -> new Symbol(sym.RIGHT_BRACKET, token.getLine(), token.getColumn());
            // Errores léxicos — el parser no debería verlos
            case ERROR -> next_token(); // saltar y pedir el siguiente
            default    -> new Symbol(sym.error, token.getLine(), token.getColumn());
        };
    }
}