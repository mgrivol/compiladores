// Aluno: Marco Antônio Bernardi Grivol
// RA:    758619
package br.ufscar.dc.compiladores.trabalho4;

import br.ufscar.dc.compiladores.trabalho4.TGENParser.ProgramaContext;
import java.io.IOException;
import java.io.PrintWriter;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

public class Principal {

    public static void main(String[] args) throws IOException {
        // args[0] arquivo de entrada
        String inFile = args[0];
        // args[1] arquivo de saída
        String outFile = args[1];
        // args[2] modo: sintatico (+ léxico), 
        //               semantico (+ sintático), 
        //               gerador   (+ semântico)
        String modo = args[2];

        switch (modo) {
            case "sintatico":
                Sintatico(inFile, outFile);
                break;
            case "semantico":
                Semantico(inFile, outFile);
                break;
        }
    }

    public static void Sintatico(String inFile, String outFile) throws IOException {
        // leitura do arquivo
        CharStream cs = CharStreams.fromFileName(inFile);
        // geração de tokens
        TGENLexer lex = new TGENLexer(cs);

        Token token = null;
        PrintWriter writer = new PrintWriter(outFile);
        while ((token = lex.nextToken()).getType() != Token.EOF) {
            String padrao = TGENLexer.VOCABULARY.getSymbolicName(token.getType());
            writer.println("<" + padrao + ", " + token.getText() + ">");
        }
        writer.print("Fim da compilacao");
        writer.close();
    }

    public static void Semantico(String inFile, String outFIle) throws IOException {
        // leitura do arquivo
        CharStream cs = CharStreams.fromFileName(inFile);
        // geração de tokens
        TGENLexer lexer = new TGENLexer(cs);
        // "tokens" providencia acesso para todos os tokens obtidos por lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        // "parser" é responsável por analisar os tokens obtidos e determinar se existem erros
        TGENParser parser = new TGENParser(tokens);
        
        // arvore contém a árvore com todas as regras da linguagem
        ProgramaContext arvore = null;
        // tgens realiza a análise semântica
        Semantico tgens = new Semantico();
        
        arvore = parser.programa();
        tgens.visitPrograma(arvore);
        System.out.println("concluido.");
    }
}
