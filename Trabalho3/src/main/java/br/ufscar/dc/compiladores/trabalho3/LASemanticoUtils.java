package br.ufscar.dc.compiladores.trabalho3;

import br.ufscar.dc.compiladores.trabalho3.TabelaDeSimbolos.TipoLA;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Pair;

public class LASemanticoUtils {

    public static List<String> erros = new ArrayList<>();

    public static void adicionaErro(String msg) {
        erros.add(msg);
    }
    /*
    public static Escopos verificarDeclare(Escopos escopo, LAParser.VariavelContext ctx) {
        Token tipo = ctx.tipo().getStart();
        // retorna uma lista contendo o <nome, tipo> da variavel
        List<Pair<String, TipoLA>> ret = new ArrayList<>();
        // para cada identificador da declaracao
        for (var value : ctx.identificador()) {
            Token ident = value.getStart();
            List<TabelaDeSimbolos> lts = escopo.percorrerEscoposAninhados();
            for (TabelaDeSimbolos ts : lts) {
                if (ts.existe(ident.getText())) {
                    // se a variavel ja existe
                    LASemanticoUtils.adicionaErro("Linha " + ident.getLine() + ": identificador " + ident.getText() + " ja declarado anteriormente");
                } else {
                    ret.add(new Pair<>(ident.getText(), verificaTipo(ctx.tipo())));
                    escopo.obterEscopoAtual().adicionar(ident.getText(), verificaTipo(ctx.tipo()));
                }
            }
            System.out.println("declare=" + ident.getText() + ", " + tipo.getText());
        } 
        return escopo; 
    } */
    
    public static Escopos verificaVariavel(Escopos escopo, LAParser.VariavelContext ctx) {
        // retorna variáveis válidas deste escopo
        TipoLA tipo = verificaTipo(ctx.tipo());
        for (var id : ctx.identificador()) {
            String ident = verificaExistenciaIdentificador(escopo.obterEscopoAtual(), id.getStart());
            if (ident != null) {
                escopo.obterEscopoAtual().adicionar(ident, tipo);
            }
        }
        return escopo;
    }
    
    public static Escopos verificaCmdAtribuicao(Escopos escopo, LAParser.CmdAtribuicaoContext ctx) {
        Token tokenLHS = ctx.identificador().getStart(); // token do lado esquerdo
        TipoLA tipoLHS = escopo.obterEscopoAtual().verificar(tokenLHS.getText()); // expressão do lado direito
        System.out.println("tipoLHS=" + tipoLHS);
        TipoLA tipoRHS = verificaExpressao(ctx.expressao());
        return escopo;
    }
    
    public static TipoLA verificaExpressao(LAParser.ExpressaoContext ctx) {
        List<TipoLA> tipoTermoLogico = new ArrayList<>();
        for (var termoLogico : ctx.termo_logico()) {
            tipoTermoLogico.add(verificaTermoLogico(termoLogico));
        }
        return TipoLA.INVALIDO;
    }
    
    public static TipoLA verificaTermoLogico(LAParser.Termo_logicoContext ctx) {
        List<TipoLA> tipoFatorLogico = new ArrayList<>();
        for (var fatorLogico : ctx.fator_logico()) {
            tipoFatorLogico.add(verificaFatorLogico(fatorLogico));
        }
        return TipoLA.INVALIDO;
    }
    
    public static TipoLA verificaFatorLogico(LAParser.Fator_logicoContext ctx) {
        return verificaParcelaLogica(ctx.parcela_logica());
    }
    
    public static TipoLA verificaParcelaLogica(LAParser.Parcela_logicaContext ctx) {
        if (ctx.exp_relacional() == null) {
            System.out.println("NULO");
        }
        return TipoLA.INVALIDO;
    }

    public static String verificaExistenciaIdentificador(TabelaDeSimbolos ts, Token ident) {
        if (ts.existe(ident.getText())) { 
            LASemanticoUtils.adicionaErro("Linha " + ident.getLine() + ": identificador " + ident.getText() + " ja declarado anteriormente");
            return null;
        } 
        return ident.getText();
    }
    
    public static TipoLA verificaTipo(LAParser.TipoContext ctx) {
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
