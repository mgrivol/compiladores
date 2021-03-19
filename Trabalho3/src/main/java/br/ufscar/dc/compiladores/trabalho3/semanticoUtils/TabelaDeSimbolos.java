package br.ufscar.dc.compiladores.trabalho3.semanticoUtils;

import java.util.HashMap;
import java.util.Map;

public class TabelaDeSimbolos {

    private final Map<String, Variavel> tabela;

    public TabelaDeSimbolos() {
        this.tabela = new HashMap();
    }

    public void adicionar(Variavel v) {
        // Adiciona uma váriavel na tabela
        tabela.put(v.nome, v);
    }

    public boolean existe(String nome) {
        // Retorna verdadeiro se a variavel com "nome" existir na tabela
        return tabela.containsKey(nome);
    }

    public TipoLA verificar(String nome) {
        // Retorna o tipo da variavel da tabela
        return tabela.get(nome).tipo;
    }

    public Variavel getVariavel(String nome) {
        return tabela.get(nome);
    }

    public void Imprime() {
        // Função temporária para depuração
        System.out.println("=========COMECO===TABELA========");
        for (var name : this.tabela.keySet()) {
            String key = name.toString();
            if (this.verificar(key).tipoBasico != null) {
                System.out.println("<" + key + ", " + this.verificar(key).tipoBasico + ">");
            } else {
                System.out.println("<" + key + ", " + this.verificar(key).tipoCriado + ">");
            }
            if (this.verificar(key).tipoBasico == TipoLA.TipoBasico.REGISTRO) {
                System.out.println(this.getVariavel(key).dados());
            }
            if (this.verificar(key).tipoBasico == TipoLA.TipoBasico.FUNCAO) {
                Variavel f = this.getVariavel(key);
                System.out.println("     tipo retorno ->" + f.funcao.getTipoRetorno().imprime());
                if (f.funcao.getParametros() != null) {
                    System.out.println("     parametros");
                    for (var p : f.funcao.getParametros()) {
                        System.out.println(p.dados());
                    }
                }
                if (f.funcao.getVariaveisLocais() != null) {
                    for (var v : f.funcao.getVariaveisLocais()) {
                        System.out.println(v.dados());
                    }
                }
                System.out.println("fim funcao");
            }
        }
        System.out.println("=========FIM======TABELA========");
    }
}
