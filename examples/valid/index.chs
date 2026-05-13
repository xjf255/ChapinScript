/* ============================================================
   archivo_correcto.chap
   Archivo sin errores — cubre todas las construcciones del
   lenguaje ChapinScript para verificar compilación exitosa.
   ============================================================ */

// ── Variables globales ───────────────────────────────────────
cabal contadorGlobal DAR 0 FRENO
chisme mensajeGlobal DAR "Hola desde ChapinScript" FRENO
fijo cabal MAXIMO DAR 100 FRENO
casaca sistemaActivo DAR deplano FRENO

// ── Arreglos ─────────────────────────────────────────────────
cabal numeros CAJON 5 TAPA FRENO
pisto precios CAJON 3 TAPA DAR ALMA 1.5 SEMILLA 2.75 SEMILLA 3.0 CUERPO FRENO

// ── Clase con constructor, campos y métodos ──────────────────
banda Persona ALMA

    barrio chisme nombre FRENO
    barrio cabal edad FRENO
    caquero pisto salario FRENO
    caquero casaca activo FRENO

    Persona ABRAZO chisme nom SEMILLA cabal ed SEMILLA pisto sal RESPALDO ALMA
        vos ATOMO nombre DAR nom FRENO
        vos ATOMO edad   DAR ed  FRENO
        vos ATOMO salario DAR sal FRENO
        vos ATOMO activo  DAR deplano FRENO
    CUERPO

    barrio nimais mostrar ABRAZO RESPALDO ALMA
        chotear ABRAZO "Nombre : " CRUZ vos ATOMO nombre RESPALDO FRENO
        chotear ABRAZO "Edad   : " CRUZ vos ATOMO edad   RESPALDO FRENO
        chotear ABRAZO "Salario: " CRUZ vos ATOMO salario RESPALDO FRENO
        simon ABRAZO vos ATOMO activo GEMELOS deplano RESPALDO ALMA
            chotear ABRAZO "Estado : activo" RESPALDO FRENO
        CUERPO chapus ALMA
            chotear ABRAZO "Estado : inactivo" RESPALDO FRENO
        CUERPO
    CUERPO

    barrio cabal obtenerEdad ABRAZO RESPALDO ALMA
        vonos vos ATOMO edad FRENO
    CUERPO

    caquero nimais desactivar ABRAZO RESPALDO ALMA
        vos ATOMO activo DAR nel FRENO
    CUERPO

CUERPO

// ── Función con parámetros y retorno ─────────────────────────
cabal sumar ABRAZO cabal a SEMILLA cabal b RESPALDO ALMA
    vonos a CRUZ b FRENO
CUERPO

// ── Función void con bucles ───────────────────────────────────
nimais procesarArreglo ABRAZO cabal limite RESPALDO ALMA

    cabal i DAR 0 FRENO
    cabal suma DAR 0 FRENO

    // for clásico
    vuelta ABRAZO cabal j DAR 0 FRENO j PICO limite FRENO j DAR j CRUZ 1 RESPALDO ALMA
        numeros CAJON j TAPA DAR j ESTRELLA 2 FRENO
        suma DAR suma CRUZ numeros CAJON j TAPA FRENO
    CUERPO

    // while
    seguile ABRAZO i PICO limite RESPALDO ALMA
        simon ABRAZO i GEMELOS 3 RESPALDO ALMA
            chanin FRENO
        CUERPO
        suma CRUZ_DAR i FRENO
        i CRUZ_CRUZ FRENO
    CUERPO

    // do-while
    dale ALMA
        suma RAYA_DAR 1 FRENO
        i CRUZ_CRUZ FRENO
    CUERPO seguile ABRAZO i PICO limite CRUZ 2 RESPALDO FRENO

    chotear ABRAZO "Suma final: " CRUZ suma RESPALDO FRENO
CUERPO

// ── Función con switch ────────────────────────────────────────
nimais clasificar ABRAZO cabal valor RESPALDO ALMA
    chiripa ABRAZO valor RESPALDO ALMA
        wasa 1 OJOS
            chotear ABRAZO "Uno" RESPALDO FRENO
            cuaje FRENO
        wasa 2 OJOS
            chotear ABRAZO "Dos" RESPALDO FRENO
            cuaje FRENO
        wasa 3 OJOS
            chotear ABRAZO "Tres" RESPALDO FRENO
            cuaje FRENO
        porsiacaso OJOS
            chotear ABRAZO "Otro valor" RESPALDO FRENO
            cuaje FRENO
    CUERPO
CUERPO

// ── Función con try/catch y throw ────────────────────────────
cabal dividir ABRAZO cabal a SEMILLA cabal b RESPALDO ALMA
    simon ABRAZO b GEMELOS 0 RESPALDO ALMA
        morongazo "Division por cero no permitida" FRENO
    CUERPO
    vonos a RAMPA b FRENO
CUERPO

