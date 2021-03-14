package br.ufscar.dc.compiladores.trabalho3;

import java.util.ArrayList;
import java.util.List;

public class LASemantico extends LABaseVisitor<Void> {

    Escopos escopo;
    List<String> erros;

    public List<String> getErros() {
        return this.erros;
    }

    @Override
    public Void visitPrograma(LAParser.ProgramaContext ctx) {
        escopo = new Escopos();
        erros = new ArrayList<>();
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
                String tipo = ctx.variavel().tipo().getText();
                // se existe add
                for (var ident : ctx.variavel().identificador()) {
                    String varNome = ident.getText();
                    escopo.obeterEscopoAtual().adicionar(varNome, tipo);
                    System.out.println("declare=" + varNome + ", " + tipo);
                }
                break;
        }
        return super.visitDeclaracao_local(ctx);
    }

    @Override
    public Void visitIdentificador(LAParser.IdentificadorContext ctx) {
        for (var ident : ctx.IDENT()) {
            System.out.println("ident=" + ident.getText());
            boolean existeIDENT = escopo.obeterEscopoAtual().existe(ident.getText());
            if (!existeIDENT) {
                System.out.println("nao existe " + ident.getText());
            }
        }
        return super.visitIdentificador(ctx);
    }

    @Override
    public Void visitVariavel(LAParser.VariavelContext ctx) {
        String tipo = ctx.tipo().getText();
        System.out.println("var=" + tipo);
        boolean existeTipo = escopo.obeterEscopoAtual().existeTipo(tipo);
        if (!existeTipo) {
            erros.add("Linha " + ctx.tipo().getStart().getLine() + ": tipo " + tipo + " nao declarado");
        }
        return super.visitVariavel(ctx);
    }
}
