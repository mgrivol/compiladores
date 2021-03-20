package br.ufscar.dc.compiladores.trabalho3.semanticoUtils;

import br.ufscar.dc.compiladores.trabalho3.LAParser;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.tree.TerminalNode;

public class LASemanticoUtils {

    // mensagens de erro
    public static List<String> erros = new ArrayList<>();
    // define quando é possível retornar um valor
    public static boolean podeRetornar = false;

    public static void adicionaErro(String msg) {
        erros.add(msg);
    }

    //*****   Declarações Globais   *****
    public static Variavel verificaDeclGlobal(Escopos escopo, LAParser.Declaracao_globalContext ctx) {
        System.out.println("!global!");
        String decl = ctx.start.getText();
        Variavel entrada = null;
        switch (decl) {
            case "procedimento":
                System.out.println("Procedimento");
                entrada = LASemanticoUtils.verificaProcedimento(escopo, ctx);
                break;
            case "funcao":
                System.out.println("Funcao " + ctx.getChild(1).getText());
                LASemanticoUtils.podeRetornar = true;
                entrada = LASemanticoUtils.verificaFuncao(escopo, ctx);
                LASemanticoUtils.podeRetornar = false;
                break;
        }
        return entrada;
    }

    public static Variavel verificaProcedimento(Escopos escopo, LAParser.Declaracao_globalContext ctx) {
        // retorna uma Variavel que contém todos os dados relevantes do procedimentos
        String nomeProc = ctx.IDENT().getText();
        Variavel ret = new Variavel(nomeProc, new TipoLA(TipoLA.TipoBasico.PROCEDIMENTO));
        escopo.criarNovoEscopo();
        if (ctx.parametros() != null) {
            // existem parâmetros
            List<Variavel> parametros = verificaParametros(escopo, ctx.parametros());
            ret.procedimento.setParametros(parametros);
            for (var p : parametros) {
                // atualiza o escopo atual com os parâmetros
                escopo.obterEscopoAtual().adicionar(p);
            }
        }
        if (ctx.declaracao_local() != null) {
            List<Variavel> declaracoes = new ArrayList<>();
            for (var decl : ctx.declaracao_local()) {
                // para cada declaração local
                declaracoes.addAll(verificaDeclLocal(escopo, decl));
            }
            for (var decl : declaracoes) {
                // atualiza o escopo com as declarações
                escopo.obterEscopoAtual().adicionar(decl);
            }

            // relaciona as variáveis com a função
            ret.procedimento.setVariaveisLocais(declaracoes);
        }
        if (ctx.cmd() != null) {
            for (var cmd : ctx.cmd()) {
                // para cada comando dentro do procedimento
                verificaCmd(escopo, cmd);
            }
        }
        escopo.abandonarEscopo();
        return ret;
    }

    public static Variavel verificaFuncao(Escopos escopo, LAParser.Declaracao_globalContext ctx) {
        String nomeFuncao = ctx.IDENT().getText(); // IDENT contém o nome da função
        TipoLA tipoRetorno = verificaTipoEstendido(escopo, ctx.tipo_estendido());
        if (tipoRetorno.tipoBasico != null && tipoRetorno.tipoBasico == TipoLA.TipoBasico.INVALIDO) {
            erroTipoNaoDeclarado(ctx.start.getLine(), ctx.tipo_estendido().getText());
        }
        Variavel ret = new Variavel(nomeFuncao, new TipoLA(TipoLA.TipoBasico.FUNCAO)); // Variavel ret contém todos os dados desta função
        escopo.criarNovoEscopo();
        ret.funcao.setTipoRetorno(tipoRetorno); // define o tipo que a função retorna
        if (ctx.parametros() != null) {
            List<Variavel> parametros = verificaParametros(escopo, ctx.parametros());
            ret.funcao.setParametros(parametros);
            for (var p : parametros) {
                // atualiza o escopo atual com os parâmetros
                escopo.obterEscopoAtual().adicionar(p);
            }
        }
        List<Variavel> declaracoes = new ArrayList<>();
        for (var decl : ctx.declaracao_local()) {
            // para cada declaração local
            declaracoes.addAll(verificaDeclLocal(escopo, decl)); // adicione na lista de declaracoes
        }
        for (var decl : declaracoes) {
            // atualiza o escopo com as declarações
            escopo.obterEscopoAtual().adicionar(decl);
        }
        ret.funcao.setVariaveisLocais(declaracoes); // relaciona as variáveis com a função
        for (var cmd : ctx.cmd()) {
            // para cada comando dentro da função
            verificaCmd(escopo, cmd);
        }
        escopo.abandonarEscopo();
        return ret;

    }

