package org.example;

import org.example.lexer.Lexer;
import org.example.lexer.Token;
import org.example.lexer.TokenType;
import org.example.parser.LexerAdapter;
import org.example.parser.Parser;
import org.example.reports.HtmlReportsGenerator;
import org.example.reports.SymbolTableGenerator;
import org.example.semantic.SymbolInfo;
import org.example.utils.FileManager;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final String DEFAULT_INPUT = "examples/valid/index.chs";
    private static final String DEFAULT_ERROR_INPUT = "examples/invalid/errores_sintacticos_01.chs";
    private static final String OUTPUT_DIR = "output";

    public static void main(String[] args) {
        try {
            String inputPath = args.length > 0 ? args[0] : DEFAULT_ERROR_INPUT;
            String source = FileManager.readFile(inputPath);

            Lexer tokenLexer = new Lexer(new StringReader(source));
            List<Token> tokens = new ArrayList<>();

            Token token;
            while ((token = tokenLexer.yylex()) != null && token.getType() != TokenType.EOF) {
                tokens.add(token);
            }

            boolean hasLexicalErrors = !tokenLexer.getErrors().isEmpty();

            System.out.println("====================================");
            System.out.println("RESULTADO DEL ANÁLISIS LÉXICO");
            System.out.println("====================================");
            System.out.println("Tokens encontrados : " + tokens.size());
            System.out.println("Errores léxicos    : " + tokenLexer.getErrors().size());

            if (hasLexicalErrors) {
                System.err.println("\nLista de errores léxicos:");
                tokenLexer.getErrors().forEach(System.err::println);
            }

            String cppCode = null;
            Parser parser = null;

            if (!hasLexicalErrors) {
                Lexer parserLexer = new Lexer(new StringReader(source));
                LexerAdapter lexerAdapter = new LexerAdapter(parserLexer);
                parser = new Parser(lexerAdapter);

                try {
                    Object parseResult = parser.parse().value;
                    if (parseResult instanceof String) {
                        cppCode = (String) parseResult;
                    }
                } catch (Exception e) {
                    System.err.println("\nSe produjo una excepción durante el parseo:");
                    System.err.println(e.getMessage());
                }
            }

            List<Parser.SyntaxError> syntaxErrors =
                    parser != null ? parser.getSyntaxErrors() : new ArrayList<>();

            boolean hasSyntaxErrors = !syntaxErrors.isEmpty();

            System.out.println("\n====================================");
            System.out.println("RESULTADO DEL ANÁLISIS SINTÁCTICO");
            System.out.println("====================================");

            if (hasLexicalErrors) {
                System.out.println("Análisis sintáctico omitido por errores léxicos.");
            } else if (!hasSyntaxErrors) {
                System.out.println("Parseo exitoso");
            } else {
                System.out.println("Parseo con errores");
                System.out.println("Errores sintácticos: " + syntaxErrors.size());
                System.err.println("\nLista de errores sintácticos:");
                syntaxErrors.forEach(System.err::println);
            }

            System.out.println("\n====================================");
            System.out.println("RESULTADO DE LA TRANSPILACIÓN");
            System.out.println("====================================");

            boolean transpilationOk = !hasLexicalErrors && !hasSyntaxErrors && cppCode != null;

            if (transpilationOk) {
                System.out.println("Código C++ generado correctamente.");
                System.out.println("Salida transpilada:");
                System.out.println(cppCode);
            } else if (hasLexicalErrors || hasSyntaxErrors) {
                System.out.println("No se generó código C++ válido debido a errores de compilación.");
            } else {
                System.out.println("No se pudo generar el código C++.");
            }
            SymbolTableGenerator tableGenerator = new SymbolTableGenerator();
            List<SymbolInfo> symbols = tableGenerator.generate(tokens);

            FileManager.writeFile(
                    OUTPUT_DIR + "/output.cpp",
                    transpilationOk
                            ? cppCode
                            : "// No se generó código C++ válido por errores léxicos o sintácticos."
            );

            HtmlReportsGenerator.generateErrorsReport(
                    OUTPUT_DIR + "/errors_report.html",
                    tokenLexer.getErrors(),
                    syntaxErrors
            );

            HtmlReportsGenerator.generateTokensReport(
                    OUTPUT_DIR + "/tokens_report.html",
                    tokens
            );

            HtmlReportsGenerator.generateSymbolTableReport(
                    OUTPUT_DIR + "/symbol_table.html",
                    symbols
            );

            System.out.println("\n====================================");
            System.out.println("RESUMEN FINAL");
            System.out.println("====================================");
            System.out.println("Símbolos encontrados : " + symbols.size());
            System.out.println("Errores léxicos      : " + tokenLexer.getErrors().size());
            System.out.println("Errores sintácticos  : " + syntaxErrors.size());
            System.out.println("C++ generado         : " + (transpilationOk ? "Sí" : "No"));
            System.out.println("Archivo generado     : " + OUTPUT_DIR + "/output.cpp");
            System.out.println("Reportes generados correctamente en la carpeta " + OUTPUT_DIR + ".");

        } catch (IOException e) {
            System.err.println("Error al leer/escribir archivo: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado:");
            e.printStackTrace();
        }
    }
}