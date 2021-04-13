package br.ufscar.dc.compiladores.trabalho4;

import br.ufscar.dc.compiladores.trabalho4.SemanticoUtils.Inimigo;
import br.ufscar.dc.compiladores.trabalho4.SemanticoUtils.SemanticoUtils;
import br.ufscar.dc.compiladores.trabalho4.SemanticoUtils.TabelaDeSimbolos;
import java.util.HashSet;
import java.util.Set;

public class Semantico extends TGENBaseVisitor<Void> {
    // realiza a análise semântica

    TabelaDeSimbolos ts;
    
    public Semantico() {
        ts = new TabelaDeSimbolos();
    }
    
    public TabelaDeSimbolos getTabelaDeSimbolos() {
        return ts;
    }

    @Override
    public Void visitPrograma(TGENParser.ProgramaContext ctx) {
        // visita o programa
        return super.visitPrograma(ctx);
    }

    @Override
    public Void visitInimigos(TGENParser.InimigosContext ctx) {
        for (var i : ctx.inimigo()) {
            // verifique os parâmetros do inimigo
            Inimigo inimigo = SemanticoUtils.verificaInimigo(i);
            if (!ts.existe(inimigo.getNome())) {
                // inimigo não existe na tabela, adicione
                ts.adicionar(inimigo);
            } else {
                // inimigo já declarado
                SemanticoUtils.adicionaErro(String.format(
                        "Linha %d: inimigo %s ja declarado anteriormente",
                        i.start.getLine(), inimigo.getNome()
                ));
            }
        }
        return null;
    }
    
    @Override
    public Void visitOndas(TGENParser.OndasContext ctx) {
        // verifica as ondas
        for (var od : ctx.onda()) {
            // verifica cada onda
            SemanticoUtils.verificaOnda(od, ts);
        }
        return null;
    }
}
