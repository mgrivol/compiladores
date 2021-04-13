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
   EnemyConfig A = new EnemyConfig(100.0f, 1.0f, 20, EnemyType.Medium);

    public GameScenarioCompiler(EnemyFactory factory) {
        this.factory = factory;
        waves = new EnemyWaveCompiler[1];
        waves[0] = new EnemyWaveCompiler(factory, 1);
        waves[0].setSpawnSequence(0, 1.5f, 10, factory, A);
  }
}

