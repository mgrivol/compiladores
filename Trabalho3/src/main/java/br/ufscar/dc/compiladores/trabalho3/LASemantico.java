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
        escopo.obterEscopoAtual().Imprime();
        return super.visitDecl_local_global(ctx);
    }

    @Override
    public Void visitDeclaracao_global(LAParser.Declaracao_globalContext ctx) {
//        System.out.println("DECLARACAO Global\n");
//        String decl = ctx.start.getText();
//        Variavel entrada = null;
//        escopo.criarNovoEscopo();
//        switch (decl) {
//            case "procedimento":
//                System.out.println("Procedimento");
//                break;
//            case "funcao":
//                System.out.println("Funcao");
//                entrada = LASemanticoUtils.verificaFuncao(escopo, ctx);
//                break;
//        }
//        escopo.abandonarEscopo();
//        if (entrada != null) {
//            escopo.obterEscopoAtual().adicionar(entrada);
//        } else {
//            System.out.println("entrada não valida");
//        }
//        escopo.obterEscopoAtual().Imprime();
        return super.visitDeclaracao_global(ctx);
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
        escopo.obterEscopoAtual().Imprime();
        for (var cmd : ctx.cmd()) {
            LASemanticoUtils.verificaCmd(escopo, cmd);
        }
        return super.visitCorpo(ctx);
    }

//    @Override
//    public Void visitDeclaracao_local(LAParser.Declaracao_localContext ctx) {
//        System.out.println("DECLARACAO Local");
//        List<Variavel> vars = LASemanticoUtils.verificaDeclLocal(escopo, ctx);
//        for (var v : vars) {
//            if (v != null) {
//                escopo.obterEscopoAtual().adicionar(v);
//            } else {
//                System.out.println("v nulo)!(@#*)");
//            }
//        }
////        escopo.obterEscopoAtual().Imprime();
//        return super.visitDeclaracao_local(ctx);
//    }

    @Override
    public Void visitCmd(LAParser.CmdContext ctx) {
        // LASemanticoUtils.verificaCmd(escopo, ctx);
        return super.visitCmd(ctx);
    }

    @Override
    public Void visitTipo(LAParser.TipoContext ctx) {
        TipoLA tipo = LASemanticoUtils.verificaTipo(escopo, ctx);
        if (tipo.tipoBasico == TipoLA.TipoBasico.INVALIDO) {
//            LASemanticoUtils.adicionaErro("Linha " + ctx.getStart().getLine() + ": tipo " + ctx.getStart().getText() + " nao declarado");
        }
        return super.visitTipo(ctx);
    }
}
