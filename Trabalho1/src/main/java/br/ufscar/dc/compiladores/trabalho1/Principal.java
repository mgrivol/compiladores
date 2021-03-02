// Aluno: Marco Antônio Bernardi Grivol
// RA:    758619

package br.ufscar.dc.compiladores.trabalho1;

import java.io.IOException;
import java.io.PrintWriter;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

public class Principal {
    // classe Principal é responsável por realizar a análise léxica de um arquivo.
    public static void main(String args[]) throws IOException {
        /* para executar é necessário passar dois argumentos:
            (arquivo_entrada, arquivo_saida) com os respectivos caminhos.
                arquivo_entrada := arquivo que será realizada a análise léxica
                arquivo_saida := arquivo que a saída com os tokes será escrita
        */
        try ( PrintWriter writer = new PrintWriter(args[1])) {
            /* "cs" e "lex" são as váriaveis responsáveis por ler o arquivo_entrada
                e gerar os tokens */
            CharStream cs = CharStreams.fromFileName(args[0]);
            LALexer lex = new LALexer(cs);

            // "t" contém os dados do token sendo lido a cada iteração
            Token t = null;
            // "buffer" é uma cadeia de caracteres que será escrita no arquivo_saída
            String buffer = new String();
            // "erro" detecta erros sintáticos, se existirem, e encerra a execução do programa
            boolean erro = false;

            while (!erro && (t = lex.nextToken()).getType() != Token.EOF) {
                // enquanto não existirem erros E não chegar no final do arquivo_entrada
                // atribua o padrão/classe do token à variável "padrao"
                String padrao = LALexer.VOCABULARY.getSymbolicName(t.getType());

                /* Sequência de "ifs" responsáveis por identificar a qual classe
                   o token pertence */
                if (padrao.equals("PALAVRA_CHAVE") || padrao.equals("DELIM")
                        || padrao.equals("OPERADORES")) {
                    /* imprime o lexema (palavra chave, delimitador ou operador) atual
                        <'lexema', 'lexema'> */
                    buffer += "<\'" + t.getText() + "\',\'" + t.getText() + "\'>";
                } else if (padrao.equals("IDENT") || padrao.equals("CADEIA")
                        || padrao.equals("NUM_INT") || padrao.equals("NUM_REAL")) {
                    /* imprime o lexema e sua classe 
                       <'lexema', classe> */
                    buffer += "<\'" + t.getText() + "\'," + padrao + ">";
                } else {
                    // caso nenhum padrão seja conhecido, existe erro no arquivo_entrada
                    if (padrao.equals("ERRO_SIMBOLO")) {
                        /* se o erro for um símbolo desconhecido, exiba uma mensagem
                           de erro com o símbolo não reconhecido */
                        buffer += "Linha " + t.getLine() + ": " + t.getText()
                                + " - simbolo nao identificado";
                    } else if (padrao.equals("COMENTARIO_ERRO")) {
                        /* se o erro for um comentário não fechado, exiba uma mensagem
                           indicando o erro */
                        buffer += "Linha " + t.getLine() + ": comentario nao fechado";
                    } else if (padrao.equals("CADEIA_ERRO")) {
                        /* se o erro for uma cadeia não fechada, exiba uma mensagem
                           indicando o erro */
                        buffer += "Linha " + t.getLine() + ": cadeia literal nao fechada";
                    } else {
                        // se chegar nessa erro, algo desconhecido ocorreu
                        buffer += "Erro desconhecido!";
                    }
                    // indique a existência de erro para encerrar o programa
                    erro = true;
                }
                // imprima o conteúdo do "buffer" no arquivo_saida
                writer.println(buffer);
                // limpe o "buffer" para a próxima iteração
                buffer = "";
            }
        }
    }
}
