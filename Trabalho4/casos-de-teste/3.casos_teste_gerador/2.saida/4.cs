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
   EnemyConfig A = new EnemyConfig(100.0f, 2.0f, 20, EnemyType.Small);

    public GameScenarioCompiler(EnemyFactory factory) {
        this.factory = factory;
        waves = new EnemyWaveCompiler[2];
        waves[0] = new EnemyWaveCompiler(factory, 2);
        waves[0].setSpawnSequence(0, 1f, 10, factory, A);
        waves[0].setSpawnSequence(1, 10.0f, 1, factory, Delay);
        waves[1] = new EnemyWaveCompiler(factory, 3);
        waves[1].setSpawnSequence(0, 1f, 10, factory, A);
        waves[1].setSpawnSequence(1, 7.5f, 1, factory, Delay);
        waves[1].setSpawnSequence(2, 1f, 20, factory, A);
  }
}

