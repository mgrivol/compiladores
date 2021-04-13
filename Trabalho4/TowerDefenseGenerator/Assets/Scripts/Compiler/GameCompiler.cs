using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GameCompiler : MonoBehaviour {

    [SerializeField]
    Vector2Int boardSize = new Vector2Int(11, 11);

    [SerializeField]
    GameTileContentFactory tileContentFactory = default;

    [SerializeField]
    GameBoard board = default;

    [SerializeField]
    EnemyFactory enemyFactory = default;

    GameScenarioCompiler scenario = default;
    GameScenarioCompiler.State activeScenario;

    [SerializeField]
    WarFactory warFactory = default;

    [SerializeField, Range(0, 100)]
    // 0 é vida infinita para testes
    int startingPlayerHealth = 10;

    [SerializeField, Range(1f, 10f)]
    float playSpeed = 1f; // velocidade padrão

    GameBehaviorCollection enemies = new GameBehaviorCollection();
    GameBehaviorCollection nonEnemies = new GameBehaviorCollection();

    TowerType selectedTowerType;

    static GameCompiler instance;

    int playerHealth;
    const float pausedTimeScale = 0f;

    // métodos padrões
    void Awake() {
        playerHealth = startingPlayerHealth;
        board.Initialize(boardSize, tileContentFactory);
        board.ShowGrid = true;

        scenario = new GameScenarioCompiler(enemyFactory);
        activeScenario = scenario.Begin();
    }

    void Update() {
        if (Input.GetMouseButtonDown(0)) {
            HandleTouch();
        }
        else if (Input.GetMouseButtonDown(1)) {
            HandleAlternativeTouch();
        }

        if (Input.GetKeyDown(KeyCode.V)) {
            board.ShowPaths = !board.ShowPaths;
        }

        if (Input.GetKeyDown(KeyCode.Alpha1)) {
            selectedTowerType = TowerType.Laser;
        }
        else if (Input.GetKeyDown(KeyCode.Alpha2)) {
            selectedTowerType = TowerType.Mortar;
        }

        if (Input.GetKeyDown(KeyCode.Space)) {
            // "espaço" pausa o jogo
            Time.timeScale = Time.timeScale > pausedTimeScale ? pausedTimeScale : 1f;
        }
        else if (Time.timeScale > pausedTimeScale) {
            Time.timeScale = playSpeed;
        }

        if (Input.GetKeyDown(KeyCode.B)) {
            // "b" começa um novo jogo
            playerHealth = startingPlayerHealth;
            BeginNewGame();
        }
        if (startingPlayerHealth == 0 && !activeScenario.Progress()
            && enemies.IsEmpty) {
                // modo invencivel
            playerHealth = startingPlayerHealth;
            enemies.Clear();
            activeScenario = scenario.Begin();
        }
        else if (playerHealth <= 0 && startingPlayerHealth > 0) {
            // sem vida = fim de jogo
            Debug.Log("Derrota!");
            BeginNewGame();
        }
        else if (!activeScenario.Progress() && enemies.IsEmpty) {
            // cenário acabou e não existem inimigos = vitória
            Debug.Log("Vitoria!");
            BeginNewGame();
            activeScenario.Progress();
        }

        activeScenario.Progress();

        enemies.GameUpdate();
        Physics.SyncTransforms();
        board.GameUpdate();
        nonEnemies.GameUpdate();
    }

    void OnEnable() {
        instance = this;
    }

    // métodos públicos
    public static Shell SpawnShell() {
        // criar um projétil
        Shell shell = instance.warFactory.Shell;
        instance.nonEnemies.Add(shell);
        return shell;
    }

    public static Explosion SpawnExplosion() {
        // cria uma explosão
        Explosion explosion = instance.warFactory.Explosion;
        instance.nonEnemies.Add(explosion);
        return explosion;
    }

    public static void SpawnEnemy(EnemyFactory factory, EnemyType type) {
        // lida com nascer inimigos
        GameTile spawnPoint = instance.board.GetSpawnPoint(
            Random.Range(0, instance.board.SpawnPointCount)
        );
        Enemy enemy = factory.Get(type);
        enemy.SpawnOn(spawnPoint);
        instance.enemies.Add(enemy);
    }

    public static void SpawnEnemy(
        EnemyFactory factory, EnemyConfig enemyConfig
    ) {
        // lida com nascer inimigos
        GameTile spawnPoint = instance.board.GetSpawnPoint(
            Random.Range(0, instance.board.SpawnPointCount)
        );
        Enemy enemy = factory.Get(enemyConfig.modelo);
        enemy.UpdateAttributes(enemyConfig.vida, enemyConfig.velocidade, enemyConfig.forca);
        enemy.SpawnOn(spawnPoint);
        instance.enemies.Add(enemy);
    }

    public static void EnemyReachedDestination(int strength) {
        // inimigos chamam esta função para avisar que chegaram ao destino
        instance.playerHealth -= strength;
    }

    // métodos privados
    Ray TouchRay => Camera.main.ScreenPointToRay(Input.mousePosition);

    void HandleTouch() {
        // lida com os botões
        GameTile tile = board.GetTile(TouchRay);
        if (tile != null) {
            if (Input.GetKey(KeyCode.LeftShift)) {
                board.ToggleTower(tile, selectedTowerType);
            }
            else {
                board.ToggleWall(tile);
            }
        }
    }

    void HandleAlternativeTouch() {
        // lida com os botões alternativos
        GameTile tile = board.GetTile(TouchRay);
        if (tile != null) {
            if (Input.GetKey(KeyCode.LeftShift)) {
                board.ToggleDestination(tile);
            }
            else {
                board.ToggleSpawnPoint(tile);
            }
        }
    }

    void BeginNewGame() {
        playerHealth = startingPlayerHealth;
        enemies.Clear();
        nonEnemies.Clear();
        board.Clear();
        activeScenario = scenario.Begin();
    }

    void OnValidate() {
        if (boardSize.x < 2) {
            boardSize.x = 2;
        }
        if (boardSize.y < 2) {
            boardSize.y = 2;
        }
    }
}
