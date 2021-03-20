package br.ufscar.dc.compiladores.trabalho3;

import br.ufscar.dc.compiladores.trabalho3.semanticoUtils.Escopos;
import br.ufscar.dc.compiladores.trabalho3.semanticoUtils.LASemanticoUtils;
import br.ufscar.dc.compiladores.trabalho3.semanticoUtils.TipoLA;
import br.ufscar.dc.compiladores.trabalho3.semanticoUtils.Variavel;
import java.util.ArrayList;
import java.util.List;

public class LASemantico extends LABaseVisitor<Void> {

    Escopos escopo;

    @Override
    public Void visitPrograma(LAParser.ProgramaContext ctx) {
        escopo = new Escopos();
        System.out.println("INICIO PROGRAMA");
        return super.visitPrograma(ctx);
    }
    
    @Override 
    public Void visitDecl_local_global(LAParser.Decl_local_globalContext ctx) {
        System.out.println("decl local global\n");
        List<Variavel> entrada = new ArrayList<>();
        if (ctx.declaracao_global() != null) {
            entrada.add(LASemanticoUtils.verificaDeclGlobal(escopo, ctx.declaracao_global()));
        } else {
            entrada = LASemanticoUtils.verificaDeclLocal(escopo, ctx.declaracao_local());
        }
        if (entrada.size() == 0) {
            System.out.println("entrada nula na declaracao global");
        } else {
            for (var v : entrada) {
                escopo.obterEscopoAtual().adicionar(v);
            }
        }
        return super.visitDecl_local_global(ctx);
    }
    
    @Override
    public Void visitCorpo(LAParser.CorpoContext ctx) {
        List<Variavel> varsDeclaradas = new ArrayList<>();
        for (var decl : ctx.declaracao_local()) {
            varsDeclaradas.addAll(LASemanticoUtils.verificaDeclLocal(escopo, decl));
            for (var v : varsDeclaradas) {
                escopo.obterEscopoAtual().adicionar(v);
            }
        }
        for (var cmd : ctx.cmd()) {
            LASemanticoUtils.verificaCmd(escopo, cmd);
        }
        return super.visitCorpo(ctx);
    }
}
