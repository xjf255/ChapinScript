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
            case PRINT    -> new Symbol(sym.PRINT);
            case IF       -> new Symbol(sym.IF);
            case ELSE     -> new Symbol(sym.ELSE);
            case SWITCH   -> new Symbol(sym.SWITCH);
            case CASE     -> new Symbol(sym.CASE);
            case DEFAULT  -> new Symbol(sym.DEFAULT);
            case FOR      -> new Symbol(sym.FOR);
            case WHILE    -> new Symbol(sym.WHILE);
            case DO       -> new Symbol(sym.DO);
            case BREAK    -> new Symbol(sym.BREAK);
            case CONTINUE -> new Symbol(sym.CONTINUE);
            case RETURN   -> new Symbol(sym.RETURN);
            case INT      -> new Symbol(sym.INT);
            case FLOAT    -> new Symbol(sym.FLOAT);
            case DOUBLE   -> new Symbol(sym.DOUBLE);
            case CHAR     -> new Symbol(sym.CHAR);
            case BOOL     -> new Symbol(sym.BOOL);
            case VOID     -> new Symbol(sym.VOID);
            case STRING   -> new Symbol(sym.STRING);
            case CONST    -> new Symbol(sym.CONST);
            case CLASS    -> new Symbol(sym.CLASS);
            case THIS     -> new Symbol(sym.THIS);
            case TRY      -> new Symbol(sym.TRY);
            case CATCH    -> new Symbol(sym.CATCH);
            case THROW    -> new Symbol(sym.THROW);
            case PUBLIC   -> new Symbol(sym.PUBLIC);
            case PRIVATE  -> new Symbol(sym.PRIVATE);
            case TRUE     -> new Symbol(sym.TRUE);
            case FALSE    -> new Symbol(sym.FALSE);
            // Literales con valor
            case IDENTIFIER      -> new Symbol(sym.IDENTIFIER,     token.getLexeme());
            case STRING_LITERAL  -> new Symbol(sym.STRING_LITERAL,  token.getLexeme());
            case INTEGER_LITERAL -> new Symbol(sym.INTEGER_LITERAL, Integer.parseInt(token.getLexeme()));
            case DECIMAL_LITERAL -> new Symbol(sym.DECIMAL_LITERAL, Double.parseDouble(token.getLexeme()));
            // Operadores
            case EQUALS        -> new Symbol(sym.EQUALS);
            case NOT_EQUALS    -> new Symbol(sym.NOT_EQUALS);
            case LESS_EQUAL    -> new Symbol(sym.LESS_EQUAL);
            case GREATER_EQUAL -> new Symbol(sym.GREATER_EQUAL);
            case AND           -> new Symbol(sym.AND);
            case OR            -> new Symbol(sym.OR);
            case ASSIGN        -> new Symbol(sym.ASSIGN);
            case LESS_THAN     -> new Symbol(sym.LESS_THAN);
            case GREATER_THAN  -> new Symbol(sym.GREATER_THAN);
            case PLUS          -> new Symbol(sym.PLUS);
            case MINUS         -> new Symbol(sym.MINUS);
            case MULTIPLY      -> new Symbol(sym.MULTIPLY);
            case DIVIDE        -> new Symbol(sym.DIVIDE);
            case NOT           -> new Symbol(sym.NOT);
            // Delimitadores
            case SEMICOLON     -> new Symbol(sym.SEMICOLON);
            case COMMA         -> new Symbol(sym.COMMA);
            case DOT           -> new Symbol(sym.DOT);
            case QUESTION      -> new Symbol(sym.QUESTION);
            case COLON         -> new Symbol(sym.COLON);
            case LEFT_PAREN    -> new Symbol(sym.LEFT_PAREN);
            case RIGHT_PAREN   -> new Symbol(sym.RIGHT_PAREN);
            case LEFT_BRACE    -> new Symbol(sym.LEFT_BRACE);
            case RIGHT_BRACE   -> new Symbol(sym.RIGHT_BRACE);
            case LEFT_BRACKET  -> new Symbol(sym.LEFT_BRACKET);
            case RIGHT_BRACKET -> new Symbol(sym.RIGHT_BRACKET);
            // Errores léxicos — el parser no debería verlos
            case ERROR -> next_token(); // saltar y pedir el siguiente
            default    -> new Symbol(sym.error);
        };
    }
}