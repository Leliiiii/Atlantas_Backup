package engine;

import factory.EntityFactory;
import units.Unit;
import java.util.List;
import java.util.Random;

public class UnitSpawner {
    private final EntityFactory factory;
    private final Random random;
    private double enemySpawnTimer = 0;
    private double enemySpawnInterval = 4.0;
    private int enemySpawnCount = 0;

    private final String[] enemyTypes = {
        "gondolier", "mask", "patrician", "childswarm", "friar"
    };

    public UnitSpawner() {
        this.factory = EntityFactory.getInstance();
        this.random = new Random();
    }

    public Unit spawnPlayerUnit(String type, double laneY) {
        Unit unit = factory.createUnit(type, 100, laneY, "player");
        return unit;
    }

    public void update(double deltaTime, List<Unit> enemyUnits, double laneY, double screenWidth) {
        enemySpawnTimer += deltaTime;

        if (enemySpawnTimer >= enemySpawnInterval) {
            enemySpawnTimer = 0;
            spawnEnemy(enemyUnits, laneY, screenWidth);
            enemySpawnCount++;

            //TODO AUMENTARE LA DIFFICOLTA' SERVE UN CONTROLLO /LO FACCIO IO ZAINAB, LASCIAMI LA LOGICA
        }
    }

    private void spawnEnemy(List<Unit> enemyUnits, double laneY, double screenWidth) {
        String type = enemyTypes[random.nextInt(enemyTypes.length)];

        // Boss spawn chance after 20 spawns
        if (enemySpawnCount > 20 && random.nextDouble() < 0.05) {
            type = "bossdoge";
        }

        Unit enemy = factory.createUnit(type, screenWidth - 150, laneY, "enemy");
        enemyUnits.add(enemy);
    }

    public void reset() {
        enemySpawnTimer = 0;
        enemySpawnInterval = 4.0;
        enemySpawnCount = 0;
    }
}
