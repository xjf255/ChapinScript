package org.example;

import org.example.lexer.Lexer;
import org.example.lexer.Token;
import org.example.lexer.TokenType;
import org.example.utils.FileManager;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            String source = FileManager.readFile("examples/valid/index.chs");
            Lexer lexer = new Lexer(new StringReader(source));

            // 1. Consumir todos los tokens
            List<Token> tokens = new ArrayList<>();
            Token token;
            while ((token = lexer.yylex()) != null
                    && token.getType() != TokenType.EOF) {
                tokens.add(token);
            }

            // 2. Escribir resultados en archivos separados
            FileManager.writeFile("output/tokens.txt", tokens.toString());
            FileManager.writeFile("output/errors.txt", lexer.getErrors().toString());

            // 3. Resumen en consola
            System.out.println("Tokens encontrados : " + tokens.size());
            System.out.println("Errores léxicos    : " + lexer.getErrors().size());

            if (!lexer.getErrors().isEmpty()) {
                System.err.println("Errores:");
                lexer.getErrors().forEach(System.err::println);
            }

        } catch (IOException e) {
            System.err.println("Error al leer/escribir archivo: " + e.getMessage());
        }
    }
}