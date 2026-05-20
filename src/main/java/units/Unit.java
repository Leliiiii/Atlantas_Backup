package units;

import entities.Entity;
import javafx.scene.paint.Color;
import observer.Subject;

public abstract class Unit extends Entity {
    protected double health;
    protected double maxHealth;
    protected double atkDamage;
    protected double moveSpeed;
    protected double attackRange;
    protected double atkCooldown;
    protected double atkTimer = 0;
    protected String squadra;
    protected Unit currentTarget;
    protected boolean isMoving = true;
    protected Subject observer;
    protected double spawnCost;

    public Unit(double x, double y, double width, double height, Color color, double hp, double damage, double speed, double range, double attackCooldown, String team, double cost) {
        super(x, y - 50, width, height, color);

        this.health = hp;
        this.maxHealth = hp;
        this.atkDamage = damage;
        this.moveSpeed = speed;
        this.attackRange = range;
        this.atkCooldown = attackCooldown;
        // Squadra dell unita
        this.squadra = team;
        this.spawnCost = cost;
        // Observer per hp ecc
        observer = new Subject();
    }

    @Override
    public void update(double deltaTime) {
        // Se muore si disattiva
        if (!attiva || health <= 0) {
            attiva = false;
            return;
        }

        // Timer attacco
        if (atkTimer > 0) {
            atkTimer = atkTimer - deltaTime;
        }

        // Se il target muore lo resetta
        if (currentTarget != null) {
            if (!currentTarget.isActive()) {
                currentTarget = null;
                isMoving = true;
            }
        }
    }

    public void move(double deltaTime) {
        // Se non deve muoversi
        if (!isMoving) {
            return;
        }
        // Per la decisione del movimento ho sfruttato l'attributo team per capire verso dove andava
        double direzione;

        // Player destra enemy sinistra
        if (squadra.equals("player")) {
            direzione = 1;
        } else {
            direzione = -1;
        }

        // Movimento unita
        posX = posX + moveSpeed * direzione * deltaTime * 60;
    }

    public void attack(Unit target) {
        // Controllo target
        if (target == null) {
            return;
        }

        // Se il target è morto
        if (!target.isActive()) {
            return;
        }

        // Attacca solo se pronto col countdown dell attacco
        if (atkTimer <= 0) {
            target.takeDamage(atkDamage);
            atkTimer = atkCooldown;
        }
    }

    public void takeDamage(double damage) {
        // Riduce hp
        health = health - damage;

        // Morte unita
        if (health <= 0) {
            health = 0;
            // Schiatta
            attiva = false;
        }

        // Aggiorna observer
        observer.notifyObservers("health_changed", this);
    }

    public Unit findTarget(java.util.List<Unit> enemies) {
        Unit nearestEnemy = null;
        double minDistance = Double.MAX_VALUE;

        // Cerca nemico piu vicino
        for (int i = 0; i < enemies.size(); i++) {
            Unit enemy = enemies.get(i);

            // Skip se morto
            if (!enemy.isActive()) {
                // Con il ragionamento opposto (usare un if(true) con le operazioni nell'if)
                // non mi riusciva a targhettare il nemico
                continue;
            }
            double distanza = distanceTo(enemy);

            // Salva il piu vicino
            if (distanza < minDistance) {
                minDistance = distanza;
                nearestEnemy = enemy;
            }
        }
        return nearestEnemy;
    }

    public boolean isInRange(Unit target) {
        double distanza = distanceTo(target);

        // Controlla se nel range
        if (distanza <= attackRange) {
            return true;
        }
        return false;
    }

    public double getHp() {
        return health;
    }

    public double getMaxHp() {
        return maxHealth;
    }

    public String getTeam() {
        return squadra;
    }

    public double getCost() {
        return spawnCost;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        this.isMoving = moving;
    }

    public void setTarget(Unit target) {
        this.currentTarget = target;
    }

    public Unit getTarget() {
        return currentTarget;
    }

    public double getRange() {
        return attackRange;
    }

    public double getAttackCooldown() {
        return atkCooldown;
    }

    public double getAttackTimer() {
        return atkTimer;
    }

    public Subject getSubject() {
        return observer;
    }

    public double getDamage() {
        return atkDamage;
    }

    public abstract String getUnitName();
}