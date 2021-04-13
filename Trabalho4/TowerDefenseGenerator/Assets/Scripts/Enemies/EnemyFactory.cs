﻿using UnityEngine;

[CreateAssetMenu]
public class EnemyFactory : GameObjectFactory {
    
    [System.Serializable]
    class EnemyConfig {
        
        [SerializeField]
        public Enemy prefab = default;

        [SerializeField, FloatRangeSlider(0.5f, 2f)]
        public FloatRange scale = new FloatRange(1f);

        [SerializeField, FloatRangeSlider(-0.4f, 0.4f)]
        public FloatRange pathOffset = new FloatRange(0f);

        [SerializeField, FloatRangeSlider(0.2f, 5f)]
        public FloatRange speed = new FloatRange(1f);

        [FloatRangeSlider(0f, 1000f)]
		public FloatRange health = new FloatRange(100f);

        [FloatRangeSlider(1f, 100f)]
        public FloatRange strength = new FloatRange(10f);
    }

    [SerializeField]
    EnemyConfig small = default, medium = default, large = default, delay = default;

    EnemyConfig GetConfig (EnemyType type) {
		switch (type) {
			case EnemyType.Small: return small;
			case EnemyType.Medium: return medium;
			case EnemyType.Large: return large;
            case EnemyType.Delay: return delay;
		}
		Debug.Assert(false, "Unsupported enemy type!");
		return null;
	}

    public Enemy Get(EnemyType type = EnemyType.Medium) {
        EnemyConfig config = GetConfig(type);
        Enemy instance = CreateGameObjectInstance(config.prefab);
        instance.OriginFactory = this;
        instance.Initialize(
            config.scale.RandomValueInRange, 
            config.pathOffset.RandomValueInRange,
            config.speed.RandomValueInRange,
            config.health.RandomValueInRange,
            (int)config.strength.RandomValueInRange
        );
        return instance;
    }

    public void Reclaim(Enemy enemy) {
        Debug.Assert(enemy.OriginFactory == this, "Wrong factory reclaimed!");
        Destroy(enemy.gameObject);
    }
}
