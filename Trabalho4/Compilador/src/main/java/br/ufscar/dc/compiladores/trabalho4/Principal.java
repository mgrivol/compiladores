// Aluno: Marco Antônio Bernardi Grivol
// RA:    758619
package br.ufscar.dc.compiladores.trabalho4;

import br.ufscar.dc.compiladores.trabalho4.SemanticoUtils.SemanticoUtils;
import br.ufscar.dc.compiladores.trabalho4.TGENParser.ProgramaContext;
import java.io.IOException;
import java.io.PrintWriter;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.ParseCancellationException;

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
            case "gerador":
                Gerador(inFile, outFile);
                break;
            default:
                System.out.println("ERRO -" + modo + "- MODO DESCONHECIDO!");
                break;
        }
    }

    public static void Sintatico(String inFile, String outFile) throws IOException {
        PrintWriter writer = new PrintWriter(outFile);
        // leitura do arquivo
        CharStream cs = CharStreams.fromFileName(inFile);
        // geração de tokens
        TGENLexer lex = new TGENLexer(cs);
        // "tokens" providencia acesso para todos os tokens obtidos por lex
        CommonTokenStream tokens = new CommonTokenStream(lex);
        // "parser" é responsável por analisar os tokens obtidos e determinar se existem erros
        TGENParser parser = new TGENParser(tokens);

        // uma classe foi criada para avaliar qualquer erro gerado durante a análise do parser
        AvisaErro erro = new AvisaErro();
        // qualquer erro anterior foi removido para evitar prints durante a execução
        parser.removeErrorListeners();
        // a classe é adicionada para receber erros do parser
        parser.addErrorListener(erro);

        try {
            // caso existam erros, é retornada uma exceção e o programa encerra
            parser.programa();
        } catch (ParseCancellationException exception) {
            // as informações sobre o erro são capturadas para emissão no arquivo_saida
            writer.println(exception.getMessage());
        }

        writer.print("Fim da compilacao");
        writer.close();
    }

    public static void Semantico(String inFile, String outFile) throws IOException {
        PrintWriter writer = new PrintWriter(outFile);
        // "cs" é responsável por ler o arquivo_entrada
        CharStream cs = CharStreams.fromFileName(inFile);
        // "lexer" é responsável por extrair a entrada do arquivo_entrada e gerar os tokens
        TGENLexer lexer = new TGENLexer(cs);
        // "tokens" providencia acesso para todos os tokens obtidos por lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        // "parser" é responsável por analisar os tokens obtidos e determinar se existem erros
        TGENParser parser = new TGENParser(tokens);
        // uma classe foi criada para avaliar qualquer erro gerado durante a análise do parser
        AvisaErro erro = new AvisaErro();
        // qualquer erro anterior foi removido para evitar prints durante a execução
        parser.removeErrorListeners();
        // a classe é adicionada para receber erros do parser
        parser.addErrorListener(erro);
        // arvore contém a árvore com todas as regras da linguagem
        ProgramaContext arvore = null;
        // sem realiza a análise semântica
        Semantico sem = new Semantico();
        boolean codigoC = false;
        try {
            // caso existam erros, é retornada uma exceção e o programa encerra
            arvore = parser.programa();
            sem.visitPrograma(arvore);
            // existem erros semânticos
            for (var msg : SemanticoUtils.erros) {
                writer.println(msg);
            }
        } catch (ParseCancellationException exception) {
            // as informações sobre o erro são capturadas para emissão no arquivo_saida
            writer.println(exception.getMessage());
        }
        writer.print("Fim da compilacao");
        writer.close();
    }
    
    public static void Gerador(String inFile, String outFile) throws IOException {
        PrintWriter writer = new PrintWriter(outFile);
        // "cs" é responsável por ler o arquivo_entrada
        CharStream cs = CharStreams.fromFileName(inFile);
        // "lexer" é responsável por extrair a entrada do arquivo_entrada e gerar os tokens
        TGENLexer lexer = new TGENLexer(cs);
        // "tokens" providencia acesso para todos os tokens obtidos por lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        // "parser" é responsável por analisar os tokens obtidos e determinar se existem erros
        TGENParser parser = new TGENParser(tokens);
        // uma classe foi criada para avaliar qualquer erro gerado durante a análise do parser
        AvisaErro erro = new AvisaErro();
        // qualquer erro anterior foi removido para evitar prints durante a execução
        parser.removeErrorListeners();
        // a classe é adicionada para receber erros do parser
        parser.addErrorListener(erro);
        // arvore contém a árvore com todas as regras da linguagem
        ProgramaContext arvore = null;
        // sem realiza a análise semântica
        Semantico sem = new Semantico();
        boolean codigoC = false;
        try {
            // caso existam erros, é retornada uma exceção e o programa encerra
            arvore = parser.programa();
            sem.visitPrograma(arvore);
            if (SemanticoUtils.erros.isEmpty()) {
                // gera código
                GeradorCS gerador = new GeradorCS(sem.getTabelaDeSimbolos());
                gerador.visit(arvore);
                writer.print(gerador.saida.toString());
            }
            else {
                // existem erros semânticos
                for (var msg : SemanticoUtils.erros) {
                    writer.println(msg);
                }
                writer.print("Fim da compilacao");
            }
        } catch (ParseCancellationException exception) {
            // as informações sobre o erro são capturadas para emissão no arquivo_saida
            writer.println(exception.getMessage());
            writer.print("Fim da compilacao");
        }
        writer.close();
    }
}
