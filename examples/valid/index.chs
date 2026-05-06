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

    Persona ABRAZO chisme nom SEMILLA cabal ed SEMILLA pisto sal RESPANDO ALMA
        vos ATOMO nombre DAR nom FRENO
        vos ATOMO edad   DAR ed  FRENO
        vos ATOMO salario DAR sal FRENO
        vos ATOMO activo  DAR deplano FRENO
    CUERPO

    barrio nimais mostrar ABRAZO RESPANDO ALMA
        chotear ABRAZO "Nombre : " CRUZ vos ATOMO nombre RESPANDO FRENO
        chotear ABRAZO "Edad   : " CRUZ vos ATOMO edad   RESPANDO FRENO
        chotear ABRAZO "Salario: " CRUZ vos ATOMO salario RESPANDO FRENO
        simon ABRAZO vos ATOMO activo GEMELOS deplano RESPANDO ALMA
            chotear ABRAZO "Estado : activo" RESPANDO FRENO
        CUERPO chapus ALMA
            chotear ABRAZO "Estado : inactivo" RESPANDO FRENO
        CUERPO
    CUERPO

    barrio cabal obtenerEdad ABRAZO RESPANDO ALMA
        vonos vos ATOMO edad FRENO
    CUERPO

    caquero nimais desactivar ABRAZO RESPANDO ALMA
        vos ATOMO activo DAR nel FRENO
    CUERPO

CUERPO

// ── Función con parámetros y retorno ─────────────────────────
cabal sumar ABRAZO cabal a SEMILLA cabal b RESPANDO ALMA
    vonos a CRUZ b FRENO
CUERPO

// ── Función void con bucles ───────────────────────────────────
nimais procesarArreglo ABRAZO cabal limite RESPANDO ALMA

    cabal i DAR 0 FRENO
    cabal suma DAR 0 FRENO

    // for clásico
    vuelta ABRAZO cabal j DAR 0 FRENO j PICO limite FRENO j DAR j CRUZ 1 RESPANDO ALMA
        numeros CAJON j TAPA DAR j ESTRELLA 2 FRENO
        suma DAR suma CRUZ numeros CAJON j TAPA FRENO
    CUERPO

    // while
    seguile ABRAZO i PICO limite RESPANDO ALMA
        simon ABRAZO i GEMELOS 3 RESPANDO ALMA
            chanin FRENO
        CUERPO
        suma CRUZ_DAR i FRENO
        i CRUZ_CRUZ FRENO
    CUERPO

    // do-while
    dale ALMA
        suma RAYA_DAR 1 FRENO
        i CRUZ_CRUZ FRENO
    CUERPO seguile ABRAZO i PICO limite CRUZ 2 RESPANDO FRENO

    chotear ABRAZO "Suma final: " CRUZ suma RESPANDO FRENO
CUERPO

// ── Función con switch ────────────────────────────────────────
nimais clasificar ABRAZO cabal valor RESPANDO ALMA
    chiripa ABRAZO valor RESPANDO ALMA
        wasa 1 OJOS
            chotear ABRAZO "Uno" RESPANDO FRENO
            cuaje FRENO
        wasa 2 OJOS
            chotear ABRAZO "Dos" RESPANDO FRENO
            cuaje FRENO
        wasa 3 OJOS
            chotear ABRAZO "Tres" RESPANDO FRENO
            cuaje FRENO
        porsiacaso OJOS
            chotear ABRAZO "Otro valor" RESPANDO FRENO
            cuaje FRENO
    CUERPO
CUERPO

// ── Función con try/catch y throw ────────────────────────────
cabal dividir ABRAZO cabal a SEMILLA cabal b RESPANDO ALMA
    simon ABRAZO b GEMELOS 0 RESPANDO ALMA
        morongazo "Division por cero no permitida" FRENO
    CUERPO
    vonos a RAMPA b FRENO
CUERPO

nimais probarExcepciones ABRAZO RESPANDO ALMA
    calale ALMA
        cabal res DAR dividir ABRAZO 10 SEMILLA 0 RESPANDO FRENO
        chotear ABRAZO res RESPANDO FRENO
    CUERPO atrapalo ABRAZO chisme err RESPANDO ALMA
        chotear ABRAZO "Error capturado: " CRUZ err RESPANDO FRENO
    CUERPO
CUERPO

