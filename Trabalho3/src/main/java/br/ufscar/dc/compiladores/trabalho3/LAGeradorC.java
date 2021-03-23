package br.ufscar.dc.compiladores.trabalho3;

import br.ufscar.dc.compiladores.trabalho3.semanticoUtils.Escopos;
import br.ufscar.dc.compiladores.trabalho3.semanticoUtils.LASemanticoUtils;
import br.ufscar.dc.compiladores.trabalho3.semanticoUtils.TabelaDeSimbolos;
import br.ufscar.dc.compiladores.trabalho3.semanticoUtils.TipoLA;
import br.ufscar.dc.compiladores.trabalho3.semanticoUtils.Variavel;
import java.util.List;

public class LAGeradorC extends LABaseVisitor<Void> {
    // gera o código em C de um arquivo em Linguagem Algoritmica
    
    StringBuilder saida;
    Escopos escopo;
    Variavel aux;

    public LAGeradorC(Escopos escopo) {
        this.saida = new StringBuilder();
        this.escopo = escopo;
    }

    @Override
    public Void visitPrograma(LAParser.ProgramaContext ctx) {
        // visita o programa
        saida.append("#include <stdio.h>\n");
        saida.append("#include <stdlib.h>\n");
        saida.append("#include <string.h>\n\n");

        if (!ctx.declaracoes().isEmpty()) {
            visitDeclaracoes(ctx.declaracoes());
        }

        visitCorpo(ctx.corpo());
        return null;
    }
    
    // --- Declarações Globais antes da main ---
    @Override
    public Void visitDeclaracoes(LAParser.DeclaracoesContext ctx) {
        // visita as declaracoes
        for (var decl : ctx.decl_local_global()) {
            // para cada decl_local_global
            visitDecl_local_global(decl);
        }
        return null;
    }
    
    @Override
    public Void visitDecl_local_global(LAParser.Decl_local_globalContext ctx) {
        // visita a decl_local_global
        if (ctx.declaracao_local() != null) {
            // existe uma declaração local
            visitDeclaracao_local(ctx.declaracao_local());
        } else {
            // existe uma declaração global
            visitDeclaracao_global(ctx.declaracao_global());
        }
        return null;
    }
    
    @Override
    public Void visitDeclaracao_global(LAParser.Declaracao_globalContext ctx) {
        // visita a declaracao_global
        switch (ctx.getChild(0).getText()) {
            case "procedimento":
                visitaProcedimento(ctx);
                break;
            case "funcao":
                visitaFuncao(ctx);
                break;
        }
        return null;
    }
    
    public void visitaProcedimento(LAParser.Declaracao_globalContext ctx) {
        // gera o código de um procedimento
        Variavel proc = escopo.obterEscopoAtual().getVariavel(ctx.IDENT().getText());
        saida.append("void " + proc.nome + "(");
        List<Variavel> parametros = proc.getProcedimento().getParametros();
        if (parametros.get(0).tipo.tipoBasico == TipoLA.TipoBasico.LITERAL) {
            saida.append(parametros.get(0).tipo.imprime() + " *" + parametros.get(0).nome);
        } else {
            saida.append(parametros.get(0).tipo.imprime() + " " + parametros.get(0).nome);
        }
        for (int i = 1; i < parametros.size(); i++) {
            // existe mais de um parâmetro
            saida.append(", ");
            if (parametros.get(i).tipo.tipoBasico == TipoLA.TipoBasico.LITERAL) {
                saida.append(parametros.get(i).tipo.imprime() + " *" + parametros.get(0).nome);
            } else {
                geraVariavel(parametros.get(i));
            }
        }
        saida.append(") {\n");
        for (var decl : ctx.declaracao_local()) {
            visitDeclaracao_local(decl);
        }
        // atualiza o escopo local com os parametros e declaracoes para serem reconhecidos nos comandos
        escopo.criarNovoEscopo();
        for (var v : parametros) {
            // atualiza o escopo com os parametros
            escopo.obterEscopoAtual().adicionar(v);
        }
        for (var v : proc.getProcedimento().getVariaveisLocais()) {
            // visita cada comando existente
            escopo.obterEscopoAtual().adicionar(v);
        }
        for (var cmd : ctx.cmd()) {
            // visita cada comando existente
            visitCmd(cmd);
        }
        escopo.abandonarEscopo();
        saida.append("}\n");
        return;
    } 
    
