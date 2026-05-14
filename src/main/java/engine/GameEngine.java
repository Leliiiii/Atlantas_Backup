package engine;

import buildings.EnemyTower;
import buildings.PlayerTower;
import buildings.Tower;
import factory.EntityFactory;
import javafx.scene.paint.Color;
import observer.Observer;
import observer.Subject;
import projectiles.Projectile;
import resources.ResourceManager;
import units.SeahorseUnit;
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

        resourceManager.update(deltaTime);
        spawner.update(deltaTime, enemyUnits, laneY, screenWidth);

        updateUnits(deltaTime, playerUnits, enemyUnits, enemyTower);
        updateUnits(deltaTime, enemyUnits, playerUnits, playerTower);

        updateProjectiles(deltaTime);

        checkGameOver();
    }

    private void updateUnits(double deltaTime, List<Unit> allies, List<Unit> enemies, Tower enemyTower) {
        Iterator<Unit> iterator = allies.iterator();
        while (iterator.hasNext()) {
            Unit unit = iterator.next();
            if (!unit.isActive()) {
                iterator.remove();
                continue;
            }

            unit.update(deltaTime);

            // Healer units special behavior
            if (unit instanceof SeahorseUnit) {
                ((SeahorseUnit) unit).healAllies(allies);
            }

            // Find target
            Unit target = unit.findTarget(enemies);

            if (target != null && unit.isInRange(target)) {
                unit.setMoving(false);
                unit.setTarget(target);

                // Ranged units shoot projectiles
                if (unit.getRange() > 100) {
                    if (unit.getAttackTimer() <= 0) {
                        spawnProjectile(unit, target);
                    }
                } else {
                    unit.attack(target);
                }
            } else {
                // No target in range, move towards enemy tower
                unit.setMoving(true);
                unit.setTarget(null);
                unit.move(deltaTime);

                // Check if reached enemy tower
                if (isUnitAtTower(unit, enemyTower)) {
                    enemyTower.takeDamage(unit.getDamage() * deltaTime);
                    unit.setMoving(false);
                }
            }
        }
    }

    private void spawnProjectile(Unit shooter, Unit target) {
        Projectile p = EntityFactory.getInstance().createProjectile(
            shooter.getCenterX(), shooter.getCenterY(),
            shooter.getDamage(), 5.0, target, shooter.getTeam()
        );
        projectiles.add(p);
    }

    private void updateProjectiles(double deltaTime) {
        Iterator<Projectile> iterator = projectiles.iterator();
        while (iterator.hasNext()) {
            Projectile p = iterator.next();
            p.update(deltaTime);
            if (!p.isActive()) {
                EntityFactory.getInstance().returnProjectile(p);
                iterator.remove();
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

    private void checkGameOver() {
        if (enemyTower.isDestroyed()) {
            gameOver = true;
            playerWon = true;
            gameStateSubject.notifyObservers("game_over", true);
        } else if (playerTower.isDestroyed()) {
            gameOver = true;
            playerWon = false;
            gameStateSubject.notifyObservers("game_over", false);
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