nimais probarExcepciones ABRAZO RESPALDO ALMA
    calale ALMA
        cabal res DAR dividir ABRAZO 10 SEMILLA 0 RESPALDO FRENO
        chotear ABRAZO res RESPALDO FRENO
    CUERPO atrapalo ABRAZO chisme err RESPALDO ALMA
        chotear ABRAZO "Error capturado: " CRUZ err RESPALDO FRENO
    CUERPO
CUERPO

// ── Función con operadores y ternario ────────────────────────
nimais operadores ABRAZO RESPALDO ALMA
    cabal x DAR 10 FRENO
    cabal y DAR 3 FRENO

    cabal suma  DAR x CRUZ y FRENO
    cabal resta DAR x RAYA y FRENO
    cabal mult  DAR x ESTRELLA y FRENO
    cabal divs  DAR x RAMPA y FRENO
    cabal mod   DAR x SOBRA y FRENO

    casaca esMayor DAR ABRAZO x BOCA y RESPALDO DUDA deplano OJOS nel FRENO

    chotear ABRAZO suma  RESPALDO FRENO
    chotear ABRAZO resta RESPALDO FRENO
    chotear ABRAZO mult  RESPALDO FRENO
    chotear ABRAZO divs  RESPALDO FRENO
    chotear ABRAZO mod   RESPALDO FRENO
    chotear ABRAZO esMayor RESPALDO FRENO

    // Operadores compuestos
    x CRUZ_DAR 5 FRENO
    x RAYA_DAR 2 FRENO
    x ESTRELLA_DAR 3 FRENO
    x RAMPA_DAR 2 FRENO
    x SOBRA_DAR 3 FRENO

    // Incremento / decremento
    x CRUZ_CRUZ FRENO
    y RAYA_RAYA FRENO
    chotear ABRAZO x RESPALDO FRENO
CUERPO

// ── Función con lógica booleana ───────────────────────────────
casaca evaluar ABRAZO cabal n RESPALDO ALMA
    vonos ABRAZO n BOCA 0 RESPALDO CADENA ABRAZO n PICO 100 RESPALDO FRENO
CUERPO

// ── Función con cadenas ───────────────────────────────────────
chisme construirMensaje ABRAZO chisme base SEMILLA cabal repeticiones RESPALDO ALMA
    chisme resultado DAR "" FRENO
    vuelta ABRAZO cabal i DAR 0 FRENO i PICO repeticiones FRENO i CRUZ_CRUZ RESPALDO ALMA
        resultado DAR resultado CRUZ base FRENO
    CUERPO
    vonos resultado FRENO
CUERPO

// ── Programa principal ────────────────────────────────────────
nimais main ABRAZO RESPALDO ALMA

    // Uso de clase
    Persona p DAR estrenar Persona ABRAZO "Juan" SEMILLA 25 SEMILLA 5000.0 RESPALDO FRENO
    p ATOMO mostrar ABRAZO RESPALDO FRENO

    // Uso de funciones
    cabal resultado DAR sumar ABRAZO 7 SEMILLA 8 RESPALDO FRENO
    chotear ABRAZO "7 + 8 = " CRUZ resultado RESPALDO FRENO

    procesarArreglo ABRAZO 5 RESPALDO FRENO
    clasificar ABRAZO 2 RESPALDO FRENO
    probarExcepciones ABRAZO RESPALDO FRENO
    operadores ABRAZO RESPALDO FRENO

    // Condicional con else-if encadenado
    cabal nota DAR 75 FRENO
    simon ABRAZO nota TECHO 90 RESPALDO ALMA
        chotear ABRAZO "Excelente" RESPALDO FRENO
    CUERPO chapus simon ABRAZO nota TECHO 70 RESPALDO ALMA
        chotear ABRAZO "Aprobado" RESPALDO FRENO
    CUERPO chapus simon ABRAZO nota TECHO 60 RESPALDO ALMA
        chotear ABRAZO "Regular" RESPALDO FRENO
    CUERPO chapus ALMA
        chotear ABRAZO "Reprobado" RESPALDO FRENO
    CUERPO

    // Booleanos y lógica
    casaca valido DAR evaluar ABRAZO 50 RESPALDO FRENO
    simon ABRAZO valido GEMELOS deplano RESPALDO ALMA
        chotear ABRAZO "Valor dentro de rango" RESPALDO FRENO
    CUERPO

    // Cadenas con tildes dentro del string (valido)
    chisme saludo DAR "Bienvenido al sistema de chapines" FRENO
    chotear ABRAZO saludo RESPALDO FRENO
    chotear ABRAZO construirMensaje ABRAZO "ha! " SEMILLA 3 RESPALDO RESPALDO FRENO

    // Arreglo
    numeros CAJON 0 TAPA DAR 99 FRENO
    chotear ABRAZO numeros CAJON 0 TAPA RESPALDO FRENO

    // Variable null
    Persona p2 DAR inutil FRENO

    chotear ABRAZO "Programa finalizado correctamente" RESPALDO FRENO
CUERPO
