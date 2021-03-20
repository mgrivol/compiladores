package br.ufscar.dc.compiladores.trabalho3.semanticoUtils;

import java.util.ArrayList;
import java.util.List;

public class Variavel {

    public String nome;
    public TipoLA tipo;
    Ponteiro ponteiro;
    Registro registro;
    Procedimento procedimento;
    Funcao funcao;
    
    // TEMPORARIO
    public String tipoToString() {
        if (tipo.tipoBasico != null) {
            return "" + tipo.tipoBasico;
        } else if (tipo.tipoCriado != null) {
            return "" + tipo.tipoCriado;
        }
        return "OS DOIS SAO NULOS";
    }
    // TEMPORARIO
    public String dados() {
        String ponteiro = "";
        if (this.ponteiro != null) {
            ponteiro += this.getTipoPonteiroAninhado().tipoBasico;
        }
        if (this.tipo.tipoBasico == TipoLA.TipoBasico.REGISTRO) {
            System.out.println("------imprimindo variaveis do registro");
            for (var v : this.registro.variaveis) {
                System.out.println("       v-reg->" + v.dados());
            }
            System.out.println("-------acabou, o registro");
        }
        return "<" + nome + ", " + tipoToString() + ">" + "->" + ponteiro;
    }
    
    public Variavel(String nome, TipoLA tipo) {
        this.nome = nome;
        this.tipo = tipo;
        if (this.tipo != null && this.tipo.tipoBasico != null) {
            switch (this.tipo.tipoBasico) {
                case REGISTRO:
                    // esta variável é um registro
                    this.registro = new Registro();
                    break;
                case PONTEIRO:
                    // esta variável é um ponteiro
                    this.ponteiro = new Ponteiro(this.tipo.tipoAninhado);
                    break;
                case FUNCAO:
                    // esta variável é uma função
                    this.funcao = new Funcao();
                    break;
                case PROCEDIMENTO:
                    // esta variável é um procedimento
                    this.procedimento = new Procedimento();
                    break;
                default:
                    break;
            }
        }
    }
    
    public class Ponteiro {
        private TipoLA apontaPara;
        
        public Ponteiro (TipoLA apontaPara) {
            this.apontaPara = apontaPara;
        }
        
        public TipoLA getTipoAninhado() {
            
            return this.apontaPara.getTipoAninhado();
        }
    }

    public class Registro {
        private List<Variavel> variaveis;
        
        public Registro() {
            this.variaveis = new ArrayList<>();
        }
        
        public void adicionaNoRegistro(List<Variavel> novasVars) {
            this.variaveis.addAll(novasVars);
        }
        
        public Variavel getVariavel (String nome) {
            for (var v : this.variaveis) {
                if (v.nome.equals(nome)) {
                    return v;
                }
            }
            return null;
        }
    }
    
    public class Funcao {
        private TipoLA tipoRetorno;
        private List<Variavel> variaveisLocais;
        private List<Variavel> parametros;
        
        public void setTipoRetorno(TipoLA tipoRetorno) {
            this.tipoRetorno = tipoRetorno;
        }
        
        public void setVariaveisLocais(List<Variavel> vars) {
            this.variaveisLocais = vars;
        }
        
        public void setParametros(List<Variavel> vars) {
            this.parametros = vars;
        }
        
        public TipoLA getTipoRetorno() {
            return this.tipoRetorno;
        }
        
        public List<Variavel> getParametros() {
            return this.parametros;
        }
        
        public List<Variavel> getVariaveisLocais() {
            return this.variaveisLocais;
        }
    }

    public class Procedimento {
        private List<Variavel> variaveisLocais;
        private List<Variavel> parametros;
        
        public void setVariaveisLocais(List<Variavel> vars) {
            this.variaveisLocais = vars;
        }
        
        public void setParametros(List<Variavel> vars) {
            this.parametros = vars;
        }
        
        public List<Variavel> getParametros() {
            return this.parametros;
        }
        
        public List<Variavel> getVariaveisLocais() {
            return this.variaveisLocais;
        }
    }

    // ===== Getters & Setters =====
    public TipoLA getTipoPonteiroAninhado() {
        return this.ponteiro.getTipoAninhado();
    }
    
    public Variavel getVarNoRegistro(String nome) {
        return this.registro.getVariavel(nome);
    }

    public Registro getRegistro() {
        return registro;
    }
    
    public void setRegistro(Registro registro) {
        this.registro = registro;
    }
}