// ── Función con operadores y ternario ────────────────────────
nimais operadores ABRAZO RESPANDO ALMA
    cabal x DAR 10 FRENO
    cabal y DAR 3 FRENO

    cabal suma  DAR x CRUZ y FRENO
    cabal resta DAR x RAYA y FRENO
    cabal mult  DAR x ESTRELLA y FRENO
    cabal divs  DAR x RAMPA y FRENO
    cabal mod   DAR x SOBRA y FRENO

    casaca esMayor DAR ABRAZO x BOCA y RESPANDO DUDA deplano OJOS nel FRENO

    chotear ABRAZO suma  RESPANDO FRENO
    chotear ABRAZO resta RESPANDO FRENO
    chotear ABRAZO mult  RESPANDO FRENO
    chotear ABRAZO divs  RESPANDO FRENO
    chotear ABRAZO mod   RESPANDO FRENO
    chotear ABRAZO esMayor RESPANDO FRENO

    // Operadores compuestos
    x CRUZ_DAR 5 FRENO
    x RAYA_DAR 2 FRENO
    x ESTRELLA_DAR 3 FRENO
    x RAMPA_DAR 2 FRENO
    x SOBRA_DAR 3 FRENO

    // Incremento / decremento
    x CRUZ_CRUZ FRENO
    y RAYA_RAYA FRENO
    chotear ABRAZO x RESPANDO FRENO
CUERPO

// ── Función con lógica booleana ───────────────────────────────
casaca evaluar ABRAZO cabal n RESPANDO ALMA
    vonos ABRAZO n BOCA 0 RESPANDO CADENA ABRAZO n PICO 100 RESPANDO FRENO
CUERPO

// ── Función con cadenas ───────────────────────────────────────
chisme construirMensaje ABRAZO chisme base SEMILLA cabal repeticiones RESPANDO ALMA
    chisme resultado DAR "" FRENO
    vuelta ABRAZO cabal i DAR 0 FRENO i PICO repeticiones FRENO i CRUZ_CRUZ RESPANDO ALMA
        resultado DAR resultado CRUZ base FRENO
    CUERPO
    vonos resultado FRENO
CUERPO

// ── Programa principal ────────────────────────────────────────
nimais main ABRAZO RESPANDO ALMA

    // Uso de clase
    Persona p DAR estrenar Persona ABRAZO "Juan" SEMILLA 25 SEMILLA 5000.0 RESPANDO FRENO
    p ATOMO mostrar ABRAZO RESPANDO FRENO

    // Uso de funciones
    cabal resultado DAR sumar ABRAZO 7 SEMILLA 8 RESPANDO FRENO
    chotear ABRAZO "7 + 8 = " CRUZ resultado RESPANDO FRENO

    procesarArreglo ABRAZO 5 RESPANDO FRENO
    clasificar ABRAZO 2 RESPANDO FRENO
    probarExcepciones ABRAZO RESPANDO FRENO
    operadores ABRAZO RESPANDO FRENO

    // Condicional con else-if encadenado
    cabal nota DAR 75 FRENO
    simon ABRAZO nota TECHO 90 RESPANDO ALMA
        chotear ABRAZO "Excelente" RESPANDO FRENO
    CUERPO chapus simon ABRAZO nota TECHO 70 RESPANDO ALMA
        chotear ABRAZO "Aprobado" RESPANDO FRENO
    CUERPO chapus simon ABRAZO nota TECHO 60 RESPANDO ALMA
        chotear ABRAZO "Regular" RESPANDO FRENO
    CUERPO chapus ALMA
        chotear ABRAZO "Reprobado" RESPANDO FRENO
    CUERPO

    // Booleanos y lógica
    casaca valido DAR evaluar ABRAZO 50 RESPANDO FRENO
    simon ABRAZO valido GEMELOS deplano RESPANDO ALMA
        chotear ABRAZO "Valor dentro de rango" RESPANDO FRENO
    CUERPO

    // Cadenas con tildes dentro del string (valido)
    chisme saludo DAR "Bienvenido al sistema de chapines" FRENO
    chotear ABRAZO saludo RESPANDO FRENO
    chotear ABRAZO construirMensaje ABRAZO "ha! " SEMILLA 3 RESPANDO RESPANDO FRENO

    // Arreglo
    numeros CAJON 0 TAPA DAR 99 FRENO
    chotear ABRAZO numeros CAJON 0 TAPA RESPANDO FRENO

    // Variable null
    Persona p2 DAR inutil FRENO

    chotear ABRAZO "Programa finalizado correctamente" RESPANDO FRENO
CUERPO
