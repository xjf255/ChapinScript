# ChapinScript

## Requisitos
- JDK 17
- Maven 3.9+

## Cómo correr
mvn clean compile
mvn test

## Cómo ejecutar
java -jar target/chapinscript.jar examples/valid/hola_mundo.chs

## Estructura del compilador
- lexer
- parser
- ast
- semantic
- generator
- report

## Flujo de trabajo Git
- main estable
- develop integración
- feature branches