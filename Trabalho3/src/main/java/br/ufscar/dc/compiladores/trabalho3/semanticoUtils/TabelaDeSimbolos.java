package br.ufscar.dc.compiladores.trabalho3.semanticoUtils;

import java.util.HashMap;
import java.util.Map;

public class TabelaDeSimbolos {
    // armazena o nome e dados de uma variável 
    
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
}
