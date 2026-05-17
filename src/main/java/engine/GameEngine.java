package engine;

import buildings.EnemyTower;
import buildings.PlayerTower;
import buildings.Tower;
import factory.EntityFactory;
import observer.Observer;
import observer.Subject;
import projectiles.Projectile;
import resources.ResourceManager;
import units.Unit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameEngine {
    private final List<Unit> playerUnits;
    private final List<Unit> enemyUnits;
    private final List<Projectile> projectiles;
    private final PlayerTower playerTower;
    private final EnemyTower enemyTower;
    private final UnitSpawner spawner;
    private final ResourceManager resourceManager;
    private final Subject gameStateSubject;

    private double laneY;
    private double screenWidth;
    private boolean gameOver = false;
    private boolean playerWon = false;

    public GameEngine(double screenWidth, double screenHeight) {
        this.screenWidth = screenWidth;
        this.laneY = screenHeight / 2;

        this.playerUnits = new ArrayList<>();
        this.enemyUnits = new ArrayList<>();
        this.projectiles = new ArrayList<>();

        this.playerTower = new PlayerTower(20, laneY - 60);
        this.enemyTower = new EnemyTower(screenWidth - 100, laneY - 60);

        this.spawner = new UnitSpawner();
        this.resourceManager = ResourceManager.getInstance();
        this.gameStateSubject = new Subject();
    }

    public void update(double deltaTime) {
        if (gameOver) return;

        // Aggiorna risorse e spawner
        resourceManager.update(deltaTime);
        spawner.update(deltaTime, enemyUnits, laneY, screenWidth);

        // Muove gli Unit
        updateUnits(deltaTime, playerUnits, enemyUnits, enemyTower);
        updateUnits(deltaTime, enemyUnits, playerUnits, playerTower);
    }

    private void updateUnits(double deltaTime, List<Unit> allies, List<Unit> enemies, Tower enemyTower) {
        // Ciclo classico sulle unità
        for (int i = 0; i < allies.size(); i++) {
            Unit unit = allies.get(i);

            // Se l unità è morta la rimuovo dalla lista
            if (!unit.isActive()) {
                allies.remove(i);
                i--; // importante per non saltare il prossimo elemento
                continue;
            }

            // Aggiorno logica base unità
            unit.update(deltaTime);

            // TODO gestione unità speciali

            // Cerco un bersaglio tra i nemici
            Unit target = unit.findTarget(enemies);

            if (target != null && unit.isInRange(target)) {
                // Si ferma e attacca
                unit.setMoving(false);
                unit.setTarget(target);

                // TODO DEVO FARE L ATTACCO A DISTAZA
            } else {
                // Se non ha target continua a muoversi
                unit.setMoving(true);
                unit.setTarget(null);
                unit.move(deltaTime);
            }
        }
    }

    private boolean isUnitAtTower(Unit unit, Tower tower) {
        double unitRight = unit.getX() + unit.getWidth();
        double unitLeft = unit.getX();
        double towerRight = tower.getX() + tower.getWidth();
        double towerLeft = tower.getX();

        if (unit.getTeam().equals("player")) {
            return unitRight >= towerLeft;
        } else {
            return unitLeft <= towerRight;
        }
    }

    public void spawnPlayerUnit(String type) {
        if (gameOver) return;

        Unit unit = EntityFactory.getInstance().createUnit(type, 100, laneY, "player");
        if (unit != null && resourceManager.canAfford(unit.getCost())) {
            if (resourceManager.spendEnergy(unit.getCost())) {
                playerUnits.add(unit);
            }
        }
    }

    public void addGameStateObserver(Observer observer) {
        gameStateSubject.attach(observer);
    }

    public void reset() {
        playerUnits.clear();
        enemyUnits.clear();
        projectiles.clear();
        spawner.reset();
        resourceManager.reset();
        gameOver = false;
        playerWon = false;
    }

    public List<Unit> getPlayerUnits() { return playerUnits; }
    public List<Unit> getEnemyUnits() { return enemyUnits; }
    public List<Projectile> getProjectiles() { return projectiles; }
    public PlayerTower getPlayerTower() { return playerTower; }
    public EnemyTower getEnemyTower() { return enemyTower; }
    public boolean isGameOver() { return gameOver; }
    public boolean isPlayerWon() { return playerWon; }
    public double getLaneY() { return laneY; }
}
