package org.example;

import org.example.lexer.Lexer;
import org.example.lexer.Token;
import org.example.lexer.TokenType;
import org.example.parser.LexerAdapter;
import org.example.parser.Parser;
import org.example.utils.FileManager;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            String source = FileManager.readFile("examples/valid/index.chs");

            Lexer parserLexer = new Lexer(new StringReader(source));
            LexerAdapter lexerAdapter = new LexerAdapter(parserLexer);
            Parser parser = new Parser(lexerAdapter);

            parser.parse();

            if (parser.getSyntaxErrors().isEmpty()) {
                System.out.println("Parseo exitoso");
            } else {
                System.out.println("Parseo no exitoso");
                System.out.println("Errores sintácticos: " + parser.getSyntaxErrors().size());
                System.out.println("Lista de errores");
                parser.getSyntaxErrors().forEach(System.err::println);
            }

            Lexer tokenLexer = new Lexer(new StringReader(source));

            List<Token> tokens = new ArrayList<>();
            Token token;
            while ((token = tokenLexer.yylex()) != null
                    && token.getType() != TokenType.EOF) {
                tokens.add(token);
            }

            FileManager.writeFile("output/tokens.txt", tokens.toString());
            FileManager.writeFile("output/errors.txt", tokenLexer.getErrors().toString());

            System.out.println("Tokens encontrados : " + tokens.size());
            System.out.println("Errores léxicos    : " + tokenLexer.getErrors().size());

            if (!tokenLexer.getErrors().isEmpty()) {
                System.err.println("Errores léxicos:");
                tokenLexer.getErrors().forEach(System.err::println);
            }

        } catch (IOException e) {
            System.err.println("Error al leer/escribir archivo: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}