    public static List<Variavel> verificaParametros(Escopos escopo, LAParser.ParametrosContext ctx) {
        List<Variavel> ret = new ArrayList<>();
        for (var parametro : ctx.parametro()) {
            // para cada parâmetro
            ret.addAll(verificaParametro(escopo, parametro));
        }
        return ret;
    }

    public static List<Variavel> verificaParametro(Escopos escopo, LAParser.ParametroContext ctx) {
        TipoLA tipo = verificaTipoEstendido(escopo, ctx.tipo_estendido());
        List<Variavel> ret = new ArrayList<>();
        for (var ident : ctx.identificador()) {
            Variavel novaVar = verificaIdentificador(escopo.obterEscopoAtual(), ident);
            if (novaVar.tipo != null) {
                // encontrou uma variável com mesmo nome no escopo atual
                erroIdentificadorJaDeclarado(ident.start.getLine(), ident.getText());
            } else {
                // não encontrou nenhuma variável com mesmo nome neste escopo
                novaVar = new Variavel(ident.getText(), tipo);
                if (tipo.tipoCriado != null) {
                    // variável possui um tipo criado
                    for (var ts : escopo.percorrerEscoposAninhados()) {
                        // busca pelo tipo criado nos escopo
                        Variavel aux = adicionaTipoCriado(ts, novaVar, tipo.tipoCriado);
                        if (aux.tipo != null) {
                            novaVar = new Variavel(novaVar.nome, new TipoLA(TipoLA.TipoBasico.REGISTRO));
                            if (novaVar.registro == null) {
                                // System.out.println("registro nulo");
                            }
                            novaVar = aux;
                        }
                    }
                }
                escopo.obterEscopoAtual().adicionar(novaVar);
                ret.add(novaVar);
            }
        }
        return ret;
    }
    //*****   Fim Declarações Globais   *****

    //*****   Declaracão Local   *****
    public static List<Variavel> verificaDeclLocal(Escopos escopo, LAParser.Declaracao_localContext ctx) {
        String decl = ctx.getStart().getText();
        List<Variavel> variaveis = new ArrayList<>();
        switch (decl) {
            case "declare":
                System.out.println("Declaracao simples");
                variaveis = verificaVariavel(escopo, ctx.variavel());
                break;
            case "constante":
                System.out.println("Declaracao constante");
                variaveis.add(verificaDeclConstante(escopo, ctx));
                break;
            case "tipo":
                System.out.println("Declaracao Tipo");
                variaveis.add(verificaDeclTipo(escopo, ctx));
                break;
        }
        return variaveis;
    }

    public static Variavel verificaDeclConstante(Escopos escopo, LAParser.Declaracao_localContext ctx) {
        TipoLA tipo = new TipoLA(verificaTipoBasico(ctx.tipo_basico())); // tipo do IDENT
        if (tipo.tipoBasico != null && tipo.tipoBasico == TipoLA.TipoBasico.INVALIDO) {
            erroTipoNaoDeclarado(ctx.start.getLine(), ctx.tipo().getText());
            System.out.println("TIPO BASICO NAO ENCONTRADO DECL CONSTANTE");
            return null;
        }
        return new Variavel(ctx.IDENT().getText(), tipo);
    }
    //*****   Fim Declarações Locais   *****

    public static Variavel verificaDeclTipo(Escopos escopo, LAParser.Declaracao_localContext ctx) {
        TipoLA tipoIDENT = verificaTipo(escopo, ctx.tipo()); // tipo do IDENT
        if (tipoIDENT.tipoBasico != null && tipoIDENT.tipoBasico == TipoLA.TipoBasico.INVALIDO) {
            if (tipoIDENT.tipoBasico != null && tipoIDENT.tipoBasico == TipoLA.TipoBasico.INVALIDO) {
                erroTipoNaoDeclarado(ctx.start.getLine(), ctx.tipo().getText());
            }
            return null;
        }
        String nome = ctx.IDENT().getText();
        TipoLA.adicionaTipoCriado(nome);
        Variavel novoTipo = new Variavel(nome, tipoIDENT);
        if (novoTipo.tipo.tipoBasico == TipoLA.TipoBasico.REGISTRO) {
            novoTipo.registro = verificaRegistro(escopo, ctx.tipo().registro()).registro;
        } else if (tipoIDENT.tipoCriado != null) {
            System.out.println("TIPO CUSTOM NO TIPO CUSTOM");
        }
        return novoTipo;
    }
    //*****   Fim Declarações Locais   *****

