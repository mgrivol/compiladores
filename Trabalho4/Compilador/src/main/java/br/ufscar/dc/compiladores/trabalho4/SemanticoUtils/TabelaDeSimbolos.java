package br.ufscar.dc.compiladores.trabalho4.SemanticoUtils;

import java.util.HashMap;
import java.util.Map;

public class TabelaDeSimbolos {
    
    private final Map<String, Inimigo> tabela;
    
    public TabelaDeSimbolos() {
        this.tabela = new HashMap<>();
    }
    
    public void adicionar(Inimigo inimigo) {
        tabela.put(inimigo.getNome(), inimigo);
    }
    
    public boolean existe(String nome) {
        return tabela.containsKey(nome);
    }
    
    public Inimigo verificar(String nome) {
        return tabela.get(nome);
    }
}
