﻿using System.Collections.Generic;

public class GameBehaviorCollection {

    List<GameBehavior> behaviors = new List<GameBehavior>();

    public bool IsEmpty => behaviors.Count == 0;

    public void Add(GameBehavior behavior) {
        // comportamento de inimigos e projéteis
        behaviors.Add(behavior);
    }

    public void GameUpdate() {
        // remove inimigos mortos
        for (int i = 0; i < behaviors.Count; i++) {
            if (!behaviors[i].GameUpdate()) {
                int lastIndex = behaviors.Count - 1;
                behaviors[i] = behaviors[lastIndex];
                behaviors.RemoveAt(lastIndex);
                i -= 1;
            }
        }
    }

    public void Clear() {
        for (int i = 0; i < behaviors.Count; i++) {
            behaviors[i].Recycle();
        }
        behaviors.Clear();
    }
}