    public static void verificaCmd(Escopos escopo, LAParser.CmdContext ctx) {
        if (ctx.cmdAtribuicao() != null) {
            System.out.println("CMD Atribuicao");
            verificaCmdAtribuicao(escopo, ctx.cmdAtribuicao());
        } else if (ctx.cmdEscreva() != null) {
            System.out.println("CMD Escreva");
            verificaCmdEscreva(escopo, ctx.cmdEscreva());
        } else if (ctx.cmdLeia() != null) {
            System.out.println("CMD Leia");
            verificaCmdLeia(escopo, ctx.cmdLeia());
        } else if (ctx.cmdEnquanto() != null) {
            System.out.println("CMD Enquanto");
            verificaCmdEnquanto(escopo, ctx.cmdEnquanto());
        } else if (ctx.cmdSe() != null) {
            System.out.println("CMD Se");
            verificaCmdSe(escopo, ctx.cmdSe());
        } else if (ctx.cmdFaca() != null) {
            System.out.println("CMD Faca");
            verificaCmdFaca(escopo, ctx.cmdFaca());
        } else if (ctx.cmdRetorne() != null) {
            verificaCmdRetorne(ctx.cmdRetorne());
        }
    }

    public static void verificaCmdLeia(Escopos escopo, LAParser.CmdLeiaContext ctx) {
        List<Integer> ponteiros = new ArrayList<>();
        String[] idents = ctx.getText().split(",");
        for (int i = 0; i < idents.length; i++) {
            if (idents[i].contains("^")) {
                System.out.println("CMD LEIA contem ponteiro");
                ponteiros.add(i);
            }
        }
        for (var ident : ctx.identificador()) {
            Variavel v = verificaIdentificador(escopo.obterEscopoAtual(), ident);
            if (v != null && v.tipo == null) { // identificador não existe na tabela
                erroIdentificadorNaoDeclarado(ident.getStart().getLine(), ident.getText());
            }
        }
    }

    public static void verificaCmdEscreva(Escopos escopo, LAParser.CmdEscrevaContext ctx) {
        for (var exp : ctx.expressao()) {
            verificaExpressao(escopo, exp);
        }
    }

    public static void verificaCmdAtribuicao(Escopos escopo, LAParser.CmdAtribuicaoContext ctx) {
        // lado esquerdo da expressão
        Variavel esquerdo = verificaIdentificador(escopo.obterEscopoAtual(), ctx.identificador());
        if (esquerdo.tipo == null) {
            // identificador do lado esquerdo não existe
            erroIdentificadorNaoDeclarado(ctx.identificador().start.getLine(), ctx.identificador().getText());
            return;
        }
        // lado direito da expressão
        TipoLA direito = verificaExpressao(escopo, ctx.expressao());
        String pont = "";
        if (ctx.getChild(0).getText().contains("^")) {
            pont += "^";
            esquerdo.tipo = esquerdo.getTipoPonteiroAninhado();
        }
        if (esquerdo.tipo.tipoBasico != null && direito.tipoBasico != null) {
            // se for um tipo padrão
            if (verificaEquivalenciaTipos(esquerdo.tipo, direito).tipoBasico == TipoLA.TipoBasico.INVALIDO) {
                erroAtribuicaoIncompativel(ctx.identificador().start.getLine(), pont + ctx.identificador().getText());
            }
        }
    }

    public static void verificaCmdEnquanto(Escopos escopo, LAParser.CmdEnquantoContext ctx) {
        verificaExpressao(escopo, ctx.expressao());
    }

    public static void verificaCmdSe(Escopos escopo, LAParser.CmdSeContext ctx) {
        verificaExpressao(escopo, ctx.expressao());
        for (var cmd : ctx.cmd()) {
            verificaCmd(escopo, cmd);
        }
    }

    public static void verificaCmdFaca(Escopos escopo, LAParser.CmdFacaContext ctx) {
        for (var cmd : ctx.cmd()) {
            verificaCmd(escopo, cmd);
        }
        verificaExpressao(escopo, ctx.expressao());
    }

    public static void verificaCmdRetorne(LAParser.CmdRetorneContext ctx) {
        if (!LASemanticoUtils.podeRetornar) {
            LASemanticoUtils.adicionaErro("Linha " + ctx.start.getLine() + ": comando retorne nao permitido nesse escopo");
        }
    }

