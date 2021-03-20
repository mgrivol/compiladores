package br.ufscar.dc.compiladores.trabalho3;

import br.ufscar.dc.compiladores.trabalho3.semanticoUtils.Escopos;
import br.ufscar.dc.compiladores.trabalho3.semanticoUtils.LASemanticoUtils;
import br.ufscar.dc.compiladores.trabalho3.semanticoUtils.TabelaDeSimbolos;
import br.ufscar.dc.compiladores.trabalho3.semanticoUtils.TipoLA;
import br.ufscar.dc.compiladores.trabalho3.semanticoUtils.Variavel;

public class LAGeradorC extends LABaseVisitor<Void> {

    StringBuilder saida;
    TabelaDeSimbolos tabela;

    public LAGeradorC(TabelaDeSimbolos ts) {
        this.saida = new StringBuilder();
        this.tabela = ts;
        tabela.Imprime();
    }

    @Override
    public Void visitPrograma(LAParser.ProgramaContext ctx) {
        saida.append("#include <stdio.h>\n");
        saida.append("#include <stdlib.h>\n\n");

        if (ctx.declaracoes().isEmpty()) {
            System.out.println("visitando declaracoes ->" + ctx.declaracoes().getText());
        }

        visitCorpo(ctx.corpo());
        return null;
    }

    @Override
    public Void visitCorpo(LAParser.CorpoContext ctx) {
        saida.append("int main() {\n");

        for (var decl : ctx.declaracao_local()) {
            // para cada declaração local
            visitDeclaracao_local(decl);
        }

        for (var cmd : ctx.cmd()) {
            visitCmd(cmd);
        }

        saida.append("return 0;\n}\n");
        return null;
    }

    @Override
    public Void visitDeclaracao_local(LAParser.Declaracao_localContext ctx) {
        String tipoDeclaracao = ctx.getChild(0).getText();
        switch (tipoDeclaracao) {
            case "declare":
                visitVariavel(ctx.variavel());
                break;
            case "constante":
                System.out.println("constante");
                break;
            case "tipo":
                System.out.println("tipo");
                break;
        }
        return null;
    }

    @Override
    public Void visitVariavel(LAParser.VariavelContext ctx) {
        for (var id : ctx.identificador()) {
            Variavel ident = tabela.getVariavel(id.getText());
            if (ident.tipo.tipoBasico == TipoLA.TipoBasico.LITERAL) {
                saida.append(String.format("%s %s[100];\n", ident.tipo.imprime(), ident.nome));
            } else {
                saida.append(String.format("%s %s;\n", ident.tipo.imprime(), ident.nome));
            }
        }
        return null;
    }
    // --- Comandos ---
    @Override 
    public Void visitCmd(LAParser.CmdContext ctx) {
        // visita os comandos
        if (ctx.cmdLeia() != null) {
            // existe comando leia
            visitCmdLeia(ctx.cmdLeia());
        } else if (ctx.cmdEscreva() != null) {
            // existe comando escreva
            visitCmdEscreva(ctx.cmdEscreva());
        } else if (ctx.cmdSe() != null) {
            // existe comando se
            visitCmdSe(ctx.cmdSe());
        } else if (ctx.cmdAtribuicao() != null) {
            // existe comando atribuição
            visitCmdAtribuicao(ctx.cmdAtribuicao());
        }
        return null;
    }
    
    @Override
    public Void visitCmdLeia(LAParser.CmdLeiaContext ctx) {
        Variavel ident = tabela.getVariavel(ctx.identificador(0).getText());
        saida.append(String.format("scanf(\"%s\", &%s);\n", ident.tipo.imprimePorcentagem(), ident.nome));
        return null;
    }

    @Override
    public Void visitCmdEscreva(LAParser.CmdEscrevaContext ctx) {
        for (var exp : ctx.expressao()) {
            TipoLA tipoExp = LASemanticoUtils.verificaExpressao(tabela, exp);
            saida.append(String.format("printf(\"%s\", ", tipoExp.imprimePorcentagem()));
            visitExpressao(exp);
            saida.append(");\n");
        }
        return null;
    }
    
    @Override 
    public Void visitCmdAtribuicao(LAParser.CmdAtribuicaoContext ctx) {
        if (ctx.getChild(0).equals("^")) {
            saida.append("*");
        }
        visitIdentificador(ctx.identificador());
        saida.append(" = ");
        visitExpressao(ctx.expressao());
        saida.append(";\n");
        return null;
    }
    
    @Override
    public Void visitCmdSe(LAParser.CmdSeContext ctx) {
        saida.append("if (");
        visitExpressao(ctx.expressao());
        saida.append(") {\n");
        for (var cmd : ctx.cmd1) {
            visitCmd(cmd);
        }
        saida.append("}\n");
        if (ctx.cmd2.size() > 0) {
            saida.append("else {\n");
            for (var cmd : ctx.cmd2) {
                visitCmd(cmd);
            }
            saida.append("}\n");
        }
        return null;
    }
    // -- Fim Comandos ---
    
