package units;

import entities.Entity;
import javafx.scene.paint.Color;
import observer.Subject;

public abstract class Unit extends Entity {
    protected double hp;
    protected double maxHp;
    protected double damage;
    protected double speed;
    protected double range;
    protected double attackCooldown;
    protected double attackTimer = 0;
    protected String team;
    protected Unit target;
    protected boolean moving = true;
    protected Subject subject;
    protected double cost;

    public Unit(double x, double y, double width, double height, Color color,
                double hp, double damage, double speed, double range,
                double attackCooldown, String team, double cost) {
        super(x, y, width, height, color);
        this.hp = hp;
        this.maxHp = hp;
        this.damage = damage;
        this.speed = speed;
        this.range = range;
        this.attackCooldown = attackCooldown;
        this.team = team;
        this.cost = cost;
        this.subject = new Subject();
    }

    @Override
    public void update(double deltaTime) {
        if (!active || hp <= 0) {
            active = false;
            return;
        }

        if (attackTimer > 0) {
            attackTimer -= deltaTime;
        }

        if (target != null && !target.isActive()) {
            target = null;
            moving = true;
        }
    }

    public void move(double deltaTime) {
        if (moving) {
            double direction = team.equals("player") ? 1 : -1;
            x += speed * direction * deltaTime * 60;
        }
    }

    public void attack(Unit target) {
        if (attackTimer <= 0 && target != null && target.isActive()) {
            target.takeDamage(damage);
            attackTimer = attackCooldown;
        }
    }

    public void takeDamage(double damage) {
        hp -= damage;
        if (hp <= 0) {
            hp = 0;
            active = false;
        }
        subject.notifyObservers("hp_changed", this);
    }

    public Unit findTarget(java.util.List<Unit> enemies) {
        Unit closest = null;
        double minDist = Double.MAX_VALUE;

        for (Unit enemy : enemies) {
            if (!enemy.isActive()) continue;
            double dist = distanceTo(enemy);
            if (dist < minDist) {
                minDist = dist;
                closest = enemy;
            }
        }

        return closest;
    }

    public boolean isInRange(Unit target) {
        return distanceTo(target) <= range;
    }

    public double getHp() { return hp; }
    public double getMaxHp() { return maxHp; }
    public String getTeam() { return team; }
    public double getCost() { return cost; }
    public boolean isMoving() { return moving; }
    public void setMoving(boolean moving) { this.moving = moving; }
    public void setTarget(Unit target) { this.target = target; }
    public Unit getTarget() { return target; }
    public double getRange() { return range; }
    public double getAttackCooldown() { return attackCooldown; }
    public double getAttackTimer() { return attackTimer; }
    public Subject getSubject() { return subject; }
    public double getDamage() { return damage; }

    public abstract String getUnitName();


}