    public static List<Variavel> verificaVariavel(Escopos escopo, LAParser.VariavelContext ctx) {
        // Retorna uma lista de variaveis válidas
        List<Variavel> variaveis = new ArrayList<>();
        TipoLA tipo = verificaTipo(escopo, ctx.tipo());
        if (tipo.tipoBasico != null && tipo.tipoBasico == TipoLA.TipoBasico.INVALIDO) {
            erroTipoNaoDeclarado(ctx.start.getLine(), ctx.tipo().getText());
        }
        for (var ident : ctx.identificador()) {
            // verifica a validade de todos identificadores
            Variavel novaVar = verificaIdentificador(escopo.obterEscopoAtual(), ident);
            if (novaVar.tipo != null) {
                // encontrou um identificador com o mesmo nome neste escopo
                erroIdentificadorJaDeclarado(ident.getStart().getLine(), ident.getText());
            } else {
                // se não existe, adiciona
                novaVar = new Variavel(novaVar.nome, tipo);
                if (tipo.tipoBasico == TipoLA.TipoBasico.REGISTRO) {
                    novaVar.registro = verificaRegistro(escopo, ctx.tipo().registro()).registro;
                } else if (tipo.tipoCriado != null) {
                    novaVar = adicionaTipoCriado(escopo.obterEscopoAtual(), novaVar, tipo.tipoCriado);
                }
                // o escopo não é alterado neste momento pois não é retornado
                // escopo é atualizado para verificação de declaração dupla
                escopo.obterEscopoAtual().adicionar(novaVar);
                variaveis.add(novaVar);
            }
        }
        return variaveis;
    }

    public static Variavel verificaRegistro(Escopos escopo, LAParser.RegistroContext ctx) {
        Variavel reg = new Variavel(null, new TipoLA(TipoLA.TipoBasico.REGISTRO));
        escopo.criarNovoEscopo();
        for (int i = 0; i < ctx.variavel().size(); i++) {
            // System.out.println("var -> " + ctx.variavel(i).getText());
            reg.registro.adicionaNoRegistro(verificaVariavel(escopo, ctx.variavel(i)));
        }
        escopo.abandonarEscopo();
        return reg;
    }

    public static TipoLA verificaTipo(Escopos escopo, LAParser.TipoContext ctx) {
        if (ctx.registro() != null) {
            return new TipoLA(TipoLA.TipoBasico.REGISTRO);
        }
        return verificaTipoEstendido(escopo, ctx.tipo_estendido());
    }

    public static TipoLA verificaTipoEstendido(Escopos escopo, LAParser.Tipo_estendidoContext ctx) {
        if (ctx.getChild(0).getText().contains("^")) {
            // tipo PONTEIRO
            TipoLA tipoPonteiro = new TipoLA(TipoLA.TipoBasico.PONTEIRO);
            TipoLA tipoAponta = verificaTipoBasicoIdent(escopo, ctx.tipo_basico_ident());
            // System.out.println("verifica tipo estendido encontrou PONTEIRO");
            return new TipoLA(tipoPonteiro, tipoAponta);
        }
        return verificaTipoBasicoIdent(escopo, ctx.tipo_basico_ident());
    }

    public static TipoLA verificaTipoBasicoIdent(Escopos escopo, LAParser.Tipo_basico_identContext ctx) {
        if (ctx.tipo_basico() != null) {
            return new TipoLA(verificaTipoBasico(ctx.tipo_basico()));
        }
        if (TipoLA.existeTipoCriado(ctx.IDENT().getText())) {
            // tipo criado pelo usuario
            return new TipoLA(TipoLA.getTipoCriado(ctx.IDENT().getText()));
        }
        return new TipoLA(TipoLA.TipoBasico.INVALIDO);
    }

    public static TipoLA.TipoBasico verificaTipoBasico(LAParser.Tipo_basicoContext ctx) {
        // verifica se o tipo é um dos básicos da linguagem
        TipoLA.TipoBasico tipo = TipoLA.TipoBasico.INVALIDO;
        switch (ctx.getText()) {
            case "inteiro":
                tipo = TipoLA.TipoBasico.INTEIRO;
                break;
            case "real":
                tipo = TipoLA.TipoBasico.REAL;
                break;
            case "literal":
                tipo = TipoLA.TipoBasico.LITERAL;
                break;
            case "logico":
                tipo = TipoLA.TipoBasico.LOGICO;
                break;
        }
        return tipo;
    }

    public static TipoLA verificaExpressao(Escopos escopo, LAParser.ExpressaoContext ctx) {
        TipoLA tipoPrimeiroTermo = verificaTermoLogico(escopo, ctx.termo_logico(0));
        if (ctx.termo_logico().size() > 1) {
            // se exitir mais de um termo_logico
            for (int i = 1; i < ctx.termo_logico().size(); i++) {
                TipoLA tipoSegundoTermo = verificaTermoLogico(escopo, ctx.termo_logico(i));
                // operador lógico "ou" só pode ser usado com tipos lógicos
                tipoPrimeiroTermo = verificaEquivalenciaTipos(tipoPrimeiroTermo, tipoSegundoTermo);
            }
            if (tipoPrimeiroTermo.tipoBasico != TipoLA.TipoBasico.INVALIDO) {
                // a operção é válida, como existe outro operator, a operação é do tipo lógico
                tipoPrimeiroTermo.tipoBasico = TipoLA.TipoBasico.LOGICO;
            }
        }
        return tipoPrimeiroTermo;
    }

