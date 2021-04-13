using UnityEngine;

public class EnemySpawnSequenceCompiler {
    // sequência de inimigos para nascer

    int amount = 1;
    float cooldown = 1f;
    EnemyFactory factory = default;

    // Configurações do inimigo
    EnemyConfig enemy = null;

    public State Begin() => new State(this);

    public struct State {

        EnemySpawnSequenceCompiler sequence;

        int count;
        float cooldown;

        public State(EnemySpawnSequenceCompiler sequence) {
            this.sequence = sequence;
            count = 0;
            cooldown = sequence.cooldown;
        }

        public float Progress(float deltaTime) {
            cooldown += deltaTime;
            while (cooldown >= sequence.cooldown) {
                cooldown -= sequence.cooldown;
                if (count >= sequence.amount) {
                    return cooldown;
                }
                count += 1;
                if (sequence.enemy != null) {
                    GameCompiler.SpawnEnemy(sequence.factory, sequence.enemy);
                }
                else {
                    // inimigo default medium
                    GameCompiler.SpawnEnemy(sequence.factory, EnemyType.Medium);
                }
			}
            return -1f;
        }
    }

    public EnemySpawnSequenceCompiler(
        float aguarde, int qtd, EnemyFactory factory
    ) {
        this.cooldown = aguarde;
        this.amount = qtd;
        this.factory = factory;
    }

    public EnemySpawnSequenceCompiler( 
        float aguarde, int qtd, EnemyFactory factory, 
        EnemyConfig enemy
    ) {
        this.enemy = enemy;
        this.cooldown = aguarde;
        this.amount = qtd;
        this.factory = factory;
    }
}