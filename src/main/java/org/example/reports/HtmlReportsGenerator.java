package org.example.reports;

import org.example.lexer.LexicalError;
import org.example.lexer.Token;
import org.example.semantic.SymbolInfo;
import org.example.utils.FileManager;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HtmlReportsGenerator {

    public static void generateErrorsReport(
            String outputPath,
            List<LexicalError> lexicalErrors,
            List<?> syntacticErrors
    ) throws IOException {

        StringBuilder html = new StringBuilder();

        html.append(getHeader("Bitácora de Errores"));

        html.append("<h1>Bitácora de Errores Léxicos y Sintácticos</h1>");

        html.append("<div class='info'>");
        html.append("<p><strong>Fecha:</strong> ")
                .append(getDate())
                .append("</p>");
        html.append("<p><strong>Total errores léxicos:</strong> ")
                .append(lexicalErrors.size())
                .append("</p>");
        html.append("<p><strong>Total errores sintácticos:</strong> ")
                .append(syntacticErrors.size())
                .append("</p>");
        html.append("</div>");

        html.append("<h2>Errores Léxicos</h2>");

        if (lexicalErrors.isEmpty()) {
            html.append("<div class='empty'>No se encontraron errores léxicos.</div>");
        } else {
            html.append("""
                    <table>
                        <tr>
                            <th>No.</th>
                            <th>Lexema</th>
                            <th>Descripción</th>
                            <th>Línea</th>
                            <th>Columna</th>
                        </tr>
                    """);

            for (int i = 0; i < lexicalErrors.size(); i++) {
                LexicalError error = lexicalErrors.get(i);

                html.append("<tr>");
                html.append("<td>").append(i + 1).append("</td>");
                html.append("<td>").append(escape(error.getLexeme())).append("</td>");
                html.append("<td>").append(escape(error.getDescription())).append("</td>");
                html.append("<td>").append(error.getLine()).append("</td>");
                html.append("<td>").append(error.getColumn()).append("</td>");
                html.append("</tr>");
            }

            html.append("</table>");
        }

        html.append("<h2>Errores Sintácticos</h2>");

        if (syntacticErrors.isEmpty()) {
            html.append("<div class='empty'>No se encontraron errores sintácticos.</div>");
        } else {
            html.append("""
                    <table>
                        <tr>
                            <th>No.</th>
                            <th>Descripción</th>
                        </tr>
                    """);

            for (int i = 0; i < syntacticErrors.size(); i++) {
                html.append("<tr>");
                html.append("<td>").append(i + 1).append("</td>");
                html.append("<td>")
                        .append(escape(String.valueOf(syntacticErrors.get(i))))
                        .append("</td>");
                html.append("</tr>");
            }

            html.append("</table>");
        }

        html.append(getFooter());

        FileManager.writeFile(outputPath, html.toString());
    }

    public static void generateTokensReport(
            String outputPath,
            List<Token> tokens
    ) throws IOException {

        StringBuilder html = new StringBuilder();

        html.append(getHeader("Bitácora de Tokens"));

        html.append("<h1>Bitácora de Tokens</h1>");

        html.append("""
                <table>
                    <tr>
                        <th>No.</th>
                        <th>Token</th>
                    </tr>
                """);

        for (int i = 0; i < tokens.size(); i++) {
            html.append("<tr>");
            html.append("<td>").append(i + 1).append("</td>");
            html.append("<td>").append(escape(tokens.get(i).toString())).append("</td>");
            html.append("</tr>");
        }

        html.append("</table>");

        html.append(getFooter());

        FileManager.writeFile(outputPath, html.toString());
    }

    public static void generateSymbolTableReport(
            String outputPath,
            List<SymbolInfo> symbols
    ) throws IOException {

        StringBuilder html = new StringBuilder();

        html.append(getHeader("Tabla de Símbolos"));

        html.append("<h1>Tabla de Símbolos</h1>");

        html.append("<div class='info'>");
        html.append("<p><strong>Fecha:</strong> ")
                .append(getDate())
                .append("</p>");
        html.append("<p><strong>Total de símbolos:</strong> ")
                .append(symbols.size())
                .append("</p>");
        html.append("</div>");

        if (symbols.isEmpty()) {
            html.append("<div class='empty'>No se encontraron símbolos.</div>");
        } else {
            html.append("""
                    <table>
                        <tr>
                            <th>No.</th>
                            <th>Nombre</th>
                            <th>Categoría</th>
                            <th>Tipo de dato</th>
                            <th>Ámbito</th>
                            <th>Línea</th>
                            <th>Columna</th>
                            <th>Valor inicial</th>
                        </tr>
                    """);

            for (int i = 0; i < symbols.size(); i++) {
                SymbolInfo s = symbols.get(i);

                html.append("<tr>");
                html.append("<td>").append(i + 1).append("</td>");
                html.append("<td>").append(escape(s.getName())).append("</td>");
                html.append("<td>").append(escape(s.getCategory())).append("</td>");
                html.append("<td>").append(escape(s.getDataType())).append("</td>");
                html.append("<td>").append(escape(s.getScope())).append("</td>");
                html.append("<td>").append(s.getLine()).append("</td>");
                html.append("<td>").append(s.getColumn()).append("</td>");
                html.append("<td>").append(escape(s.getValue())).append("</td>");
                html.append("</tr>");
            }

            html.append("</table>");
        }

        html.append(getFooter());

        FileManager.writeFile(outputPath, html.toString());
    }

    private static String getHeader(String title) {
        return """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>""" + title + """
                    </title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            margin: 30px;
                            background-color: #f4f6f9;
                            color: #222;
                        }
                        h1, h2 {
                            color: #1f3b73;
                        }
                        .info {
                            background: #e8f0ff;
                            padding: 15px;
                            border-left: 5px solid #2b6cb0;
                            margin-bottom: 20px;
                            border-radius: 8px;
                        }
                        table {
                            width: 100%;
                            border-collapse: collapse;
                            margin-bottom: 30px;
                            background: white;
                            box-shadow: 0 2px 8px rgba(0,0,0,0.08);
                        }
                        th, td {
                            border: 1px solid #ddd;
                            padding: 10px;
                            text-align: left;
                            vertical-align: top;
                        }
                        th {
                            background-color: #1f3b73;
                            color: white;
                        }
                        tr:nth-child(even) {
                            background-color: #f9fbfd;
                        }
                        .empty {
                            padding: 15px;
                            background: #e6ffed;
                            border-left: 5px solid #2f855a;
                            border-radius: 8px;
                        }
                    </style>
                </head>
                <body>
                """;
    }

    private static String getFooter() {
        return """
                </body>
                </html>
                """;
    }

    private static String getDate() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

    private static String escape(String text) {
        if (text == null) return "";
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}