    public void visitaFuncao(LAParser.Declaracao_globalContext ctx) {
        // gera o códico de uma função
        Variavel funcao = escopo.obterEscopoAtual().getVariavel(ctx.IDENT().getText());
        saida.append(funcao.getFuncao().getTipoRetorno().imprime() + " " + funcao.nome + "(");
        List<Variavel> parametros = funcao.getFuncao().getParametros();
        if (parametros.get(0).tipo.tipoBasico == TipoLA.TipoBasico.LITERAL) {
            saida.append(parametros.get(0).tipo.imprime() + " *" + parametros.get(0).nome);
        } else {
            saida.append(parametros.get(0).tipo.imprime() + " " + parametros.get(0).nome);
        }
        for (int i = 1; i < parametros.size(); i++) {
            // existe mais de um parâmetro na função
            saida.append(", ");
            if (parametros.get(i).tipo.tipoBasico == TipoLA.TipoBasico.LITERAL) {
                saida.append(parametros.get(i).tipo.imprime() + " *" + parametros.get(i).nome);
            } else {
                geraVariavel(parametros.get(i));
            }
        }
        saida.append(") {\n");
        for (var decl : ctx.declaracao_local()) {
            visitDeclaracao_local(decl);
        }
        // atualiza o escopo local com os parametros e declaracoes para serem reconhecidos nos comandos
        escopo.criarNovoEscopo();
        for (var v : parametros) {
            // atualiza o escopo com os parametros
            escopo.obterEscopoAtual().adicionar(v);
        }
        for (var v : funcao.getFuncao().getVariaveisLocais()) {
            // atualiza o escopo com as variaveis locais
            escopo.obterEscopoAtual().adicionar(v);
        }
        for (var cmd : ctx.cmd()) {
            // visita cada comando existente
            visitCmd(cmd);
        }
        escopo.abandonarEscopo();
        saida.append("}\n");
        return;
    }
    
    @Override
    public Void visitValor_constante(LAParser.Valor_constanteContext ctx) {
        // gera o código de uma constante
        if (ctx.CADEIA() != null) {
            // se o valor constante for uma cadeia de caracteres
            saida.append("\"" + ctx.CADEIA().getText() + "\"\n");
        } else if (ctx.NUM_INT() != null) {
            // se o valor constante for um número inteiro
            saida.append(Integer.parseInt(ctx.NUM_INT().getText()) + "\n");
        } else if (ctx.NUM_REAL() != null) {
            // se o valor constante for um número real
            saida.append(Float.parseFloat(ctx.NUM_REAL().getText()) + "\n");
        } else if (ctx.getChild(0).getText().equals("verdadeiro")) {
            // se o valor constante for o valor lógico verdadeiro
            saida.append("1\n");
        } else {
            // senão, o valor constante é o valor lógico falso
            saida.append("0\n");
        }
        return null;
    }
    // --- Fim Declarações Globais antes da main ---

    // --- Corpo - main ---
    @Override
    public Void visitCorpo(LAParser.CorpoContext ctx) {
        // gera o código do corpo
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
        // gera o código de uma declaracao_local
        String tipoDeclaracao = ctx.getChild(0).getText();
        switch (tipoDeclaracao) {
            case "declare":
                visitVariavel(ctx.variavel());
                break;
            case "constante":
                saida.append("#define " + ctx.IDENT().getText() + " ");
                visitValor_constante(ctx.valor_constante());
                break;
            case "tipo":
                saida.append("typedef struct {\n");
                this.aux = escopo.obterEscopoAtual().getVariavel(ctx.IDENT().getText());
                visitTipo(ctx.tipo());
                saida.append("} " + ctx.IDENT().getText() + ";\n");
                break;
        }
        return null;
    }
    
