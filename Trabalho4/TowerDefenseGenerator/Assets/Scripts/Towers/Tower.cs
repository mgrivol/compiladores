using UnityEngine;

public abstract class Tower : GameTileContent {
    // torre base

    [SerializeField, Range(1.5f, 10.5f)]
    protected float targetingRange = 2f;

    public abstract TowerType TowerType { get; }

    // Métodos Protegidos
    protected bool AcquireTarget(out TargetPoint target) {
        // encontra a posição dos inimigos
        if (TargetPoint.FillBuffer(transform.localPosition, targetingRange)) {
            target = TargetPoint.RandomBuffered;
            return true;
        }
        target = null;
        return false;
    }

    protected bool TrackTarget(ref TargetPoint target) {
        // responsável por manter a mira no alvo
        if (target == null) {
            return false;
        }
        // limita o alcance
        Vector3 a = transform.localPosition;
        Vector3 b = target.Position;
        float x = a.x - b.x;
        float z = a.z - b.z;
        float r = targetingRange + 0.125f * target.Enemy.Scale;
        if (x * x + z * z > r * r) {
            target = null;
            return false;
        }
        return true;
    }

    void OnDrawGizmosSelected() {
        // para debug
        Gizmos.color = Color.yellow;
        Vector3 position = transform.localPosition;
        position.y += 0.01f;
    }
}