using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GameBoard : MonoBehaviour {
    [SerializeField]
    Transform ground = default;

    [SerializeField]
    GameTile tilePrefab = default;

    [SerializeField]
    Texture2D gridTexture = default;

    Vector2Int size;

    GameTile[] tiles;

    // bfs para encontrar o caminho de cada tile até o destino
    Queue<GameTile> searchFrontier = new Queue<GameTile>();

    GameTileContentFactory contentFactory;

    bool showPaths, showGrid;

    List<GameTile> spawnPoints = new List<GameTile>();

    List<GameTileContent> updatingContent = new List<GameTileContent>();

    // Métodos Públicos
    public int SpawnPointCount => spawnPoints.Count;

    public void Initialize(Vector2Int size, GameTileContentFactory contentFactory) {
        // inicializa o mapa
        this.size = size;
        this.contentFactory = contentFactory;
        ground.localScale = new Vector3(size.x, size.y, 1f);
        tiles = new GameTile[size.x * size.y];

        Vector2 offset = new Vector2(
            (size.x - 1) * 0.5f, (size.y - 1) * 0.5f
        );
        for (int y = 0, i = 0; y < size.y; y++) {
            for (int x = 0; x < size.x; x++, i++) {
                GameTile tile = tiles[i] = Instantiate(tilePrefab);
                tile.transform.SetParent(transform, false);
                // define as posições
                tile.transform.localPosition = new Vector3(
                    x - offset.x, 0f, y - offset.y
                );
                if (x > 0) {
                    GameTile.MakeEastWestNeighbors(tile, tiles[i - 1]);
                }
                if (y > 0) {
                    GameTile.MakeNorthSouthNeighbors(tile, tiles[i - size.x]);
                }
                tile.isAlternative = (x & 1) == 0; // é par
                if ((y & 1) == 0) {
                    tile.isAlternative = !tile.isAlternative;
                }
                // define o conteúdo
            }
        }
        Clear();
    }

    public void GameUpdate() {
        // atualiza o estado de dos objetos em updatingContent
        for (int i = 0; i < updatingContent.Count; i++) {
            updatingContent[i].GameUpdate();
        }
    }

    public GameTile GetTile(Ray ray) {
        if (Physics.Raycast(ray, out RaycastHit hit, float.MaxValue, 1)) {
            int x = (int)(hit.point.x + size.x * 0.5f);
            int y = (int)(hit.point.z + size.y * 0.5f);
            if (x >= 0 && x < size.x && y >= 0 && y < size.y) {
                // está dentro dos limites do mapa
                return tiles[x + y * size.x];
            }
        }
        return null;
    }

    public void ToggleDestination(GameTile tile) {
        // alterna entre destino e vazio
        if (tile.Content.Type == GameTileContentType.Destination) {
            tile.Content = contentFactory.Get(GameTileContentType.Empty);
            if (!FindPaths()) {
                // ao remover um destino o estado do mapa ficou inválido,
                // retorna ao estado anterior
                tile.Content = contentFactory.Get(GameTileContentType.Destination);
                FindPaths();
            }
        }
        else if (tile.Content.Type == GameTileContentType.Empty) {
            tile.Content = contentFactory.Get(GameTileContentType.Destination);
            FindPaths();
        }
    }

    public void ToggleWall(GameTile tile) {
        // alterna entre vazio e parede
        if (tile.Content.Type == GameTileContentType.Wall) {
            tile.Content = contentFactory.Get(GameTileContentType.Empty);
            FindPaths();
        }
        else if (tile.Content.Type == GameTileContentType.Empty) {
            tile.Content = contentFactory.Get(GameTileContentType.Wall);
            if (!FindPaths()) {
                // ao adicionar uma parede o estado do mapa ficou inválido,
                // retorna ao estado anterior
                tile.Content = contentFactory.Get(GameTileContentType.Empty);
                FindPaths();
            }
        }
    }

    public void ToggleSpawnPoint(GameTile tile) {
        // alterna entre vazio e spawn point
        if (tile.Content.Type == GameTileContentType.SpawnPoint) {
            if (spawnPoints.Count > 1) {
                spawnPoints.Remove(tile);
                tile.Content = contentFactory.Get(GameTileContentType.Empty);
            }
        }
        else if (tile.Content.Type == GameTileContentType.Empty) {
            tile.Content = contentFactory.Get(GameTileContentType.SpawnPoint);
            spawnPoints.Add(tile);
        }
    }

    public void ToggleTower(GameTile tile, TowerType towerType) {
        // alterna entre vazio/parede e torre 
        if (tile.Content.Type == GameTileContentType.Tower) {
            updatingContent.Remove(tile.Content);
            if (((Tower)tile.Content).TowerType == towerType) {
                tile.Content = contentFactory.Get(GameTileContentType.Empty);
                FindPaths();
            }
            else {
                tile.Content = contentFactory.Get(towerType);
                updatingContent.Add(tile.Content);
            }
        }
        else if (tile.Content.Type == GameTileContentType.Empty) {
            tile.Content = contentFactory.Get(towerType);
            if (FindPaths()) {
                // ao adicionar uma parede o estado do mapa ficou inválido,
                // retorna ao estado anterior
                updatingContent.Add(tile.Content);
            }
            else {
                tile.Content = contentFactory.Get(GameTileContentType.Empty);
                FindPaths();
            }
        }
        else if (tile.Content.Type == GameTileContentType.Wall) {
            tile.Content = contentFactory.Get(towerType);
            updatingContent.Add(tile.Content);
        }
    }

    public bool ShowPaths {
        get => showPaths;
        set {
            showPaths = value;
            if (showPaths) {
                foreach (GameTile tile in tiles) {
                    tile.ShowPath();
                }
            }
            else {
                foreach (GameTile tile in tiles) {
                    tile.HidePath();
                }
            }
        }
    }

    public bool ShowGrid {
        // ativa o contorno de cada tile no mapa
        get => showGrid;
        set {
            showGrid = value;
            Material m = ground.GetComponent<MeshRenderer>().material;
            if (showGrid) {
                m.mainTexture = gridTexture;
                m.SetTextureScale("_MainTex", size);
            }
            else {
                m.mainTexture = null;
            }
        }
    }

    public GameTile GetSpawnPoint(int index) {
        return spawnPoints[index];
    }

    public void Clear() {
        foreach (GameTile tile in tiles) {
            tile.Content = contentFactory.Get(GameTileContentType.Empty);
        }
        spawnPoints.Clear();
        updatingContent.Clear();
        ToggleDestination(tiles[tiles.Length / 2]);
        ToggleSpawnPoint(tiles[0]);
    }

    // Métodos Privados
    bool FindPaths() {
        // encontra o caminho de cada tile ao destino
        foreach (GameTile tile in tiles) {
            if (tile.Content.Type == GameTileContentType.Destination) {
                tile.BecomeDestination();
                searchFrontier.Enqueue(tile);
            }
            else {
                tile.ClearPath();
            }
        }
        if (searchFrontier.Count == 0) {
            // não existem destinos
            return false;
        }
        while (searchFrontier.Count > 0) {
            // visita cada vizinho e adiciona na lista de não visitados
            GameTile tile = searchFrontier.Dequeue();
            if (tile != null) {
                if (tile.isAlternative) {
                    searchFrontier.Enqueue(tile.GrowPathNorth());
                    searchFrontier.Enqueue(tile.GrowPathSouth());
                    searchFrontier.Enqueue(tile.GrowPathEast());
                    searchFrontier.Enqueue(tile.GrowPathWest());
                }
                else {
                    searchFrontier.Enqueue(tile.GrowPathWest());
                    searchFrontier.Enqueue(tile.GrowPathEast());
                    searchFrontier.Enqueue(tile.GrowPathSouth());
                    searchFrontier.Enqueue(tile.GrowPathNorth());
                }
            }
        }
        foreach (GameTile tile in tiles) {
            // && tile.Content.Type != GameTileContentType.Wall
            if (!tile.HasPath) {
                return false;
            }
        }
        if (showPaths) {
            foreach (GameTile tile in tiles) {
                tile.ShowPath();
            }
        }
        return true;
    }
}
