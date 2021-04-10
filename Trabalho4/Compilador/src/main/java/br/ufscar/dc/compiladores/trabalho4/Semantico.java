package br.ufscar.dc.compiladores.trabalho4;

import java.util.HashSet;
import java.util.Set;

public class Semantico extends TGENBaseVisitor<Void> {
    // realiza a análise semântica

    // tabela de símbolos
    Set<String> ts = new HashSet<>();
    
    @Override
    public Void visitPrograma(TGENParser.ProgramaContext ctx) {
        // visita o programa
        return super.visitPrograma(ctx);
    }

    @Override
    public Void visitInimigos(TGENParser.InimigosContext ctx) {
        System.out.println("encontrei inimigos:");
        for (var inimigo : ctx.inimigo()) {
            System.out.println("inimigo -> " + inimigo.getText());
            if (SemanticoUtils.verificaInimigo(inimigo, ts)) {
                // inimigo válido
                ts.add(inimigo.IDENT().getText());
            }
        }
        return null;
    }
    
    @Override
    public Void visitOndas(TGENParser.OndasContext ctx) {
        System.out.println("encontrei ondas: ");
        for (var o : ctx.onda()) {
            System.out.println("onda -> " + o.getText());
        }
        return null;
    }
}
