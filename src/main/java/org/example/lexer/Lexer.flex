package org.example.lexer;

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

    private Token token(TokenType type, String value) {
        return new Token(value, type, yyline + 1, yycolumn + 1);
    }

    private void lexicalError(String message) {
        errors.add(new LexicalError(
            yytext(),
            message,
            yyline + 1,
            yycolumn + 1
        ));
    }

    private int errorLine;
    private int errorColumn;
    private StringBuilder stringBuffer = new StringBuilder();
%}

LETTER         = [a-zA-Z_]
DIGIT          = [0-9]
ACCENTED_CHAR  = [áéíóúÁÉÍÓÚàèìòùÀÈÌÒÙâêîôûÂÊÎÔÛäëïöüÄËÏÖÜñÑ]
IDENTIFIER     = {LETTER}({LETTER}|{DIGIT})*
ID_CHAR_VALID   = [a-zA-Z0-9_]
ID_CHAR_INVALID = [áéíóúÁÉÍÓÚàèìòùÀÈÌÒÙâêîôûÂÊÎÔÛäëïöüÄËÏÖÜñÑ]
ID_CHAR_ANY     = {ID_CHAR_VALID}|{ID_CHAR_INVALID}
INTEGER        = {DIGIT}+
DECIMAL        = {DIGIT}+"."{DIGIT}+
WHITESPACE     = [ \t\r\n]+
COMMENT_LINE   = "//"[^\n]*
%state BLOCK_COMMENT
%state STRING_DQ
%state STRING_SQ
%state IDENT_STATE

%%

