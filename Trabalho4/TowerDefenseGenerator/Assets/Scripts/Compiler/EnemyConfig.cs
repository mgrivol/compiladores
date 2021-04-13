using UnityEngine;

public class EnemyConfig {

    public float vida;
    public float velocidade;
    public int forca;
    public EnemyType modelo;

    public EnemyConfig(float vida, float velocidade, int forca, EnemyType modelo) {
        this.vida = vida;
        this.velocidade = velocidade;
        this.forca = forca;
        this.modelo = modelo;
    }
}