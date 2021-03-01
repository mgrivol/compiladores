package br.ufscar.dc.compiladores.trabalho1;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

public class Principal {
    public static void main(String args[]) throws IOException {
        // Tente criar um arquivo parar a saida
        PrintWriter writer = new PrintWriter(args[1]);
   
        CharStream cs = CharStreams.fromFileName(args[0]);
        LALexer lex = new LALexer(cs);
        
        Token t = null;
        boolean erro = false;
        String buffer = new String();
        
        while ((t=lex.nextToken()).getType() != Token.EOF && !erro) {
            // padrao identifica a CLASSE do token
            String padrao = LALexer.VOCABULARY.getSymbolicName(t.getType());

            if (padrao.equals("PALAVRA_CHAVE") || padrao.equals("DELIM") || 
                padrao.equals("OPERADORES")) {
                // Se o padrao do token for uma PALAVRA_CHAVE ou DELIM
                // System.out.println("<\'" + t.getText() + "\',\'" + t.getText() + "\'>");
                buffer += "<\'" + t.getText() + "\',\'" + t.getText() + "\'>";
            }
            else if (padrao.equals("IDENT") || padrao.equals("CADEIA") ||
                     padrao.equals("NUM_INT") ) {
                // Se o padrao do token for um IDENT ou CADEIA ou NUM_INT
                // System.out.println("<\'" + t.getText() + "\'," +  padrao + ">");
                buffer += "<\'" + t.getText() + "\'," +  padrao + ">";
            }
            else if (padrao.equals("ERRO") || padrao.equals("COMENTARIO_ERRO")) {
                if (padrao.equals("COMENTARIO_ERRO")) {
                    buffer += "Linha " + t.getLine() + ": " +
                              "comentario nao fechado";
                }
                else if (padrao.equals("CADEIA_ERRO")) {
                    buffer += "Linha " + t.getLine() + ": " +
                              "cadeia literal nao fechada";
                }
                else {
                    buffer += "Linha " + t.getLine() + ": " + t.getText() + 
                              " - simbolo nao identificado";
                }
                erro = true;
            }
            else {
                System.out.println("ESTRANHO" + padrao + "   " + t.getText());
            }
            
            writer.print(buffer + "\n");
            buffer = "";
        }
        writer.close();
    }
}