<YYINITIAL> {

    {WHITESPACE}   { /* ignorar */ }
    {COMMENT_LINE} { /* ignorar */ }

    "/*" {
             errorLine   = yyline + 1;
             errorColumn = yycolumn + 1;
             yybegin(BLOCK_COMMENT);
         }

    "chotear"    { return token(TokenType.PRINT); }
    "simon"      { return token(TokenType.IF); }
    "chapus"     { return token(TokenType.ELSE); }
    "chiripa"    { return token(TokenType.SWITCH); }
    "wasa"       { return token(TokenType.CASE); }
    "porsiacaso" { return token(TokenType.DEFAULT); }
    "vuelta"     { return token(TokenType.FOR); }
    "seguile"    { return token(TokenType.WHILE); }
    "dale"       { return token(TokenType.DO); }
    "cuaje"      { return token(TokenType.BREAK); }
    "chanin"     { return token(TokenType.CONTINUE); }
    "vonos"      { return token(TokenType.RETURN); }
    "cabal"      { return token(TokenType.INT); }
    "pisto"      { return token(TokenType.FLOAT); }
    "pistazo"    { return token(TokenType.DOUBLE); }
    "cachito"    { return token(TokenType.CHAR); }
    "casaca"     { return token(TokenType.BOOL); }
    "nimais"     { return token(TokenType.VOID); }
    "chisme"     { return token(TokenType.STRING); }
    "fijo"       { return token(TokenType.CONST); }
    "banda"      { return token(TokenType.CLASS); }
    "vos"        { return token(TokenType.THIS); }
    "calale"     { return token(TokenType.TRY); }
    "atrapalo"   { return token(TokenType.CATCH); }
    "morongazo"  { return token(TokenType.THROW); }
    "barrio"     { return token(TokenType.PUBLIC); }
    "caquero"    { return token(TokenType.PRIVATE); }
    "deplano"    { return token(TokenType.TRUE); }
    "nel"        { return token(TokenType.FALSE); }
    "inutil"     { return token(TokenType.NULL); }
    "estrenar"   { return token(TokenType.NEW); }

    "GEMELOS"      { return token(TokenType.EQUALS); }
    "CRUZ_DAR"     { return token(TokenType.PLUS_ASSIGN); }
    "RAYA_DAR"     { return token(TokenType.MINUS_ASSIGN); }
    "ESTRELLA_DAR" { return token(TokenType.MULTIPLY_ASSIGN); }
    "RAMPA_DAR"    { return token(TokenType.DIVIDE_ASSIGN); }
    "SOBRA_DAR"    { return token(TokenType.MOD_ASSIGN); }
    "DIVORCIO"     { return token(TokenType.NOT_EQUALS); }
    "BASE"         { return token(TokenType.LESS_EQUAL); }
    "TECHO"        { return token(TokenType.GREATER_EQUAL); }
    "CADENA"       { return token(TokenType.AND); }
    "VALLAS"       { return token(TokenType.OR); }
    "CRUZ_CRUZ"    { return token(TokenType.INCREMENT); }
    "RAYA_RAYA"    { return token(TokenType.DECREMENT); }
    "DAR"      { return token(TokenType.ASSIGN); }
    "PICO"     { return token(TokenType.LESS_THAN); }
    "BOCA"     { return token(TokenType.GREATER_THAN); }
    "CRUZ"     { return token(TokenType.PLUS); }
    "RAYA"     { return token(TokenType.MINUS); }
    "ESTRELLA" { return token(TokenType.MULTIPLY); }
    "RAMPA"    { return token(TokenType.DIVIDE); }
    "SOBRA"    { return token(TokenType.MOD); }
    "GRITO"    { return token(TokenType.NOT); }
    "FRENO"    { return token(TokenType.SEMICOLON); }
    "SEMILLA"  { return token(TokenType.COMMA); }
    "ATOMO"    { return token(TokenType.DOT); }
    "DUDA"     { return token(TokenType.QUESTION); }
    "OJOS"     { return token(TokenType.COLON); }
    "ABRAZO"   { return token(TokenType.LEFT_PAREN); }
    "RESPALDO" { return token(TokenType.RIGHT_PAREN); }
    "ALMA"     { return token(TokenType.LEFT_BRACE); }
    "CUERPO"   { return token(TokenType.RIGHT_BRACE); }
    "CAJON"    { return token(TokenType.LEFT_BRACKET); }
    "TAPA"     { return token(TokenType.RIGHT_BRACKET); }
    {DECIMAL} { return token(TokenType.DECIMAL_LITERAL); }
    {INTEGER} { return token(TokenType.INTEGER_LITERAL); }

    \" {
           stringBuffer.setLength(0);
           errorLine   = yyline + 1;
           errorColumn = yycolumn + 1;
           yybegin(STRING_DQ);
       }
    \' {
           stringBuffer.setLength(0);
           errorLine   = yyline + 1;
           errorColumn = yycolumn + 1;
           yybegin(STRING_SQ);
       }

    {ID_CHAR_ANY} {
        stringBuffer.setLength(0);
        stringBuffer.append(yytext());
        errorLine   = yyline + 1;
        errorColumn = yycolumn + 1;
        yybegin(IDENT_STATE);
    }

    [^ \t\r\n] {
        lexicalError("Símbolo no reconocido: '" + yytext() + "'");
    }

    <<EOF>> { return token(TokenType.EOF); }
}

<IDENT_STATE> {

    {ID_CHAR_ANY}+ { stringBuffer.append(yytext()); }

    [^] {
            yypushback(1);
            yybegin(YYINITIAL);

            String lexema = stringBuffer.toString();

            // Comprobar si contiene algún carácter acentuado o ñ
            boolean tieneInvalido = false;
            for (int cp, i = 0; i < lexema.length(); i += Character.charCount(cp)) {
                cp = lexema.codePointAt(i);
                // Rango aproximado de letras latinas con diacríticos
                if (cp > 127) { tieneInvalido = true; break; }
            }

            if (tieneInvalido) {
                errors.add(new LexicalError(
                    lexema,
                    "Identificador inválido: '" + lexema +
                    "' — no se permiten tildes ni la letra ñ",
                    errorLine,
                    errorColumn
                ));
                // No retorna token — el parser nunca ve este lexema
            } else {
                // Lexema ASCII puro → token IDENTIFIER
                // (las palabras reservadas ya fueron capturadas antes de
                //  entrar al estado, por lo que aquí solo llegan ids normales)
                return new Token(lexema, TokenType.IDENTIFIER, errorLine, errorColumn);
            }
        }

    <<EOF>> {
                yybegin(YYINITIAL);
                String lexema = stringBuffer.toString();
                boolean tieneInvalido = false;
                for (int cp, i = 0; i < lexema.length(); i += Character.charCount(cp)) {
                    cp = lexema.codePointAt(i);
                    if (cp > 127) { tieneInvalido = true; break; }
                }
                if (tieneInvalido) {
                    errors.add(new LexicalError(
                        lexema,
                        "Identificador inválido: '" + lexema +
                        "' — no se permiten tildes ni la letra ñ",
                        errorLine,
                        errorColumn
                    ));
                } else {
                    return new Token(lexema, TokenType.IDENTIFIER, errorLine, errorColumn);
                }
                return token(TokenType.EOF);
            }
}

