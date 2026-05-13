/* ============================================================
   errores_sintacticos.chap
   Cada bloque cubre un tipo distinto de error sintáctico.
   Se usan variables y funciones con nombres válidos para que
   el lexer no interfiera — todos los errores son del parser.
   ============================================================ */


// ══════════════════════════════════════════════════════════════
// ERROR 1: Falta FRENO (;) al declarar variable
// Regla: type IDENTIFIER ASSIGN expression error
// ══════════════════════════════════════════════════════════════
cabal a DAR 5
cabal b DAR 10 FRENO


// ══════════════════════════════════════════════════════════════
// ERROR 2: Falta FRENO después de print
// Regla: PRINT LEFT_PAREN expression RIGHT_PAREN error
// ══════════════════════════════════════════════════════════════
nimais errorPrint ABRAZO RESPALDO ALMA
    chotear ABRAZO "sin punto y coma" RESPALDO
    cabal x DAR 1 FRENO
CUERPO


// ══════════════════════════════════════════════════════════════
// ERROR 3: Llave de cierre de bloque faltante
// Regla: block_open statement_list error
// ══════════════════════════════════════════════════════════════
nimais sinCerrar ABRAZO RESPALDO ALMA
    cabal x DAR 1 FRENO
    chotear ABRAZO x RESPALDO FRENO
// falta CUERPO aquí


// ══════════════════════════════════════════════════════════════
// ERROR 4: Condición inválida en IF (expresión vacía)
// Regla: IF LEFT_PAREN error RIGHT_PAREN block
// ══════════════════════════════════════════════════════════════
nimais errorIf ABRAZO RESPALDO ALMA
    simon ABRAZO RESPALDO ALMA
        chotear ABRAZO "nunca" RESPALDO FRENO
    CUERPO
CUERPO


// ══════════════════════════════════════════════════════════════
// ERROR 5: Falta paréntesis de cierre en IF
// Regla: IF LEFT_PAREN expression error block
// ══════════════════════════════════════════════════════════════
nimais errorIfParen ABRAZO RESPALDO ALMA
    cabal n DAR 5 FRENO
    simon ABRAZO n BOCA 0 ALMA
        chotear ABRAZO "positivo" RESPALDO FRENO
    CUERPO
CUERPO


// ══════════════════════════════════════════════════════════════
// ERROR 6: Condición inválida en WHILE
// Regla: WHILE LEFT_PAREN error RIGHT_PAREN block
// ══════════════════════════════════════════════════════════════
nimais errorWhile ABRAZO RESPALDO ALMA
    seguile ABRAZO RESPALDO ALMA
        cuaje FRENO
    CUERPO
CUERPO


// ══════════════════════════════════════════════════════════════
// ERROR 7: Falta FRENO al final del do-while
// Regla: DO block WHILE LEFT_PAREN expression RIGHT_PAREN error
// ══════════════════════════════════════════════════════════════
nimais errorDoWhile ABRAZO RESPALDO ALMA
    cabal i DAR 0 FRENO
    dale ALMA
        i CRUZ_CRUZ FRENO
    CUERPO seguile ABRAZO i PICO 5 RESPALDO
    cabal x DAR 1 FRENO
CUERPO


// ══════════════════════════════════════════════════════════════
// ERROR 8: Estructura inválida en FOR (faltan los ;)
// Regla: FOR LEFT_PAREN error RIGHT_PAREN block
// ══════════════════════════════════════════════════════════════
nimais errorFor ABRAZO RESPALDO ALMA
    vuelta ABRAZO cabal i DAR 0 RESPALDO ALMA
        chotear ABRAZO i RESPALDO FRENO
    CUERPO
CUERPO


// ══════════════════════════════════════════════════════════════
// ERROR 9: Expresión inválida en SWITCH
// Regla: SWITCH LEFT_PAREN error RIGHT_PAREN LEFT_BRACE ...
// ══════════════════════════════════════════════════════════════
nimais errorSwitch ABRAZO RESPALDO ALMA
    chiripa ABRAZO RESPALDO ALMA
        wasa 1 OJOS
            cuaje FRENO
        porsiacaso OJOS
            cuaje FRENO
    CUERPO
CUERPO


// ══════════════════════════════════════════════════════════════
// ERROR 10: CASE con expresión inválida
// Regla: CASE error COLON statement_list
// ══════════════════════════════════════════════════════════════
nimais errorCase ABRAZO cabal op RESPALDO ALMA
    chiripa ABRAZO op RESPALDO ALMA
        wasa OJOS
            chotear ABRAZO "caso malo" RESPALDO FRENO
            cuaje FRENO
        porsiacaso OJOS
            cuaje FRENO
    CUERPO
CUERPO


// ══════════════════════════════════════════════════════════════
// ERROR 11: Falta FRENO después de RETURN
// Regla: RETURN expression error
// ══════════════════════════════════════════════════════════════
cabal errorReturn ABRAZO RESPALDO ALMA
    vonos 42
CUERPO


// ══════════════════════════════════════════════════════════════
// ERROR 12: Falta FRENO después de BREAK
// Regla: BREAK error
// ══════════════════════════════════════════════════════════════
nimais errorBreak ABRAZO RESPALDO ALMA
    vuelta ABRAZO cabal i DAR 0 FRENO i PICO 10 FRENO i CRUZ_CRUZ RESPALDO ALMA
        simon ABRAZO i GEMELOS 5 RESPALDO ALMA
            cuaje
        CUERPO
    CUERPO
CUERPO


