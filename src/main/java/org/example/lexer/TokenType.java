package org.example.lexer;

public enum TokenType {
    PRINT,
    IF,
    ELSE,
    SWITCH,
    CASE,
    DEFAULT,
    FOR,
    WHILE,
    DO,
    BREAK,
    CONTINUE,
    RETURN,
    INT,
    FLOAT,
    DOUBLE,
    CHAR,
    BOOL,
    VOID,
    STRING,
    CONST,
    CLASS,
    THIS,
    TRY,
    CATCH,
    THROW,
    PUBLIC,
    PRIVATE,
    TRUE,
    FALSE,
    NULL,
    NEW,
    /* Literales */
    IDENTIFIER,
    INTEGER_LITERAL,
    DECIMAL_LITERAL,
    STRING_LITERAL,
    CHAR_LITERAL,

    /* Operadores */
    EQUALS,             // GEMELOS
    NOT_EQUALS,         // DIVORCIO
    LESS_EQUAL,         // BASE
    GREATER_EQUAL,      // TECHO
    LESS_THAN,          // PICO
    GREATER_THAN,       // BOCA
    AND,                // CADENA
    OR,                 // VALLAS
    NOT,                // GRITO

    /* Operadores de asignación */
    ASSIGN,             // DAR
    PLUS_ASSIGN,        // CRUZ_DAR
    MINUS_ASSIGN,       // RAYA_DAR
    MULTIPLY_ASSIGN,    // ESTRELLA_DAR
    DIVIDE_ASSIGN,      // RAMPA_DAR
    MOD_ASSIGN,         // SOBRA_DAR

    /* Operadores aritméticos */
    PLUS,               // CRUZ
    MINUS,              // RAYA
    UMINUS,
    MULTIPLY,           // ESTRELLA
    DIVIDE,             // RAMPA
    MOD,                // SOBRA
    INCREMENT,          // CRUZ_CRUZ
    DECREMENT,          // RAYA_RAYA

    /* Delimitadores */
    SEMICOLON,          // FRENO
    COMMA,              // SEMILLA
    DOT,                // ATOMO
    QUESTION,           // DUDA
    COLON,              // OJOS
    LEFT_PAREN,         // ABRAZO
    RIGHT_PAREN,        // RESPALDO
    LEFT_BRACE,         // ALMA
    RIGHT_BRACE,        // CUERPO
    LEFT_BRACKET,       // CAJON
    RIGHT_BRACKET,      // TAPA

    /* Especiales */
    EOF,
    ERROR
}