    public static TipoLA verificaTermoLogico(Escopos escopo, LAParser.Termo_logicoContext ctx) {
        TipoLA tipoPrimeiroFator = verificaFatorLogico(escopo, ctx.fator_logico(0));
        if (ctx.fator_logico().size() > 1) {
            // se exitir mais de um fator_logico
            for (int i = 1; i < ctx.fator_logico().size(); i++) {
                TipoLA tipoSegundoFator = verificaFatorLogico(escopo, ctx.fator_logico(i));
                // operador lógico "e" só pode ser usado com os tipos
                tipoPrimeiroFator = verificaEquivalenciaTipos(tipoPrimeiroFator, tipoSegundoFator);
            }
            if (tipoPrimeiroFator.tipoBasico != TipoLA.TipoBasico.INVALIDO) {
                // a operção é válida, como existe outro operator, a operação é do tipo lógico
                tipoPrimeiroFator.tipoBasico = TipoLA.TipoBasico.LOGICO;
            }
        }
        return tipoPrimeiroFator;
    }

    public static TipoLA verificaFatorLogico(Escopos escopo, LAParser.Fator_logicoContext ctx) {
        TipoLA parcelaLogica = verificaParcelaLogica(escopo, ctx.parcela_logica());
        if (ctx.getChild(0).getText().contains("nao")) {
            // negação é válida apenas para tipos lógicos
            return verificaEquivalenciaTipos(parcelaLogica, new TipoLA(TipoLA.TipoBasico.LOGICO));
        }
        return parcelaLogica;
    }

    public static TipoLA verificaParcelaLogica(Escopos escopo, LAParser.Parcela_logicaContext ctx) {
        TipoLA tipoParcela;
        if (ctx.exp_relacional() != null) {
            // existe exp_relacional
            tipoParcela = verificaExpRelacional(escopo, ctx.exp_relacional());
        } else {
            // verdadeiro ou falso
            tipoParcela = new TipoLA(TipoLA.TipoBasico.LOGICO);
        }
        return tipoParcela;
    }

    public static TipoLA verificaExpRelacional(Escopos escopo, LAParser.Exp_relacionalContext ctx) {
        TipoLA tipoPrimeiraExp = verificaExpAritmetica(escopo, ctx.exp_aritmetica(0));
        if (ctx.exp_aritmetica().size() > 1) {
            TipoLA tipoSegundaExp = verificaExpAritmetica(escopo, ctx.exp_aritmetica(1));
            tipoPrimeiraExp = verificaEquivalenciaTipos(tipoPrimeiraExp, tipoSegundaExp);
            if (tipoPrimeiraExp.tipoBasico != TipoLA.TipoBasico.INVALIDO) {
                // a operção é válida, como existe outro operator, a operação é do tipo lógico
                tipoPrimeiraExp.tipoBasico = TipoLA.TipoBasico.LOGICO;
            }
        }
        return tipoPrimeiraExp;
    }

    public static TipoLA verificaExpAritmetica(Escopos escopo, LAParser.Exp_aritmeticaContext ctx) {
        TipoLA tipoPrimeiroTermo = verificaTermo(escopo, ctx.termo(0));
        if (ctx.termo().size() > 1) {
            // se existir mais de um termo
            for (int i = 1; i < ctx.termo().size(); i++) {
                TipoLA tipoSegundoTermo = verificaTermo(escopo, ctx.termo(i));
                tipoPrimeiroTermo = verificaEquivalenciaTipos(tipoPrimeiroTermo, tipoSegundoTermo);
            }
            for (var op : ctx.op1()) {
                if (op.getText().contains("+") && tipoPrimeiroTermo.tipoBasico == TipoLA.TipoBasico.LITERAL) {
                    // é possível "somar" literais, inteiros, e reais
                    // evita identificador inválido caso seja uma soma de literais
                    tipoPrimeiroTermo = verificaEquivalenciaTipos(tipoPrimeiroTermo, new TipoLA(TipoLA.TipoBasico.LITERAL));
                } else {
                    // "-" apenas com valoes inteiros ou reais
                    tipoPrimeiroTermo = verificaEquivalenciaTipos(tipoPrimeiroTermo, new TipoLA(TipoLA.TipoBasico.INTEIRO));
                }
            }
        }
        return tipoPrimeiroTermo;
    }