// ══════════════════════════════════════════════════════════════
// ERROR 13: Falta FRENO después de CONTINUE
// Regla: CONTINUE error
// ══════════════════════════════════════════════════════════════
nimais errorContinue ABRAZO RESPALDO ALMA
    vuelta ABRAZO cabal i DAR 0 FRENO i PICO 10 FRENO i CRUZ_CRUZ RESPALDO ALMA
        simon ABRAZO i GEMELOS 3 RESPALDO ALMA
            chanin
        CUERPO
    CUERPO
CUERPO


// ══════════════════════════════════════════════════════════════
// ERROR 14: Falta FRENO después de THROW
// Regla: THROW expression error
// ══════════════════════════════════════════════════════════════
nimais errorThrow ABRAZO RESPALDO ALMA
    morongazo "error sin freno"
CUERPO


// ══════════════════════════════════════════════════════════════
// ERROR 15: Parámetro inválido en CATCH
// Regla: CATCH LEFT_PAREN error RIGHT_PAREN block
// ══════════════════════════════════════════════════════════════
nimais errorCatch ABRAZO RESPALDO ALMA
    calale ALMA
        morongazo "algo" FRENO
    CUERPO atrapalo ABRAZO RESPALDO ALMA
        chotear ABRAZO "catch vacio" RESPALDO FRENO
    CUERPO
CUERPO


// ══════════════════════════════════════════════════════════════
// ERROR 16: Argumentos inválidos en llamada a función
// Regla: func_call IDENTIFIER LEFT_PAREN error RIGHT_PAREN
// ══════════════════════════════════════════════════════════════
nimais errorLlamada ABRAZO RESPALDO ALMA
    cabal r DAR sumar ABRAZO SEMILLA RESPALDO FRENO
    chotear ABRAZO r RESPALDO FRENO
CUERPO


// ══════════════════════════════════════════════════════════════
// ERROR 17: Falta paréntesis de cierre en expresión agrupada
// Regla: LEFT_PAREN expression error
// ══════════════════════════════════════════════════════════════
nimais errorGrupo ABRAZO RESPALDO ALMA
    cabal x DAR 5 FRENO
    cabal y DAR ABRAZO x CRUZ 3 FRENO
    chotear ABRAZO y RESPALDO FRENO
CUERPO


// ══════════════════════════════════════════════════════════════
// ERROR 18: Índice inválido en acceso a arreglo
// Regla: array_access IDENTIFIER LEFT_BRACKET error RIGHT_BRACKET
// ══════════════════════════════════════════════════════════════
nimais errorArreglo ABRAZO RESPALDO ALMA
    cabal nums CAJON 5 TAPA FRENO
    cabal val DAR nums CAJON TAPA FRENO
    chotear ABRAZO val RESPALDO FRENO
CUERPO


// ══════════════════════════════════════════════════════════════
// ERROR 19: Constructor sin bloque de cuerpo
// Regla: decl_constructors IDENTIFIER LEFT_PAREN param_list RIGHT_PAREN error
// ══════════════════════════════════════════════════════════════
banda SinCuerpo ALMA
    cabal valor FRENO

    SinCuerpo ABRAZO cabal v RESPALDO
    cabal otroMetodo DAR 0 FRENO

CUERPO


// ══════════════════════════════════════════════════════════════
// ERROR 20: Clase sin llave de cierre
// Regla: CLASS IDENTIFIER LEFT_BRACE class_member_list error
// ══════════════════════════════════════════════════════════════
banda ClaseSinCerrar ALMA
    barrio cabal dato FRENO
    barrio nimais hacer ABRAZO RESPALDO ALMA
        chotear ABRAZO "haciendo" RESPALDO FRENO
    CUERPO
// falta CUERPO de la clase


// ══════════════════════════════════════════════════════════════
// ERROR 21: Parámetros inválidos en constructor
// Regla: decl_constructors IDENTIFIER LEFT_PAREN error RIGHT_PAREN block
// ══════════════════════════════════════════════════════════════
banda ParamMalo ALMA
    cabal x FRENO

    ParamMalo ABRAZO SEMILLA SEMILLA RESPALDO ALMA
        vos ATOMO x DAR 0 FRENO
    CUERPO

CUERPO


// ══════════════════════════════════════════════════════════════
// ERROR 22: Sentencia inválida (token desconocido en posición de sentencia)
// Regla: error SEMICOLON
// ══════════════════════════════════════════════════════════════
nimais errorSentencia ABRAZO RESPALDO ALMA
    RAYA RAYA RAYA FRENO
    chotear ABRAZO "continua" RESPALDO FRENO
CUERPO


// ══════════════════════════════════════════════════════════════
// ERROR 23: ELSE sin IF válido
// syntax_error con sym.ELSE — chapus debe aparecer DENTRO de una
// función justo después de una sentencia, sin simon previo.
// ══════════════════════════════════════════════════════════════
nimais errorElse ABRAZO RESPALDO ALMA
    cabal n DAR 5 FRENO
    chapus ALMA
        chotear ABRAZO "else huerfano" RESPALDO FRENO
    CUERPO
CUERPO


// ══════════════════════════════════════════════════════════════
// ERROR 24: Fin de archivo inesperado
// syntax_error con sym.EOF — el archivo termina dentro de una función
// ══════════════════════════════════════════════════════════════
nimais errorEOF ABRAZO RESPALDO ALMA
    cabal x DAR 10 FRENO
    chotear ABRAZO x RESPALDO FRENO
// archivo termina aquí sin CUERPO ni nada más