    // --- Expressão ---
    @Override
    public Void visitExpressao(LAParser.ExpressaoContext ctx) {
        visitTermo_logico(ctx.termo_logico(0));
        for (int i = 0; i < ctx.op_logico_1().size(); i++) {
            saida.append(" || ");
            visitTermo_logico(ctx.termo_logico(i + 1));
        }
        return null;
    }
    
    @Override
    public Void visitTermo_logico(LAParser.Termo_logicoContext ctx) {
        visitFator_logico(ctx.fator_logico(0));
        for (int i = 0; i < ctx.op_logico_2().size(); i++) {
            saida.append(" && ");
            visitFator_logico(ctx.fator_logico(i + 1));
        }
        return null;
    }
    
    @Override
    public Void visitFator_logico(LAParser.Fator_logicoContext ctx) {
        if (ctx.getChild(0).equals("nao")) {
            saida.append("!");
        }
        visitParcela_logica(ctx.parcela_logica());
        return null;
    }
    
    @Override
    public Void visitParcela_logica(LAParser.Parcela_logicaContext ctx) {
        if (ctx.exp_relacional() != null) {
            visitExp_relacional(ctx.exp_relacional());
            return null;
        }
        if (ctx.getText().equals("verdadeiro")) {
            saida.append(" true ");
        } else {
            saida.append(" false ");
        }
        return null;
    }
    
    @Override
    public Void visitExp_relacional(LAParser.Exp_relacionalContext ctx) {
        visitExp_aritmetica(ctx.exp_aritmetica(0));
        if (ctx.op_relacional() != null) {
            visitOp_relacional(ctx.op_relacional());
            visitExp_aritmetica(ctx.exp_aritmetica(1));
        }
        return null;
    }
    
    @Override
    public Void visitOp_relacional(LAParser.Op_relacionalContext ctx) {
        switch (ctx.getText()) {
            case "=":
                saida.append(" == ");
                break;
            case "<>":
                saida.append(" != ");
                break;
            default:
                saida.append(ctx.getText());
                break;
        }
        return null;
    }
    
    @Override
    public Void visitExp_aritmetica(LAParser.Exp_aritmeticaContext ctx) {
        visitTermo(ctx.termo(0));
        for (int i = 0; i < ctx.op1().size(); i++) {
            saida.append(" " + ctx.op1(i).getText() + " ");
            visitTermo(ctx.termo(i + 1));
        }
        return null;
    }
    
    @Override
    public Void visitTermo(LAParser.TermoContext ctx) {
        visitFator(ctx.fator(0));
        for (int i = 0; i < ctx.op2().size(); i++) {
            saida.append(" " + ctx.op2(i).getText() + " ");
            visitFator(ctx.fator(i + 1));
        }
        return null;
    }
    
    @Override
    public Void visitFator(LAParser.FatorContext ctx) {
        visitParcela(ctx.parcela(0));
        for (int i = 0; i < ctx.op3().size(); i++) {
            saida.append(" " + ctx.op3(i).getText() + " ");
            visitParcela(ctx.parcela(i));
        }
        return null;
    }
    
    @Override
    public Void visitParcela(LAParser.ParcelaContext ctx) {
        if (ctx.parcela_unario() != null) {
            if (ctx.op_unario() != null) {
                saida.append(" " + ctx.op_unario().getText());
            }
            visitParcela_unario(ctx.parcela_unario());
        } else {
            visitParcela_nao_unario(ctx.parcela_nao_unario());
        }
        return null;
    }
    
    @Override
    public Void visitParcela_unario(LAParser.Parcela_unarioContext ctx) {
        if (ctx.identificador() != null) {
            if (ctx.getChild(0).equals("^")) {
                saida.append("*");
            }
            visitIdentificador(ctx.identificador());
        } else if (ctx.IDENT() != null) {
            saida.append("PARCELA UNARIO COM IDENTIFICADOR");
        } else if (ctx.NUM_INT() != null) {
            saida.append(ctx.NUM_INT().getText());
        } else if (ctx.NUM_REAL() != null) {
            saida.append(ctx.NUM_REAL().getText());
        } else {
            saida.append(" (");
            for (var exp : ctx.expressao()) {
                visitExpressao(exp);
            }
            saida.append(") ");
        }
        return null;
    }
    
    @Override
    public Void visitParcela_nao_unario(LAParser.Parcela_nao_unarioContext ctx) {
        if (ctx.identificador() != null) {
            if (ctx.getChild(0).equals("&")) {
                saida.append("&");
            }
            visitIdentificador(ctx.identificador());
        } else {
            saida.append(ctx.CADEIA().getText());
        }
        return null;
    }
    // Fim da expressão
    
    @Override
    public Void visitIdentificador(LAParser.IdentificadorContext ctx) {
        saida.append(ctx.IDENT(0).getText());
        for (int i = 1; i < ctx.IDENT().size(); i++) {
            saida.append(".");
            saida.append(ctx.IDENT(i).getText());
        }
        if (ctx.dimensao().getChild(0) != null) {
            visitDimensao(ctx.dimensao());
        }
        return null;
    }
    
    @Override
    public Void visitDimensao(LAParser.DimensaoContext ctx) {
        saida.append("[");
        for (var exp : ctx.exp_aritmetica()) {
            visitExp_aritmetica(exp);
        }
        saida.append("]");
        return null;
    }
}
