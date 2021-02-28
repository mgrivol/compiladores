package br.ufscar.dc.compiladores.trabalho1;

import java.io.IOException;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

public class Principal {
    public static void main(String args[]) throws IOException {
        CharStream cs = CharStreams.fromFileName(args[0]);
        LALexer lex = new LALexer(cs);
        
        System.out.println("Iniciando Analise Lexica: ");
        
        Token t = null;
        while ((t=lex.nextToken()).getType() != Token.EOF) {
            System.out.println("<" +
                               "\'" + t.getText() + "\'" +
                               "," + LALexer.VOCABULARY.getDisplayName(t.getType()) + 
                               ">");
        }
        
        System.out.println("Analise Lexica Terminada!");
    }
}
