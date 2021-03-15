package br.ufscar.dc.compiladores.trabalho3;

import br.ufscar.dc.compiladores.trabalho3.TabelaDeSimbolos.TipoLA;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.Token;

public class LASemanticoUtils {

    public static List<String> erros = new ArrayList<>();

    public static void adicionaErro(String msg) {
        erros.add(msg);
    }

    public static Escopos verificarDeclare(Escopos escopo, LAParser.VariavelContext ctx) {
        Token tipo = ctx.tipo().getStart();
        // se existe add
        for (var value : ctx.identificador()) {
            Token ident = value.getStart();
            List<TabelaDeSimbolos> lts = escopo.percorrerEscoposAninhados();
            for (TabelaDeSimbolos ts : lts) {
                if (ts.existe(ident.getText())) {
                    // se a variavel ja existe
                    LASemanticoUtils.adicionaErro("Linha " + ident.getLine() + ": identificador " + ident.getText() + " ja declarado anteriormente");
                } else {
                    escopo.obterEscopoAtual().adicionar(ident.getText(), getTipo(ctx.tipo()));
                }
            }
            System.out.println("declare=" + ident.getText() + ", " + tipo.getText());
        }
        return escopo;
    }
    
    public static TipoLA getTipo(LAParser.TipoContext ctx) {
        Token tipo = ctx.getStart();
        System.out.println("tipo=" + tipo.getText());
        TipoLA tipoVar = TipoLA.INVALIDO;
        switch (tipo.getText()) {
            case "inteiro":
                tipoVar = TipoLA.INTEIRO;
                break;
            case "real":
                tipoVar = TipoLA.REAL;
                break;
            case "literal":
                tipoVar = TipoLA.LITERAL;
                break;
        }
        return tipoVar;
    }
}
