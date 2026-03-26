package org.example.parser;

import java.util.ArrayList;
import java.util.List;

%%

%public
%class Lexer
%unicode
%line
%column
%type Token

%{
    private final List<LexicalError> errors = new ArrayList<>();

    public List<LexicalError> getErrors() {
        return errors;
    }

    private Token token(TokenType type) {
        return new Token(yytext(), type, yyline + 1, yycolumn + 1);
    }

    private void lexicalError(String message) {
        errors.add(new LexicalError(
            yytext(),
            message,
            yyline + 1,
            yycolumn + 1
        ));
    }
%}

LETTER                  = [a-zA-Z_]
DIGIT                   = [0-9]
ACCENTED_CHAR           = [áéíóúÁÉÍÓÚñÑ]
IDENTIFIER              = {LETTER}({LETTER}|{DIGIT})*
INVALID_IDENTIFIER      = ({LETTER}|{ACCENTED_CHAR})({LETTER}|{DIGIT}|{ACCENTED_CHAR})*{ACCENTED_CHAR}({LETTER}|{DIGIT}|{ACCENTED_CHAR})*
INTEGER                 = {DIGIT}+
DECIMAL                 = {DIGIT}+"."{DIGIT}+
WHITESPACE              = [ \t\r\n]+
STRING                  = \"([^\"\\\n]|\\.)*\" | \'([^\'\\\n]|\\.)*\'
STRING_UNCLOSED         = \"([^\"\\\n]|\\.)*\n | \'([^\'\\\n]|\\.)*\n
COMMENT_LINE            = "//"[^\n]*
COMMENT_BLOCK           = "/*"[^*]*("*"+[^*/][^*]*)*"*"+"/"
COMMENT_BLOCK_UNCLOSED  = "/*"[^*]*("*"+[^*/][^*]*)*"*"*

%%

{WHITESPACE}               { /* ignorar */ }

/* Comentarios */
{COMMENT_LINE}             { /* ignorar */ }
{COMMENT_BLOCK}            { /* ignorar */ }
{COMMENT_BLOCK_UNCLOSED}   { lexicalError("Comentario de bloque no cerrado"); return token(TokenType.ERROR); }

/* Palabras reservadas */
"chotear"                  { return token(TokenType.PRINT); }
"simon"                    { return token(TokenType.IF); }
"chapus"                   { return token(TokenType.ELSE); }
"chiripa"                  { return token(TokenType.SWITCH); }
"wasa"                     { return token(TokenType.CASE); }
"porsiacaso"               { return token(TokenType.DEFAULT); }
"vuelta"                   { return token(TokenType.FOR); }
"seguile"                  { return token(TokenType.WHILE); }
"dale"                     { return token(TokenType.DO); }
"cuaje"                    { return token(TokenType.BREAK); }
"chanin"                   { return token(TokenType.CONTINUE); }
"vonos"                    { return token(TokenType.RETURN); }
"cabal"                    { return token(TokenType.INT); }
"pisto"                    { return token(TokenType.FLOAT); }
"pistazo"                  { return token(TokenType.DOUBLE); }
"cachito"                  { return token(TokenType.CHAR); }
"casaca"                   { return token(TokenType.BOOL); }
"nimais"                   { return token(TokenType.VOID); }
"chisme"                   { return token(TokenType.STRING); }
"fijo"                     { return token(TokenType.CONST); }
"banda"                    { return token(TokenType.CLASS); }
"vos"                      { return token(TokenType.THIS); }
"calale"                   { return token(TokenType.TRY); }
"atrapalo"                 { return token(TokenType.CATCH); }
"morongazo"                { return token(TokenType.THROW); }
"barrio"                   { return token(TokenType.PUBLIC); }
"caquero"                  { return token(TokenType.PRIVATE); }
"deplano"                  { return token(TokenType.TRUE); }
"nel"                      { return token(TokenType.FALSE); }

/* Literales */
{DECIMAL}                  { return token(TokenType.DECIMAL_LITERAL); }
{INTEGER}                  { return token(TokenType.INTEGER_LITERAL); }
{STRING}                   { return token(TokenType.STRING_LITERAL); }
{STRING_UNCLOSED}          { lexicalError("Cadena de texto no cerrada"); return token(TokenType.ERROR); }

/* Identificadores */
{INVALID_IDENTIFIER}       { lexicalError("Identificador inválido: no se permiten tildes ni la letra ñ"); return token(TokenType.ERROR); }
{IDENTIFIER}               { return token(TokenType.IDENTIFIER); }

/* Operadores */
"=="                       { return token(TokenType.EQUALS); }
"!="                       { return token(TokenType.NOT_EQUALS); }
"<="                       { return token(TokenType.LESS_EQUAL); }
">="                       { return token(TokenType.GREATER_EQUAL); }
"&&"                       { return token(TokenType.AND); }
"||"                       { return token(TokenType.OR); }
"="                        { return token(TokenType.ASSIGN); }
"<"                        { return token(TokenType.LESS_THAN); }
">"                        { return token(TokenType.GREATER_THAN); }
"+"                        { return token(TokenType.PLUS); }
"-"                        { return token(TokenType.MINUS); }
"*"                        { return token(TokenType.MULTIPLY); }
"/"                        { return token(TokenType.DIVIDE); }
"!"                        { return token(TokenType.NOT); }

/* Delimitadores */
";"                        { return token(TokenType.SEMICOLON); }
","                        { return token(TokenType.COMMA); }
"."                        { return token(TokenType.DOT); }
"?"                        { return token(TokenType.QUESTION); }
":"                        { return token(TokenType.COLON); }
"("                        { return token(TokenType.LEFT_PAREN); }
")"                        { return token(TokenType.RIGHT_PAREN); }
"{"                        { return token(TokenType.LEFT_BRACE); }
"}"                        { return token(TokenType.RIGHT_BRACE); }
"["                        { return token(TokenType.LEFT_BRACKET); }
"]"                        { return token(TokenType.RIGHT_BRACKET); }

<<EOF>>                    { return token(TokenType.EOF); }

/* Error léxico */
.                          { lexicalError("Símbolo no reconocido"); return token(TokenType.ERROR); }