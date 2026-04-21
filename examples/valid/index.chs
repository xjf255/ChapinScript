banda ContadorChapin ALMA

    barrio cabal LIMITE DAR 5 FRENO
    chisme nombre FRENO
    chisme nombres CAJON 5 TAPA FRENO @
    cabal total FRENO
    pisto promedio FRENO
    pistazo ahorro FRENO
    cachito inicial FRENO
    casaca activo FRENO
@
    ContadorChapin ABRAZO chisme nom SEMILLA cabal t SEMILLA pisto p SEMILLA pistazo a SEMILLA cachito i SEMILLA casaca estado RESPANDO
        vos ATOMO nombre DAR nom FRENO
        vos ATOMO total DAR t FRENO
        vos ATOMO promedio DAR p FRENO
        vos ATOMO ahorro DAR a FRENO
        vos ATOMO inicial DAR i FRENO
        vos ATOMO activo DAR estado FRENO


    nimais mostrarEstado ABRAZO RESPANDO ALMA
        chotear ABRAZO "==== ESTADO DEL CONTADOR ====" RESPANDO FRENO
        chotear ABRAZO "Nombre: " CRUZ vos ATOMO nombre RESPANDO FRENO
        chotear ABRAZO "Total: " CRUZ vos ATOMO total RESPANDO FRENO
        chotear ABRAZO "Promedio: " CRUZ vos ATOMO promedio RESPANDO FRENO
        chotear ABRAZO "Ahorro: " CRUZ vos ATOMO ahorro RESPANDO FRENO
        chotear ABRAZO "Inicial: " CRUZ vos ATOMO inicial RESPANDO FRENO

        simon ABRAZO vos ATOMO activo GEMELOS deplano RESPANDO ALMA
            chotear ABRAZO "Estado: activo" RESPANDO FRENO
        CUERPO chapus ALMA
            chotear ABRAZO "Estado: inactivo" RESPANDO FRENO
        CUERPO
    CUERPO

    cabal procesar ABRAZO cabal numero RESPANDO ALMA
        cabal suma DAR 0 FRENO

        vuelta ABRAZO cabal i DAR 0 FRENO i PICO numero FRENO i DAR i CRUZ 1 RESPANDO ALMA
            simon ABRAZO i GEMELOS 2 RESPANDO ALMA
                chanin FRENO
            CUERPO

            simon ABRAZO i BOCA 10 RESPANDO ALMA
                cuaje FRENO
            CUERPO

            suma DAR suma CRUZ i FRENO
        CUERPO

        seguile ABRAZO suma PICO 20 RESPANDO ALMA
            suma DAR suma CRUZ 2 FRENO
        CUERPO

        dale ALMA
            suma DAR suma RAYA 1 FRENO
        CUERPO seguile ABRAZO suma BOCA 15 RESPANDO FRENO

        chiripa ABRAZO numero RESPANDO ALMA
            wasa 1 OJOS
                chotear ABRAZO "Caso 1" RESPANDO FRENO
                cuaje FRENO

            wasa 2 OJOS
                chotear ABRAZO "Caso 2" RESPANDO FRENO
                cuaje FRENO

            wasa 3 OJOS
                chotear ABRAZO "Caso 3" RESPANDO FRENO
                cuaje FRENO

            porsiacaso OJOS
                chotear ABRAZO "Caso no contemplado" RESPANDO FRENO
                cuaje FRENO
        CUERPO

        vonos suma FRENO
    CUERPO

    caquero nimais validar ABRAZO cabal edad RESPANDO ALMA
        simon ABRAZO edad PICO 0 RESPANDO ALMA
            morongazo "La edad no puede ser negativa" FRENO
        CUERPO

        simon ABRAZO edad GEMELOS 0 RESPANDO ALMA
            morongazo "La edad no puede ser cero" FRENO
        CUERPO
    CUERPO
CUERPO

nimais main ABRAZO RESPANDO ALMA
    contador ATOMO mostrarEstado ABRAZO RESPANDO FRENO

    calale ALMA
        contador ATOMO validar ABRAZO 5 RESPANDO FRENO

        cabal resultado DAR contador ATOMO procesar ABRAZO 7 RESPANDO FRENO
        chotear ABRAZO "Resultado final: " CRUZ resultado RESPANDO FRENO

        casaca bandera DAR nel FRENO

        simon ABRAZO resultado BOCA 10 RESPANDO ALMA
            bandera DAR deplano FRENO
        CUERPO chapus ALMA
            bandera DAR nel FRENO
        CUERPO

        simon ABRAZO bandera GEMELOS deplano RESPANDO ALMA
            chotear ABRAZO "La bandera quedó en verdadero" RESPANDO FRENO
        CUERPO chapus ALMA
            chotear ABRAZO "La bandera quedó en falso" RESPANDO FRENO
        CUERPO

    CUERPO atrapalo ABRAZO chisme error RESPANDO ALMA
        chotear ABRAZO "Se atrapó un error: " CRUZ error RESPANDO FRENO
    CUERPO

    chotear ABRAZO "Programa finalizado" RESPANDO FRENO
CUERPO