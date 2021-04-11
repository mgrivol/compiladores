package br.ufscar.dc.compiladores.trabalho4.SemanticoUtils;

public class Inimigo {

    private String nome;
    private String modelo;
    private int forca;
    private float vida;
    private float velocidade;
    
    public Inimigo(String nome) {
        this.nome = nome;
        // valores padrões podem ou não serem alterados
        this.modelo = "MEDIO";
        this.forca = 10;
        this.vida = 100f;
        this.velocidade = 1f;
    }

    public String getModelo() {
        switch (modelo) {
            case "PEQUENO":
                return "EnemyType.Small";
            case "MEDIO":
                return "EnemyType.Medium";
            case "GRANDE":
                return "EnemyType.Large";
        }
        return modelo;
    }

    public int getForca() {
        return forca;
    }

    public float getVida() {
        return vida;
    }

    public float getVelocidade() {
        return velocidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setForca(int forca) {
        this.forca = forca;
    }

    public void setVida(float vida) {
        this.vida = vida;
    }

    public void setVelocidade(float velocidade) {
        this.velocidade = velocidade;
    }
}