    public static TipoLA verificaTermo(Escopos escopo, LAParser.TermoContext ctx) {
        TipoLA tipoPrimeiroFator = verificaFator(escopo, ctx.fator(0));
        if (ctx.fator().size() > 1) {
            // se existir mais de um fator
            for (int i = 1; i < ctx.fator().size(); i++) {
                TipoLA tipoSegundoFator = verificaFator(escopo, ctx.fator(i));
                tipoPrimeiroFator = verificaEquivalenciaTipos(tipoPrimeiroFator, tipoSegundoFator);
            }
        }
        return tipoPrimeiroFator;
    }

    public static TipoLA verificaFator(Escopos escopo, LAParser.FatorContext ctx) {
        TipoLA tipoPrimeiraParcela = verificaParcela(escopo, ctx.parcela(0));
        if (ctx.parcela().size() > 1) {
            // se existir mais de uma parcela
            for (int i = 1; i < ctx.parcela().size(); i++) {
                TipoLA tipoSegundaParcela = verificaParcela(escopo, ctx.parcela(i));
                // se for invalido, a primeira parcela carrega esse valor até ser retornada
                tipoPrimeiraParcela = verificaEquivalenciaTipos(tipoPrimeiraParcela, tipoSegundaParcela);
            }
        }
        return tipoPrimeiraParcela;
    }

    public static TipoLA verificaParcela(Escopos escopo, LAParser.ParcelaContext ctx) {
        if (ctx.parcela_unario() != null) {
            TipoLA pUnario = verificaParcelaUnario(escopo, ctx.parcela_unario());
            // existe parcela_unario
            if (ctx.op_unario() != null) {
                // existe op_unario
                if (pUnario.tipoBasico != TipoLA.TipoBasico.INTEIRO && pUnario.tipoBasico != TipoLA.TipoBasico.REAL) {
                    // não é possível atribuir sinal negativo a outro tipo
                    // operador unário não é valido
                    return new TipoLA(TipoLA.TipoBasico.INVALIDO);
                }
                // operador unario é válido
                return pUnario;
            }
            // não existe operador unário, retorna tipo da parcela unario
            return pUnario;
        }
        // existe parcela_nao_unario
        return verificaParcelaNaoUnario(escopo, ctx.parcela_nao_unario());
    }

    public static TipoLA verificaParcelaUnario(Escopos escopo, LAParser.Parcela_unarioContext ctx) {
        // retorna tipo INVALIDO se identificador não existir ou se ctx.expressao() possuir tipo INVALIDO 
        if (ctx.identificador() != null) {
            // existe um identificador
            // System.out.println(ctx.getText());
            if (ctx.getChild(0).getText().contains("^")) {
                System.out.println("PARCELA UNARIO COM ^ =>" + ctx.getText());
            }
            Variavel ident = verificaIdentificador(escopo.obterEscopoAtual(), ctx.identificador());
            if (ident.tipo == null) {
                // identificador não existe
                erroIdentificadorNaoDeclarado(ctx.identificador().start.getLine(), ident.nome);
                return new TipoLA(TipoLA.TipoBasico.INVALIDO);
            }
            return ident.tipo;
        } else if (ctx.IDENT() != null) {
            return verificaMetodo(escopo, ctx.IDENT(), ctx.expressao());
        } else if (ctx.NUM_INT() != null) {
            return new TipoLA(TipoLA.TipoBasico.INTEIRO);
        } else if (ctx.NUM_REAL() != null) {
            return new TipoLA(TipoLA.TipoBasico.REAL);
        }
        // se não for nenhum caso anterior
        TipoLA primeiraExp = verificaExpressao(escopo, ctx.expressao(0));
        if (ctx.expressao().size() > 1) {
            for (int i = 1; i < ctx.expressao().size(); i++) {
                TipoLA segundaExp = verificaExpressao(escopo, ctx.expressao(i));
                primeiraExp = verificaEquivalenciaTipos(primeiraExp, segundaExp);
            }
        }
        return primeiraExp;
    }

    public static TipoLA verificaParcelaNaoUnario(Escopos escopo, LAParser.Parcela_nao_unarioContext ctx) {
        // retorna tipo INVALIDO se identificador não existir
        if (ctx.CADEIA() != null) {
            // se for uma CADEIA
            return new TipoLA(TipoLA.TipoBasico.LITERAL);
        } else {
            if (ctx.getChild(0).getText().contains("&")) {
                // System.out.println("PARCELA NAO UNARIO COM & =>" + ctx.getText());
                return new TipoLA(TipoLA.TipoBasico.ENDERECO);
            }
            Variavel ident = verificaIdentificador(escopo.obterEscopoAtual(), ctx.identificador());
            if (ident.tipo == null) {
                // identificador não existe
                erroIdentificadorNaoDeclarado(ctx.identificador().start.getLine(), ident.nome);
                return new TipoLA(TipoLA.TipoBasico.INVALIDO);
            }
            return ident.tipo;
        }
    }

