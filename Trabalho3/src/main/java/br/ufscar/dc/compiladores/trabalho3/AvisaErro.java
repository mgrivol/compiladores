// Aluno: Marco Antônio Bernardi Grivol
// RA:    758619

package br.ufscar.dc.compiladores.trabalho3;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.ParseCancellationException;

// Adaptado de: https://stackoverflow.com/questions/18132078/handling-errors-in-antlr4
public class AvisaErro extends BaseErrorListener {
    // classe responsável por analisar os erros gerados durante a análise sintática do parser
    
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
            int charPositionInLine, String msg, RecognitionException e) 
                throws ParseCancellationException{
        // substitui o método padrão do ANTLR para emissão dos erros
        
        // "t" armazena o último token reconhecido corretamente
        Token t = (Token) offendingSymbol;
        // "padrao" armazena a classe do token atual
        String padrao = LALexer.VOCABULARY.getSymbolicName(t.getType());
        
        // "buffer" armazena a mensagem de erro
        String buffer = "";
        if (padrao != null && padrao.equals("COMENTARIO_ERRO")) {
            // se o erro for um comentario não fechado
            buffer = "Linha " + t.getLine() + ": comentario nao fechado";
        }
        else if (padrao != null && padrao.equals("CADEIA_ERRO")) {
            // se o erro for uma cadeia literal não fechada
            buffer = "Linha " + t.getLine() + ": cadeia literal nao fechada";
        }
        else if (padrao != null && padrao.equals("SIMBOLO_ERRO")) {
            // se o erro for um símbolo desconhecido
            buffer = "Linha " + t.getLine() + ": " + t.getText() + " - simbolo nao identificado";
        }
        else if (t.getType() == Token.EOF) {
            // se o erro for no final do arquivo
            buffer = "Linha " + t.getLine() + ": erro sintatico proximo a EOF";
        }
        else {
            // se o erro não for nenhum dos anteriores, define como erro sintático genérico
            buffer = "Linha " + t.getLine() + ": erro sintatico proximo a " + t.getText();
        }
        // emite uma exceção com a mensagem do buffer
        throw new ParseCancellationException(buffer);
    }
}
