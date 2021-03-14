// Aluno: Marco Antônio Bernardi Grivol
// RA:    758619
package br.ufscar.dc.compiladores.trabalho3;

import br.ufscar.dc.compiladores.trabalho3.LAParser.ProgramaContext;
import java.io.IOException;
import java.io.PrintWriter;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;

public class Principal {

    // classe Principal é responsável por realizar a análise sintática da linguagem LA
    public static void main(String[] args) throws IOException {
        try (PrintWriter writer = new PrintWriter(args[1])) {
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
            
            try {
                // caso existam erros, é retornada uma exceção e o programa encerra
                ProgramaContext arvore = parser.programa();
                LASemantico las = new LASemantico();
                las.visitPrograma(arvore);
                for (String msg : las.getErros()) {
                    writer.println(msg);
                }
            }
            catch (ParseCancellationException exception) {
                // as informações sobre o erro são capturadas para emissão no arquivo_saida
                writer.println(exception.getMessage());
            }
            // a compilação terminou com ou sem erros, é anunciado o fim e o arquivo_saida é fechado
            writer.println("Fim da compilacao");
            writer.close();
        }
    }
}
