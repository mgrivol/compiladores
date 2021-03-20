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
        saida = new StringBuilder();
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
            saida.append(String.format("%s %s;\n", ident.tipo.imprime(), ident.nome));
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
            String str = exp.getText().replaceAll("\"", "");
            if (tipoExp.tipoBasico == TipoLA.TipoBasico.LITERAL) {
                saida.append(String.format("printf(\"%s\");\n", str));
            } else {
                saida.append(String.format("printf(\"%s\", %s);\n", tipoExp.imprimePorcentagem(), str));
            }
        }
        saida.append("printf(\"\\n\");\n");
        return null;
    }
}
