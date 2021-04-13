using UnityEngine;

public class EnemyWaveCompiler {
    // uma onda é uma sequência de inimigos para nascer

    EnemyFactory factory;

    EnemySpawnSequenceCompiler[] spawnSequences;

    public State Begin() => new State(this);

    public struct State {

        EnemyWaveCompiler wave;

        int index;

        EnemySpawnSequenceCompiler.State sequence;

        public State(EnemyWaveCompiler wave) {
            this.wave = wave;
            index = 0;
            Debug.Assert(wave.spawnSequences.Length > 0, "Empty wave!");
            sequence = wave.spawnSequences[0].Begin();
        }

        public float Progress(float deltaTime) {
            deltaTime = sequence.Progress(deltaTime);
            while (deltaTime >= 0f) {
                if (++index >= wave.spawnSequences.Length) {
                    return deltaTime;
                }
                sequence = wave.spawnSequences[index].Begin();
                deltaTime = sequence.Progress(deltaTime);
            }
            return -1f;
        }
    }

    public EnemyWaveCompiler(EnemyFactory factory, int enemiesInSequenceCount) {
        this.factory = factory;
        spawnSequences = new EnemySpawnSequenceCompiler[enemiesInSequenceCount];
    }

    public void setSpawnSequence(
        int index, float aguarde, int qtd, EnemyFactory factory
    ) {
        spawnSequences[index] = new EnemySpawnSequenceCompiler(
            aguarde, qtd, factory
        );
    }

    public void setSpawnSequence(
        int index, float aguarde, int qtd, EnemyFactory factory, EnemyConfig enemy
    ) {
        spawnSequences[index] = new EnemySpawnSequenceCompiler(
            aguarde, qtd, factory, enemy
        );
    }
}