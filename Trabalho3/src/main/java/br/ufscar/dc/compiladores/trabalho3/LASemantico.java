package br.ufscar.dc.compiladores.trabalho3;

import br.ufscar.dc.compiladores.trabalho3.TabelaDeSimbolos.TipoLA;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Pair;

public class LASemantico extends LABaseVisitor<Void> {

    Escopos escopo;

    @Override
    public Void visitPrograma(LAParser.ProgramaContext ctx) {
        escopo = new Escopos();
        return super.visitPrograma(ctx);
    }

    @Override
    public Void visitDeclaracao_global(LAParser.Declaracao_globalContext ctx) {
        System.out.println("decl global\n");
        return super.visitDeclaracao_global(ctx);
    }

    @Override
    public Void visitDeclaracao_local(LAParser.Declaracao_localContext ctx) {
        String decl = ctx.getStart().getText();
        switch (decl) {
            case ("declare"):
                escopo = LASemanticoUtils.verificaVariavel(escopo, ctx.variavel());
                break;
        }
        return super.visitDeclaracao_local(ctx);
    }

    @Override
    public Void visitCmdAtribuicao(LAParser.CmdAtribuicaoContext ctx) {
        String exp = ctx.expressao().getText();
        String ident = ctx.identificador().getText();
        System.out.println("cmd = " + ident + " <- " + exp);
        return super.visitCmdAtribuicao(ctx);
    }

    @Override
    public Void visitIdentificador(LAParser.IdentificadorContext ctx) {
        for (var value : ctx.IDENT()) {
            Token ident = value.getSymbol();
            System.out.println("ident=" + ident.getText());
            boolean existeIDENT = escopo.obterEscopoAtual().existe(ident.getText());
            if (!existeIDENT) {
                LASemanticoUtils.adicionaErro("Linha " + ident.getLine() + ": identificador " + ident.getText() + " nao declarado");
            }
        }
        return super.visitIdentificador(ctx);
    }

    @Override
    public Void visitTipo(LAParser.TipoContext ctx) {
        TipoLA tipoVar = LASemanticoUtils.verificaTipo(ctx);
        if (tipoVar == TipoLA.INVALIDO) {
            LASemanticoUtils.adicionaErro("Linha " + ctx.getStart().getLine() + ": tipo " + ctx.getStart().getText() + " nao declarado");
        }
        return super.visitTipo(ctx);
    }
}
