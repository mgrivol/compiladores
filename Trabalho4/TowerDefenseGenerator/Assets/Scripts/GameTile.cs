using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GameTile : MonoBehaviour {

    [SerializeField]
    Transform arrow = default;

    GameTile north, east, south, west, nextOnPath;

    int distance;

    static Quaternion
        northRotation = Quaternion.Euler(90f, 0f, 0f),
        eastRotation = Quaternion.Euler(90f, 90f, 0f),
        southRotation = Quaternion.Euler(90f, 180f, 0f),
        westRotation = Quaternion.Euler(90f, 270f, 0f);

    GameTileContent content;

    // Métodos Públicos
    public bool HasPath => distance != int.MaxValue;

    public GameTile GrowPathNorth() => GrowPathTo(north, Direction.South);

    public GameTile GrowPathEast() => GrowPathTo(east, Direction.West);

    public GameTile GrowPathSouth() => GrowPathTo(south, Direction.North);

    public GameTile GrowPathWest() => GrowPathTo(west, Direction.East);

    public GameTile NextTileOnPath => nextOnPath;

    public bool isAlternative { get; set; }

    public Vector3 ExitPoint { get; private set; }

    public Direction PathDirection { get; private set; }

    public static void MakeEastWestNeighbors(GameTile east, GameTile west) {
        // define os vizinhos laterais
        Debug.Assert(
            west.east == null && east.west == null, "Redefined neighbors!"
        );
        west.east = east;
        east.west = west;
    }

    public static void MakeNorthSouthNeighbors(GameTile north, GameTile south) {
        // define o vizinho superior e inferior
        Debug.Assert(
            south.north == null && north.south == null, "Redefined neighbors!"
        );
        south.north = north;
        north.south = south;
    }

    public void ClearPath() {
        // limpa o caminho
        distance = int.MaxValue;
        nextOnPath = null;
    }

    public void BecomeDestination() {
        // torna esta tile destino
        distance = 0;
        nextOnPath = null;
        ExitPoint = transform.localPosition;
    }

    public void ShowPath() {
        // mostra os caminhos
        if (distance == 0) {
            // é objetivo
            arrow.gameObject.SetActive(false);
            return;
        }
        arrow.gameObject.SetActive(true);
        arrow.localRotation =
            nextOnPath == north ? northRotation :
            nextOnPath == east ? eastRotation :
            nextOnPath == south ? southRotation :
            westRotation;
    }

    public void HidePath() {
        // esconde os caminhos
        arrow.gameObject.SetActive(false);
    }

    public GameTileContent Content {
        get => content;
        set {
            Debug.Assert(value != null, "Null assigned to content!");
            if (content != null) {
                content.Recycle();
            }
            content = value;
            content.transform.localPosition = transform.localPosition;
        }
    }

    // Métodos Privados
    GameTile GrowPathTo(GameTile neighbor, Direction direction) {
        // cadiciona dados no vizinho
        Debug.Assert(HasPath, "No Path!");
        if (neighbor == null || neighbor.HasPath) {
            return null;
        }
        neighbor.distance = distance + 1;
        neighbor.nextOnPath = this;
        neighbor.ExitPoint = neighbor.transform.localPosition + direction.GetHalfVector();
        neighbor.PathDirection = direction;
        return neighbor.Content.BlocksPath ? null : neighbor;
    }
}
