package br.ufscar.dc.compiladores.trabalho3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabelaDeSimbolos {
    /*
    public class TipoLA {
        List<String> tipos;
        
        private TipoLA() {
            this.tipos = new ArrayList<>();
            this.tipos.add("inteiro");
            this.tipos.add("real");
            this.tipos.add("literal");
        }
        
        public void addTipo(String tipo) {
            this.tipos.add(tipo);
        }
        
        public List<String> getTipos() {
            return this.tipos;
        }
    } */
    
    public enum TipoLA {
        INTEIRO,
        REAL,
        LITERAL,
        INVALIDO
    }
    
    class EntradaTabelaDeSimbolos {
        String nome;
        TipoLA tipo;
        
        private EntradaTabelaDeSimbolos(String nome, TipoLA tipo) {
            this.nome = nome;
            this.tipo = tipo;
        }
    }
    
    private final Map<String, EntradaTabelaDeSimbolos> tabela;
//    private TipoLA tiposDaTabela;
    
    public TabelaDeSimbolos() {
        this.tabela = new HashMap();
//        this.tiposDaTabela = new TipoLA();
    }
    
    public void adicionar(String nome, TipoLA tipo) {
        tabela.put(nome, new EntradaTabelaDeSimbolos(nome, tipo));
    }
    
    public boolean existe(String nome) {
        return tabela.containsKey(nome);
    }
    
    public TipoLA verificar(String nome) {
        return tabela.get(nome).tipo;
    }
    
//    public boolean existeTipo(String tipo) {
//        return tiposDaTabela.getTipos().contains(tipo);
//    }
}
