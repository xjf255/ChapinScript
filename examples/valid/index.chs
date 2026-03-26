banda ContadorChapin {

        barrio cabal LIMITE = 5;
        chisme nombre;
        cabal total;
        pisto promedio;
        pistazo ahorro;
        cachito inicial;
        casaca activo;

        ContadorChapin(chisme nom, cabal t, pisto p, pistazo a, cachito i, casaca estado) {
            vos.nombre = nom;
            vos.total = t;
            vos.promedio = p;
            vos.ahorro = a;
            vos.inicial = i;
            vos.activo = estado;
        }

        nimais mostrarEstado() {
            chotear("==== ESTADO DEL CONTADOR ====");
            chotear("Nombre: " + vos.nombre);
            chotear("Total: " + vos.total);
            chotear("Promedio: " + vos.promedio);
            chotear("Ahorro: " + vos.ahorro);
            chotear("Inicial: " + vos.inicial);

            simon (vos.activo == deplano) {
                chotear("Estado: activo");
            } chapus {
                chotear("Estado: inactivo");
            }
        }

        cabal procesar(cabal numero) {
            cabal suma = 0;

            vuelta (cabal i = 0; i < numero; i = i + 1) {
                simon (i == 2) {
                    chanin;
                }

                simon (i > 10) {
                    cuaje;
                }

                suma = suma + i;
            }

            seguile (suma < 20) {
                suma = suma + 2;
            }

            dale {
                suma = suma - 1;
            } seguile (suma > 15)

            chiripa (numero) {
                wasa 1:
                    chotear("Caso 1");
                    cuaje

                wasa 2:
                    chotear("Caso 2");
                    cuaje

                wasa 3:
                    chotear("Caso 3");
                    cuaje

                porsiacaso:
                    chotear("Caso no contemplado");
                    cuaje
            }

            vonos suma
        }

    caquero:
        nimais validar(cabal edad) {
            simon (edad < 0) {
                morongazo "La edad no puede ser negativa"
            }

            simon (edad == 0) {
                morongazo "La edad no puede ser cero"
            }
        }
}

nimais main() {
    banda ContadorChapin contador = ContadorChapin("Fernando", 3, 10.5, 1500.755, 'F', deplano)

    contador.mostrarEstado()

    calale {
        contador.validar(5)

        cabal resultado = contador.procesar(7);
        chotear("Resultado final: " + resultado);

        casaca bandera = nel

        simon (resultado > 10) {
            bandera = deplano;
        } chapus {
            bandera = nel;
        }

        simon (bandera == deplano) {
            chotear("La bandera quedó en verdadero");
        } chapus {
            chotear("La bandera quedó en falso");
        }

    } atrapalo (chisme error) {
        chotear("Se atrapó un error: " + error);
    }

    chotear("Programa finalizado");
}