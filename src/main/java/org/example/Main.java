package org.example;

import org.example.lexer.Lexer;
import org.example.lexer.Token;
import org.example.lexer.TokenType;
import org.example.parser.LexerAdapter;
import org.example.parser.Parser;
import org.example.reports.HtmlReportsGenerator;
import org.example.reports.SymbolInfo;
import org.example.reports.SymbolTableGenerator;
import org.example.utils.FileManager;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        try {
//            String source = FileManager.readFile("examples/valid/index.chs");
//            String source = FileManager.readFile("examples/invalid/error_lexico_01.chs");
            String source = FileManager.readFile("examples/invalid/error_sintactico_01.chs");

            Lexer tokenLexer = new Lexer(new StringReader(source));

            List<Token> tokens = new ArrayList<>();
            Token token;
            while ((token = tokenLexer.yylex()) != null
                    && token.getType() != TokenType.EOF) {
                tokens.add(token);
            }

            System.out.println("Tokens encontrados : " + tokens.size());
            System.out.println("Errores léxicos    : " + tokenLexer.getErrors().size());

            if (!tokenLexer.getErrors().isEmpty()) {
                System.err.println("Errores léxicos:");
                tokenLexer.getErrors().forEach(System.err::println);
            }

            Lexer parserLexer = new Lexer(new StringReader(source));
            LexerAdapter lexerAdapter = new LexerAdapter(parserLexer);
            Parser parser = new Parser(lexerAdapter);

            parser.parse();

            if (parser.getSyntaxErrors().isEmpty()) {
                System.out.println("Parseo exitoso");
            } else {
                System.out.println("Parseo con errores");
                System.out.println("Errores sintácticos: " + parser.getSyntaxErrors().size());
                System.out.println("Lista de errores:");
                parser.getSyntaxErrors().forEach(System.err::println);
            }

            List<SymbolInfo> symbols = SymbolTableGenerator.generate(tokens);

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

            System.out.println("Símbolos encontrados: " + symbols.size());
            System.out.println("Reportes generados correctamente en la carpeta output.");

        } catch (IOException e) {
            System.err.println("Error al leer/escribir archivo: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}