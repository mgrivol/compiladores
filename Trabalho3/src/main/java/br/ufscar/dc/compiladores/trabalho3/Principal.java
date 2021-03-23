// Aluno: Marco Antônio Bernardi Grivol
// RA:    758619
package br.ufscar.dc.compiladores.trabalho3;

import br.ufscar.dc.compiladores.trabalho3.LAParser.ProgramaContext;
import br.ufscar.dc.compiladores.trabalho3.semanticoUtils.LASemanticoUtils;
import java.io.IOException;
import java.io.PrintWriter;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;

public class Principal {

    // classe Principal é responsável por realizar a análise semântica da linguagem LA
    public static void main(String[] args) throws IOException {
        /* para executar é necessário passar dois argumentos:
            (arquivo_entrada, arquivo_saida) com os respectivos caminhos.
                arquivo_entrada := arquivo que será realizada a análise léxica
                arquivo_saida := arquivo que a saída com os tokes será escrita
         */
        try ( PrintWriter writer = new PrintWriter(args[1])) {
            // "cs" é responsável por ler o arquivo_entrada
            CharStream cs = CharStreams.fromFileName(args[0]);
            // "lexer" é responsável por extrair a entrada do arquivo_entrada e gerar os tokens
            LALexer lexer = new LALexer(cs);
            // "tokens" providencia acesso para todos os tokens obtidos por lexer
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            // "parser" é responsável por analisar os tokens obtidos e determinar se existem erros
            LAParser parser = new LAParser(tokens);

            // uma classe foi criada para avaliar qualquer erro gerado durante a análise do parser
            AvisaErro erro = new AvisaErro();
            // qualquer erro anterior foi removido para evitar prints durante a execução
            parser.removeErrorListeners();
            // a classe é adicionada para receber erros do parser
            parser.addErrorListener(erro);

            // arvore contém a árvore com todas as regras da linguagem
            ProgramaContext arvore = null;
            // las realiza a análise semântica
            LASemantico las = new LASemantico();
            boolean codigoC = false;
            try {
                // caso existam erros, é retornada uma exceção e o programa encerra
                arvore = parser.programa();
                las.visitPrograma(arvore);
                // existem erros semânticos
                for (var msg : LASemanticoUtils.erros) {
                    writer.println(msg);
                }
                if (LASemanticoUtils.erros.isEmpty()) {
                    // não existem erros semânticos, gera o código em C
                    LAGeradorC geradorC = new LAGeradorC(las.getEscopos());
                    // geradorC visita a árvore
                    geradorC.visit(arvore);
                    // imprime o resultado no arquivo_saida
                    writer.print(geradorC.saida.toString());
                } else {
                    writer.println("Fim da compilacao");
                }
            } catch (ParseCancellationException exception) {
                // as informações sobre o erro são capturadas para emissão no arquivo_saida
                writer.println(exception.getMessage());
                writer.println("Fim da compilacao");
            }
            writer.close();
        }
    }
}