    public static Variavel verificaIdentificador(TabelaDeSimbolos ts, LAParser.IdentificadorContext ctx) {
        // Retorna uma variavel contendo o nome do identificador
        String nome = ctx.IDENT(0).getText();
        if (ctx.dimensao().getChildCount() != 0) {
              // System.out.println("EXISTE DIMENSAO NO IDENTIFICADOR");
              // System.out.println(ctx.dimensao().getText());
        }
        if (ts.existe(nome)) { // tenta encontrar um identificador na tabela
            Variavel ret = ts.getVariavel(nome);
            if (ctx.IDENT().size() > 1) {
                ret = ret.getVarNoRegistro(ctx.IDENT(1).getText());
                if (ret == null) {
                    // variável dentro do registro não existe
                    erroIdentificadorNaoDeclarado(ctx.start.getLine(), ctx.getText());
                }
            }
            return ret;
        }
        // identificador não está na tabela
        nome = ctx.IDENT(0).getText();
        for (int i = 1; i < ctx.IDENT().size(); i++) {
            nome += "." + ctx.IDENT(i);
        }
        return new Variavel(nome, null);
    }

    public static TipoLA verificaEquivalenciaTipos(TipoLA a, TipoLA b) {
        TipoLA t = new TipoLA(TipoLA.TipoBasico.INVALIDO);
        if (a.tipoBasico == TipoLA.TipoBasico.PONTEIRO && b.tipoBasico == TipoLA.TipoBasico.ENDERECO) {
            // define tipo PONTEIRO
            t.tipoBasico = TipoLA.TipoBasico.PONTEIRO;
        } else if ((a.tipoBasico == TipoLA.TipoBasico.REAL && b.tipoBasico == TipoLA.TipoBasico.REAL)
                || (a.tipoBasico == TipoLA.TipoBasico.REAL && b.tipoBasico == TipoLA.TipoBasico.INTEIRO)
                || (a.tipoBasico == TipoLA.TipoBasico.INTEIRO && b.tipoBasico == TipoLA.TipoBasico.REAL)
                || (a.tipoBasico == TipoLA.TipoBasico.INTEIRO && b.tipoBasico == TipoLA.TipoBasico.INTEIRO)) {
            // define tipo INTEIRO
            t.tipoBasico = TipoLA.TipoBasico.INTEIRO;
        } else if (a.tipoBasico == TipoLA.TipoBasico.LITERAL && b.tipoBasico == TipoLA.TipoBasico.LITERAL) {
            // define tipo LITERAL
            t.tipoBasico = TipoLA.TipoBasico.LITERAL;
        } else if (a.tipoBasico == TipoLA.TipoBasico.LOGICO && b.tipoBasico == TipoLA.TipoBasico.LOGICO) {
            // define tipo LOGICO
            t.tipoBasico = TipoLA.TipoBasico.LOGICO;
        } else if (a.tipoBasico == TipoLA.TipoBasico.REGISTRO && b.tipoBasico == TipoLA.TipoBasico.REGISTRO) {
            // define tipo REGISTRO
            System.out.println("REGISTRO NA VERIFICAO DE EQUIVALENCIA");
            t.tipoBasico = TipoLA.TipoBasico.REGISTRO;
        }
        return t;
    }

