package org.example;

import org.example.ast.ASTPrinter;
import org.example.ast.ProgramNode;
import org.example.lexer.Lexer;
import org.example.lexer.Token;
import org.example.lexer.TokenType;
import org.example.parser.LexerAdapter;
import org.example.parser.Parser;
import org.example.reports.HtmlReportsGenerator;
import org.example.semantic.SymbolInfo;
import org.example.semantic.SymbolTable;
import org.example.utils.FileManager;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        try {
            // Cambia aquí el archivo de prueba
//            String source = FileManager.readFile("examples/valid/index.chs");
//            String source = FileManager.readFile("examples/invalid/error_lexico_01.chs");
            String source = FileManager.readFile("examples/valid/index.chs");

            /*
             * =========================
             * 1. ANÁLISIS LÉXICO
             * =========================
             */
            Lexer tokenLexer = new Lexer(new StringReader(source));

            List<Token> tokens = new ArrayList<>();
            Token token;
            while ((token = tokenLexer.yylex()) != null
                    && token.getType() != TokenType.EOF) {
                tokens.add(token);
            }

            System.out.println("====================================");
            System.out.println("RESULTADO DEL ANÁLISIS LÉXICO");
            System.out.println("====================================");
            System.out.println("Tokens encontrados : " + tokens.size());
            System.out.println("Errores léxicos    : " + tokenLexer.getErrors().size());

            if (!tokenLexer.getErrors().isEmpty()) {
                System.err.println("\nLista de errores léxicos:");
                tokenLexer.getErrors().forEach(System.err::println);
            }

            /*
             * =========================
             * 2. ANÁLISIS SINTÁCTICO + AST
             * =========================
             */
            Lexer parserLexer = new Lexer(new StringReader(source));
            LexerAdapter lexerAdapter = new LexerAdapter(parserLexer);
            Parser parser = new Parser(lexerAdapter);

            ProgramNode ast = null;
            String prettyAST = null;

            try {
                Object parseResult = parser.parse().value;

                if (parseResult instanceof ProgramNode) {
                    ast = (ProgramNode) parseResult;
                    prettyAST = ASTPrinter.format(ast,0);
                }
            } catch (Exception e) {
                System.err.println("\nSe produjo una excepción durante el parseo:");
                System.err.println(e.getMessage());
            }

            System.out.println("\n====================================");
            System.out.println("RESULTADO DEL ANÁLISIS SINTÁCTICO");
            System.out.println("====================================");

            if (parser.getSyntaxErrors().isEmpty()) {
                System.out.println("Parseo exitoso");
            } else {
                System.out.println("Parseo con errores");
                System.out.println("Errores sintácticos: " + parser.getSyntaxErrors().size());
                System.err.println("\nLista de errores sintácticos:");
                parser.getSyntaxErrors().forEach(System.err::println);
            }

            /*
             * =========================
             * 3. RESULTADO DEL AST
             * =========================
             */
            System.out.println("\n====================================");
            System.out.println("RESULTADO DEL AST");
            System.out.println("====================================");

            if (ast != null) {
                System.out.println("AST generado correctamente.");
                System.out.println("Nodo raíz: " + ast.getClass().getSimpleName());
                System.out.println("Contenido del AST:");
                System.out.println(prettyAST);
            } else {
                System.out.println("No se pudo construir el AST.");
            }

            /*
             * =========================
             * 4. TABLA DE SÍMBOLOS BÁSICA DESDE TOKENS
             * =========================
             */
            List<SymbolInfo> symbols = SymbolTable.generate(tokens);

            /*
             * =========================
             * 5. SALIDA A ARCHIVOS
             * =========================
             */
            FileManager.writeFile(
                    "output/tokens.txt",
                    tokens.stream()
                            .map(Object::toString)
                            .collect(Collectors.joining(System.lineSeparator()))
            );

            FileManager.writeFile(
                    "output/lexical_errors.txt",
                    tokenLexer.getErrors().stream()
                            .map(Object::toString)
                            .collect(Collectors.joining(System.lineSeparator()))
            );

            FileManager.writeFile(
                    "output/syntactic_errors.txt",
                    parser.getSyntaxErrors().stream()
                            .map(Object::toString)
                            .collect(Collectors.joining(System.lineSeparator()))
            );

            FileManager.writeFile(
                    "output/ast.txt",
                    ast != null ? prettyAST : "No se pudo construir el AST."
            );

            HtmlReportsGenerator.generateErrorsReport(
                    "output/errors_report.html",
                    tokenLexer.getErrors(),
                    parser.getSyntaxErrors()
            );

            HtmlReportsGenerator.generateTokensReport(
                    "output/tokens_report.html",
                    tokens
            );

            HtmlReportsGenerator.generateSymbolTableReport(
                    "output/symbol_table.html",
                    symbols
            );

            System.out.println("\n====================================");
            System.out.println("RESUMEN FINAL");
            System.out.println("====================================");
            System.out.println("Símbolos encontrados: " + symbols.size());
            System.out.println("AST generado        : " + (ast != null ? "Sí" : "No"));
            System.out.println("Reportes generados correctamente en la carpeta output.");

        } catch (IOException e) {
            System.err.println("Error al leer/escribir archivo: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado:");
            e.printStackTrace();
        }
    }
}