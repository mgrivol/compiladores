package br.ufscar.dc.compiladores.trabalho1;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

public class Principal {
    public static void main(String args[]) throws IOException {
        PrintWriter writer = new PrintWriter(args[1]);
        
        CharStream cs = CharStreams.fromFileName(args[0]);
        LALexer lex = new LALexer(cs);
        
        Token t = null;
        String buffer = new String();
        boolean erro = false;
        
        while (!erro && (t=lex.nextToken()).getType() != Token.EOF) {
            String padrao = LALexer.VOCABULARY.getSymbolicName(t.getType());
            
            if (padrao.equals("PALAVRA_CHAVE") || padrao.equals("DELIM") ||
                padrao.equals("OPERADORES")) {
                
                buffer += "<\'" + t.getText() + "\',\'" + t.getText() + "\'>";
            }
            else if (padrao.equals("IDENT")   || padrao.equals("CADEIA") ||
                     padrao.equals("NUM_INT") || padrao.equals("NUM_REAL")) {
                
                buffer += "<\'" + t.getText() + "\'," +  padrao + ">";
            }
            else {
                if (padrao.equals("ERRO_SIMBOLO")) {
                    buffer += "Linha " + t.getLine() + ": " + t.getText() +
                              " - simbolo nao identificado";
                }
                else if (padrao.equals("COMENTARIO_ERRO")) {
                    buffer += "Linha " + t.getLine() + ": comentario nao fechado";
                }
                else if (padrao.equals("CADEIA_ERRO")) {
                    buffer += "Linha " + t.getLine() + ": cadeia literal nao fechada";
                }
                else {
                    buffer += "Erro desconhecido!";
                }
                erro = true;
            }
            writer.println(buffer);
            buffer = "";
        }
        writer.close();
    }
}