    public static TipoLA verificaEquivalenciaTiposExatos(TipoLA a, TipoLA b) {
        TipoLA t = new TipoLA(TipoLA.TipoBasico.INVALIDO);
        if (a.tipoBasico == TipoLA.TipoBasico.ENDERECO && b.tipoBasico == TipoLA.TipoBasico.PONTEIRO) {
            // define tipo PONTEIRO
            t.tipoBasico = TipoLA.TipoBasico.PONTEIRO;
        } else if (a.tipoBasico == TipoLA.TipoBasico.INTEIRO && b.tipoBasico == TipoLA.TipoBasico.INTEIRO) {
            // define tipo INTEIRO
            t.tipoBasico = TipoLA.TipoBasico.INTEIRO;
        } else if (a.tipoBasico == TipoLA.TipoBasico.REAL && b.tipoBasico == TipoLA.TipoBasico.REAL) {
            // define tipo REAL
            t.tipoBasico = TipoLA.TipoBasico.REAL;
        } else if (a.tipoBasico == TipoLA.TipoBasico.LITERAL && b.tipoBasico == TipoLA.TipoBasico.LITERAL) {
            // define tipo LITERAL
            t.tipoBasico = TipoLA.TipoBasico.LITERAL;
        } else if (a.tipoBasico == TipoLA.TipoBasico.LOGICO && b.tipoBasico == TipoLA.TipoBasico.LOGICO) {
            // define tipo LOGICO
            t.tipoBasico = TipoLA.TipoBasico.LOGICO;
        } else if (a.tipoBasico == TipoLA.TipoBasico.REGISTRO && b.tipoBasico == TipoLA.TipoBasico.REGISTRO) {
            // define tipo REGISTRO
            System.out.println("REGISTRO NA VERIFICAO DE EQUIVALENCIA");
            t.tipoBasico = TipoLA.TipoBasico.REGISTRO;
        }
        return t;
    }

    public static Variavel adicionaTipoCriado(TabelaDeSimbolos ts, Variavel v, String nome) {
        // adiciona o tipo do modeno na variavel
        if (ts.existe(nome)) {
            Variavel modelo = ts.getVariavel(nome);
            if (modelo.tipo.tipoBasico == TipoLA.TipoBasico.REGISTRO) {
                // modelo é do tipo registro
                Variavel ret = new Variavel(v.nome, new TipoLA(TipoLA.TipoBasico.REGISTRO));
                ret.setRegistro(modelo.getRegistro());
                ret.tipo = v.tipo;
                return ret;
            }
        }
        // System.out.println("fim NULO adiciona tipo criado");
        return new Variavel(null, null);
    }

    public static TipoLA verificaMetodo(Escopos escopo, TerminalNode IDENT, List<LAParser.ExpressaoContext> expressoes) {
        TipoLA ret = null;
        Variavel metodo;
        if (!escopo.obterEscopoAtual().existe(IDENT.getText())) {
            System.out.println("IDENT em verifica METODO nao EXISTE");
            metodo = null;
        } else {
            metodo = escopo.obterEscopoAtual().getVariavel(IDENT.getText());
        }
        if (metodo.funcao != null) {
            // se for um método
            if (metodo.funcao.getParametros().size() != expressoes.size()) {
                // verifica a quantidade de parâmetros e expressões
                ret = new TipoLA(TipoLA.TipoBasico.INVALIDO);
            }
            for (var exp : expressoes) {
                TipoLA tipoExp = verificaExpressao(escopo, exp);
                if (ret == null || ret.tipoBasico != TipoLA.TipoBasico.INVALIDO) {
                    // enquanto os tipos das expressões forem iguais aos tipos dos parâmetros
                    ret = verificaEquivalenciaTiposExatos(metodo.funcao.getTipoRetorno(), tipoExp);
                }
            }
        }
        if (ret == null || ret.tipoBasico == TipoLA.TipoBasico.INVALIDO) {
            erroIncompatibilidadeParametros(IDENT.getSymbol().getLine(), IDENT.getText());
            return new TipoLA(TipoLA.TipoBasico.INVALIDO);
        }
        return ret;
    }

    public static void erroIdentificadorNaoDeclarado(int linha, String nome) {
        LASemanticoUtils.adicionaErro("Linha " + linha + ": identificador " + nome + " nao declarado");
        System.out.println("Linha " + linha + ": identificador " + nome + " nao declarado");
    }

    public static void erroIdentificadorJaDeclarado(int linha, String nome) {
        LASemanticoUtils.adicionaErro("Linha " + linha + ": identificador " + nome + " ja declarado anteriormente");
        System.out.println("Linha " + linha + ": identificador " + nome + " ja declarado anteriormente");
    }

    public static void erroAtribuicaoIncompativel(int linha, String nome) {
        LASemanticoUtils.adicionaErro("Linha " + linha + ": atribuicao nao compativel para " + nome);
        System.out.println("Linha " + linha + ": atribuicao nao compativel para " + nome);
    }

    public static void erroTipoNaoDeclarado(int linha, String nome) {
        LASemanticoUtils.adicionaErro("Linha " + linha + ": tipo " + nome + " nao declarado");
        System.out.println("Linha " + linha + ": tipo " + nome + " nao declarado");
    }

    public static void erroIncompatibilidadeParametros(int linha, String nome) {
        LASemanticoUtils.adicionaErro("Linha " + linha + ": incompatibilidade de parametros na chamada de " + nome);
        System.out.println("Linha " + linha + ": incompatibilidade de parametros na chamada de " + nome);
    }
}
