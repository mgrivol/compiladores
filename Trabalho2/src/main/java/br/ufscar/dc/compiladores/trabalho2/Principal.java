// Aluno: Marco Antônio Bernardi Grivol
// RA:    758619

package br.ufscar.dc.compiladores.trabalho2;

import java.io.IOException;
import java.io.PrintWriter;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;

public class Principal {
    public static void main(String[] args) throws IOException {
        try (PrintWriter writer = new PrintWriter(args[1])) {
            CharStream cs = CharStreams.fromFileName(args[0]);
            LALexer lexer = new LALexer(cs);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            LAParser parser = new LAParser(tokens);
            AvisaErro erro = new AvisaErro();
            parser.removeErrorListeners();
            parser.addErrorListener(erro);
            try {
                parser.programa();
            }
            catch (ParseCancellationException exception) {
                writer.println(exception.getMessage());
            }
            writer.println("Fim da compilacao");
            writer.close();
        }
    }
}