<BLOCK_COMMENT> {
    \*\/    { yybegin(YYINITIAL); }

    <<EOF>> {
                errors.add(new LexicalError(
                    "/*",
                    "Comentario de bloque no cerrado",
                    errorLine,
                    errorColumn
                ));
                return token(TokenType.EOF);
            }

    [^]     { // consumir cualquier carácter, incluidas tildes
            }
}

<STRING_DQ> {
    \"       {
                 yybegin(YYINITIAL);
                 return token(TokenType.STRING_LITERAL,
                              "\"" + stringBuffer.toString() + "\"");
             }

    \\\"     { stringBuffer.append("\\\""); }
    \\\\     { stringBuffer.append("\\\\"); }
    \\n      { stringBuffer.append("\\n");  }
    \\t      { stringBuffer.append("\\t");  }
    \\r      { stringBuffer.append("\\r");  }
    \\.      { stringBuffer.append(yytext()); }

    \n       {
                 errors.add(new LexicalError(
                     "\"" + stringBuffer.toString(),
                     "Cadena de texto no cerrada (salto de línea inesperado)",
                     errorLine, errorColumn
                 ));
                 yybegin(YYINITIAL);
             }

    <<EOF>>  {
                 errors.add(new LexicalError(
                     "\"" + stringBuffer.toString(),
                     "Cadena de texto no cerrada (fin de archivo)",
                     errorLine, errorColumn
                 ));
                 return token(TokenType.EOF);
             }

    [^\"\\\n]+ { stringBuffer.append(yytext()); }
}

<STRING_SQ> {
    \'       {
                 yybegin(YYINITIAL);
                 String content = stringBuffer.toString();
                 boolean isEscape = content.length() == 2 && content.charAt(0) == '\\';
                 boolean isSingle = content.codePointCount(0, content.length()) == 1;
                 if (isEscape || isSingle) {
                     return token(TokenType.CHAR_LITERAL, "'" + content + "'");
                 }
                 return token(TokenType.STRING_LITERAL, "\"" + content + "\"");
             }

    \\\'     { stringBuffer.append("\\'"); }
    \\\\     { stringBuffer.append("\\\\"); }
    \\n      { stringBuffer.append("\\n");  }
    \\t      { stringBuffer.append("\\t");  }
    \\r      { stringBuffer.append("\\r");  }
    \\.      { stringBuffer.append(yytext()); }

    \n       {
                 errors.add(new LexicalError(
                     "'" + stringBuffer.toString(),
                     "Cadena de texto no cerrada (salto de línea inesperado)",
                     errorLine, errorColumn
                 ));
                 yybegin(YYINITIAL);
             }

    <<EOF>>  {
                 errors.add(new LexicalError(
                     "'" + stringBuffer.toString(),
                     "Cadena de texto no cerrada (fin de archivo)",
                     errorLine, errorColumn
                 ));
                 return token(TokenType.EOF);
             }

    // Tildes y cualquier Unicode válidos dentro de strings/chars
    [^\'\\\n]+ { stringBuffer.append(yytext()); }
}