package br.ufscar.dc.compiladores.trabalho3;

import br.ufscar.dc.compiladores.trabalho3.semanticoUtils.Escopos;
import br.ufscar.dc.compiladores.trabalho3.semanticoUtils.LASemanticoUtils;
import br.ufscar.dc.compiladores.trabalho3.semanticoUtils.TabelaDeSimbolos;
import br.ufscar.dc.compiladores.trabalho3.semanticoUtils.Variavel;
import java.util.ArrayList;
import java.util.List;

public class LASemantico extends LABaseVisitor<Void> {
    // realiza a análise semântica
    
    Escopos escopo;

    public Escopos getEscopos() {
        return escopo;
    }

    @Override
    public Void visitPrograma(LAParser.ProgramaContext ctx) {
        // visita o programa
        escopo = new Escopos();
        return super.visitPrograma(ctx);
    }

    @Override
    public Void visitDecl_local_global(LAParser.Decl_local_globalContext ctx) {
        // visita as declarações globais
        List<Variavel> entrada = new ArrayList<>();
        if (ctx.declaracao_global() != null) {
            // visita declaracao_global
            entrada.add(LASemanticoUtils.verificaDeclGlobal(escopo, ctx.declaracao_global()));
        } else {
            // visita declaracao_local
            entrada = LASemanticoUtils.verificaDeclLocal(escopo, ctx.declaracao_local());
        }
        for (var v : entrada) {
            // adiciona as variáveis encontradas no escopo
            escopo.obterEscopoAtual().adicionar(v);
        }
        return super.visitDecl_local_global(ctx);
    }

    @Override
    public Void visitCorpo(LAParser.CorpoContext ctx) {
        // visita o corpo
        List<Variavel> varsDeclaradas = new ArrayList<>();
        for (var decl : ctx.declaracao_local()) {
            // visita cada declaração e salva as variáveis
            varsDeclaradas.addAll(LASemanticoUtils.verificaDeclLocal(escopo, decl));
            for (var v : varsDeclaradas) {
                escopo.obterEscopoAtual().adicionar(v);
            }
        }
        for (var cmd : ctx.cmd()) {
            // visita cada comando
            LASemanticoUtils.verificaCmd(escopo.obterEscopoAtual(), cmd);
        }
        return super.visitCorpo(ctx);
    }
}