    // --- Variáveis e Tipos ---
    @Override
    public Void visitVariavel(LAParser.VariavelContext ctx) {
        // avalia uma variável
        for (var id : ctx.identificador()) {
            System.out.println(id.getText());
            // nome armazena a variavel sem considerar a dimensao
            String nome = id.IDENT(0).getText();
            for (int i = 1; i < id.IDENT().size(); i++) {
                nome += "." + id.IDENT(i).getText();
            }
            Variavel ident = escopo.obterEscopoAtual().getVariavel(nome);
            geraVariavel(ident);
            if (!id.dimensao().exp_aritmetica().isEmpty()){
                // se existe algum valor dentro da dimensão
                visitDimensao(id.dimensao());
            }
            saida.append(";\n");
        }
        return null;
    }
    
    public void geraVariavel(Variavel v) {
        // gera o código de uma variável
        if (v.tipo != null && v.tipo.tipoBasico != null) {
            switch (v.tipo.tipoBasico) {
                case LITERAL:
                    // literal
                    saida.append(String.format("%s %s[100]", v.tipo.imprime(), v.nome));
                    break;
                case PONTEIRO:
                    // ponteiro
                    saida.append(String.format("%s *%s", v.getTipoPonteiroAninhado().imprime(), v.nome));
                    break;
                case REGISTRO:
                    // registro
                    saida.append("struct {\n");
                    for (var vReg : v.getRegistro().getTodasVariaveis()) {
                        geraVariavel(vReg);
                        saida.append(";\n");
                    }
                    saida.append("} " + v.nome);
                    break;
                case INTEIRO:
                    // inteiro
                    saida.append(String.format("%s %s", v.tipo.imprime(), v.nome));
                    break;
                case REAL:
                    // real
                    saida.append(String.format("%s %s", v.tipo.imprime(), v.nome));
                    break;
            }
        } else {
            // tipo criado
            saida.append(String.format("%s %s", v.tipo.tipoCriado, v.nome));
        }
    }
    
    @Override
    public Void visitTipo(LAParser.TipoContext ctx) {
        // visita o tipo
        if (ctx.registro() != null) {
            // existe registro
            visitRegistro(ctx.registro());
        } else {
            // existe tipo_estendido
        }
        return null;
    }
    
