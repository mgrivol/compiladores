/*
using UnityEngine;

public class GameScenarioCompiler {

    EnemyFactory factory;

    EnemyWaveCompiler[] waves;

    public State Begin() => new State(this);

    public struct State {

        GameScenarioCompiler scenario;

        int index;

        EnemyWaveCompiler.State wave;

        public State(GameScenarioCompiler scenario) {
            this.scenario = scenario;
            index = 0;
            Debug.Assert(scenario.waves.Length > 0, "Empty scenario!");
            wave = scenario.waves[0].Begin();
        }

        public bool Progress() {
            // verifica o progresso do cenário
            float deltaTime = wave.Progress(Time.deltaTime);
            while (deltaTime >= 0f) {
                if (++index >= scenario.waves.Length) {
                    return false;
                }
                wave = scenario.waves[index].Begin();
                deltaTime = wave.Progress(deltaTime);
            }
            return true;
        }
    }

    EnemyConfig A = new EnemyConfig(100f, 1.5f, 10, EnemyType.Small);
    EnemyConfig B = new EnemyConfig(300f, 1f, 15, EnemyType.Medium);
    EnemyConfig Delay = new EnemyConfig(0f, 0f, 0, EnemyType.Delay);

    public GameScenarioCompiler(EnemyFactory factory) {
        this.factory = factory;
        waves = new EnemyWaveCompiler[1];
        waves[0] = new EnemyWaveCompiler(factory, 3);
        waves[0].setSpawnSequence(0, 1f, 10, factory, A);
        waves[0].setSpawnSequence(1, 10f, 1, factory, Delay);
        waves[0].setSpawnSequence(2, 1f, 5, factory, B);
    }
}
*/
using UnityEngine;

public class GameScenarioCompiler {

    EnemyFactory factory;
    EnemyWaveCompiler[] waves;
    public State Begin() => new State(this);

    public struct State {

        GameScenarioCompiler scenario;
        int index;
        EnemyWaveCompiler.State wave;

        public State(GameScenarioCompiler scenario) {
            this.scenario = scenario;
            index = 0;
            Debug.Assert(scenario.waves.Length > 0, "Empty scenario!");
            wave = scenario.waves[0].Begin();
        }

        public bool Progress() {
            float deltaTime = wave.Progress(Time.deltaTime);
            while (deltaTime >= 0f) {
                if (++index >= scenario.waves.Length) {
                    return false;
                }
                wave = scenario.waves[index].Begin();
                deltaTime = wave.Progress(deltaTime);
            }
            return true;
        }
    }
   EnemyConfig Delay = new EnemyConfig(0f, 0f, 0, EnemyType.Delay);
   EnemyConfig A = new EnemyConfig(100.0f, 3.9f, 4, EnemyType.Small);
   EnemyConfig B = new EnemyConfig(350.0f, 1.75f, 10, EnemyType.Medium);
   EnemyConfig C = new EnemyConfig(500.0f, 1f, 30, EnemyType.Large);

    public GameScenarioCompiler(EnemyFactory factory) {
        this.factory = factory;
        waves = new EnemyWaveCompiler[2];
        waves[0] = new EnemyWaveCompiler(factory, 2);
        waves[0].setSpawnSequence(0, 1f, 5, factory, A);
        waves[0].setSpawnSequence(1, 1.0f, 3, factory, B);
        waves[1] = new EnemyWaveCompiler(factory, 4);
        waves[1].setSpawnSequence(0, 0.5f, 6, factory, A);
        waves[1].setSpawnSequence(1, 10.0f, 1, factory, Delay);
        waves[1].setSpawnSequence(2, 1.75f, 5, factory, B);
        waves[1].setSpawnSequence(3, 2f, 3, factory, C);
  }
}