    @Override
    public Void visitRegistro(LAParser.RegistroContext ctx) {
        // gera o código de um registro
        List<Variavel> varsNoRegistro = aux.getRegistro().getTodasVariaveis();
        for (var v : aux.getRegistro().getTodasVariaveis()) {
            geraVariavel(v);
            saida.append(";\n");
        }
        return null;
    }
    // --- Fim Variáveis e Tipos ---
    
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
        } else if (ctx.cmdCaso() != null) {
            // existe comando caso
            visitCmdCaso(ctx.cmdCaso());
        } else if (ctx.cmdPara() != null) {
            // existe comando para
            visitCmdPara(ctx.cmdPara());
        } else if (ctx.cmdEnquanto() != null) {
            // existe comando enquanto
            visitCmdEnquanto(ctx.cmdEnquanto());
        } else if (ctx.cmdFaca() != null) {
            // existe comando faca
            visitCmdFaca(ctx.cmdFaca());
        } else if (ctx.cmdChamada() != null) {
            // existe comando chamda
            visitCmdChamada(ctx.cmdChamada());
        } else if (ctx.cmdRetorne() != null) {
            // existe comando retorne
            visitCmdRetorne(ctx.cmdRetorne());
        }
        return null;
    }
    
    @Override
    public Void visitCmdLeia(LAParser.CmdLeiaContext ctx) {
        // gera o código do comando leia
        Variavel ident = escopo.obterEscopoAtual().getVariavel(ctx.identificador(0).getText());
        saida.append(String.format("scanf(\"%s\", &%s);\n", ident.tipo.imprimePorcentagem(), ident.nome));
        return null;
    }

    @Override
    public Void visitCmdEscreva(LAParser.CmdEscrevaContext ctx) {
        // gera o código do comando escreva
        for (var exp : ctx.expressao()) {
            TipoLA tipoExp = LASemanticoUtils.verificaExpressao(escopo.obterEscopoAtual(), exp);
            saida.append(String.format("printf(\"%s\", ", tipoExp.imprimePorcentagem()));
            visitExpressao(exp);
            saida.append(");\n");
        }
        return null;
    }
    
    @Override 
    public Void visitCmdAtribuicao(LAParser.CmdAtribuicaoContext ctx) {
        // gera o código do comando atribuicao
        if (ctx.getChild(0).getText().equals("^")) {
            saida.append("*");
        }
        Variavel ident = LASemanticoUtils.verificaIdentificador(escopo.obterEscopoAtual(), ctx.identificador());
        if (ident.tipo != null && ident.tipo.tipoBasico != TipoLA.TipoBasico.LITERAL) {
            visitIdentificador(ctx.identificador());
            saida.append(" = ");
            visitExpressao(ctx.expressao());
        } else {
            saida.append("strcpy(" + ctx.identificador().getText() + ",");
            visitExpressao(ctx.expressao());
            saida.append(")");
        }
        saida.append(";\n");
        return null;
    }
    
    @Override
    public Void visitCmdSe(LAParser.CmdSeContext ctx) {
        // gera o código do comando se
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
    
    @Override
    public Void visitCmdCaso(LAParser.CmdCasoContext ctx) {
        // gera o código do comando caso
        // cria um switch case
        saida.append("switch (");
        visitExp_aritmetica(ctx.exp_aritmetica());
        saida.append(") {\n");
        visitSelecao(ctx.selecao());
        if (!ctx.cmd().isEmpty()) {
            // existe senao
            saida.append("default:\n");
            for (var cmd : ctx.cmd()) {
                visitCmd(cmd);
            }
        }
        // terminou switch case
        saida.append("}\n");
        return null;
    }
    
    @Override
    public Void visitCmdPara(LAParser.CmdParaContext ctx) {
        // gera o código do comando para
        // cria um foor-loop
        saida.append("for (");
        Variavel ident = escopo.obterEscopoAtual().getVariavel(ctx.IDENT().getText());
        // for( X; _; _)
        saida.append(ident.nome + " = ");
        visitExp_aritmetica(ctx.inicio); 
        saida.append("; ");
        // for ( _; X; _)
        saida.append(ident.nome + " <= ");
        visitExp_aritmetica(ctx.fim);
        saida.append("; ");
        // for ( _; _; X)
        saida.append(ident.nome + "++) {\n");
        // comandos dentro do loop
        for (var cmd : ctx.cmd()) {
            // visita cada comando
            visitCmd(cmd);
        }
        saida.append("}\n");
        return null;
    }
    
    @Override
    public Void visitCmdEnquanto(LAParser.CmdEnquantoContext ctx) {
        // gera o código do comando para
        // cria um while-loop
        saida.append("while (");
        // condição
        visitExpressao(ctx.expressao());
        saida.append(") {\n");
        for (var cmd : ctx.cmd()) {
            // para cada comando dentro do loop
            visitCmd(cmd);
        }
        saida.append("}\n");
        return null;
    }
    
    @Override
    public Void visitCmdFaca(LAParser.CmdFacaContext ctx) {
        // gera o código do comando para
        // cria um do-while-loop
        saida.append("do {\n");
        for (var cmd : ctx.cmd()) {
            // para cada comando dentro do loop
            visitCmd(cmd);
        }
        // enquanto
        saida.append("} while (");
        visitExpressao(ctx.expressao());
        saida.append(");\n");
        return null;
    }
    
    @Override
    public Void visitCmdChamada(LAParser.CmdChamadaContext ctx) {
        // gera o código do comando chamada
        // cria uma chamada de função ou procedimento
        saida.append(ctx.IDENT().getText() + "(");
        visitExpressao(ctx.expressao(0));
        for (int i = 1; i < ctx.expressao().size(); i++) {
            saida.append(", ");
            visitExpressao(ctx.expressao(i));
        }
        saida.append(");\n");
        return null;
    }
    
    @Override
    public Void visitCmdRetorne(LAParser.CmdRetorneContext ctx) {
        // gera o código do comando retorne
        saida.append("return ");
        visitExpressao(ctx.expressao());
        saida.append(";\n");
        return null;
    }
    // -- Fim Comandos ---
    
    // -- Seleção ---
    @Override
    public Void visitSelecao(LAParser.SelecaoContext ctx) {
        // gera o código de uma seleção
        for (var itemSelecao : ctx.item_selecao()) {
            // visita cada item_selecao
            visitItem_selecao(itemSelecao);
            saida.append("break;\n");
        }
        return null;
    }
    
    @Override
    public Void visitItem_selecao(LAParser.Item_selecaoContext ctx) {
        // visita um item de umca seleção
        visitConstantes(ctx.constantes());
        for (var cmd : ctx.cmd()) {
            // visita cada comando
            visitCmd(cmd);
        }
        return null;
    }
    
    @Override
    public Void visitConstantes(LAParser.ConstantesContext ctx) {
        // visita uma constante
        visitNumero_intervalo(ctx.numero_intervalo(0));
        for (int i = 1; i < ctx.numero_intervalo().size(); i++) {
            visitNumero_intervalo(ctx.numero_intervalo(i));
        }
        return null;
    }
    
    @Override
    public Void visitNumero_intervalo(LAParser.Numero_intervaloContext ctx) {
        // gera o código de um numero_intervalo
        // case de um switch 
        int comeco, fim;
        if (ctx.opUnario1 != null) {
            comeco = -Integer.parseInt(ctx.NUM_INT(0).getText());
        } else {
            comeco = Integer.parseInt(ctx.NUM_INT(0).getText());
        }
        if (ctx.opUnario2 != null) {
            fim = -Integer.parseInt(ctx.NUM_INT(1).getText());
        } else if (ctx.NUM_INT(1) != null) {
            fim = Integer.parseInt(ctx.NUM_INT(1).getText());
        } else {
            fim = comeco;
        }
        for (int i = comeco; i <= fim; i++) {
            saida.append("case " + i + ":\n");
        }
        return null;
    }
    // --- Fim Seleção ---
    
    // --- Expressão ---
    @Override
    public Void visitExpressao(LAParser.ExpressaoContext ctx) {
        // gera o código de uma expressão
        visitTermo_logico(ctx.termo_logico(0));
        for (int i = 0; i < ctx.op_logico_1().size(); i++) {
            saida.append(" || ");
            visitTermo_logico(ctx.termo_logico(i + 1));
        }
        return null;
    }
    
    @Override
    public Void visitTermo_logico(LAParser.Termo_logicoContext ctx) {
        // gera o código de um termo_logico
        visitFator_logico(ctx.fator_logico(0));
        for (int i = 0; i < ctx.op_logico_2().size(); i++) {
            saida.append(" && ");
            visitFator_logico(ctx.fator_logico(i + 1));
        }
        return null;
    }
    
    @Override
    public Void visitFator_logico(LAParser.Fator_logicoContext ctx) {
        // gera o código de um fator_logico
        if (ctx.getChild(0).getText().equals("nao")) {
            saida.append("!");
        }
        visitParcela_logica(ctx.parcela_logica());
        return null;
    }
    
    @Override
    public Void visitParcela_logica(LAParser.Parcela_logicaContext ctx) {
        // gera o código de uma parcela_logica
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
        // gera o código de uma exp_relacional
        visitExp_aritmetica(ctx.exp_aritmetica(0));
        if (ctx.op_relacional() != null) {
            visitOp_relacional(ctx.op_relacional());
            visitExp_aritmetica(ctx.exp_aritmetica(1));
        }
        return null;
    }
    
    @Override
    public Void visitOp_relacional(LAParser.Op_relacionalContext ctx) {
        // gera o código de um op_relacional
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
        // gera o código de uma exp_aritmetica
        visitTermo(ctx.termo(0));
        for (int i = 0; i < ctx.op1().size(); i++) {
            saida.append(" " + ctx.op1(i).getText() + " ");
            visitTermo(ctx.termo(i + 1));
        }
        return null;
    }
    
    @Override
    public Void visitTermo(LAParser.TermoContext ctx) {
        // gera o código de um termo
        visitFator(ctx.fator(0));
        for (int i = 0; i < ctx.op2().size(); i++) {
            saida.append(" " + ctx.op2(i).getText() + " ");
            visitFator(ctx.fator(i + 1));
        }
        return null;
    }
    
    @Override
    public Void visitFator(LAParser.FatorContext ctx) {
        // gera o código de um fator
        visitParcela(ctx.parcela(0));
        for (int i = 0; i < ctx.op3().size(); i++) {
            saida.append(" " + ctx.op3(i).getText() + " ");
            visitParcela(ctx.parcela(i));
        }
        return null;
    }
    
    @Override
    public Void visitParcela(LAParser.ParcelaContext ctx) {
        // gera o código de uma parcela
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
        // gera o código de uma parcela_unario
        if (ctx.identificador() != null) {
            // possui identificador
            if (ctx.getChild(0).getText().equals("^")) {
                saida.append("*");
            }
            visitIdentificador(ctx.identificador());
        } else if (ctx.IDENT() != null) {
            // possui IDENT
            saida.append(ctx.IDENT().getText() + "(");
            visitExpressao(ctx.expressao(0));
            for (int i = 1; i < ctx.expressao().size(); i++) {
                // para cada expressao
                saida.append(", ");
                visitExpressao(ctx.expressao(i));
            }
            saida.append(")");
        } else if (ctx.NUM_INT() != null) {
            // possui número inteiro
            saida.append(ctx.NUM_INT().getText());
        } else if (ctx.NUM_REAL() != null) {
            // possui número real
            saida.append(ctx.NUM_REAL().getText());
        } else {
            saida.append("(");
            for (var exp : ctx.expressao()) {
                // para cada expressão
                visitExpressao(exp);
            }
            saida.append(")");
        }
        return null;
    }
    
    @Override
    public Void visitParcela_nao_unario(LAParser.Parcela_nao_unarioContext ctx) {
        // gera o código de uma parcela_nao_unario
        if (ctx.identificador() != null) {
            if (ctx.getChild(0).getText().equals("&")) {
                saida.append("&");
            }
            visitIdentificador(ctx.identificador());
        } else {
            saida.append(ctx.CADEIA().getText());
        }
        return null;
    }
    // Fim da expressão
    
    // --- Identificador ---
    @Override
    public Void visitIdentificador(LAParser.IdentificadorContext ctx) {
        // gera o código de um identificador
        saida.append(ctx.IDENT(0).getText());
        for (int i = 1; i < ctx.IDENT().size(); i++) {
            // para cada IDENT
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
        // gera o código de uma dimensão
        saida.append("[");
        for (var exp : ctx.exp_aritmetica()) {
            visitExp_aritmetica(exp);
        }
        saida.append("]");
        return null;
    }
    // --- Fim Identificador ---
    
    // --- Fim Corpo - Main ---
}
