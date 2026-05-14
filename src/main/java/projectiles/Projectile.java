package projectiles;

import entities.Entity;
import javafx.scene.paint.Color;
import pool.Poolable;
import units.Unit;

public class Projectile extends Entity implements Poolable {
    protected double damage;
    protected double speed;
    protected Unit target;
    protected String team;

    public Projectile(double x, double y, double damage, double speed, Unit target, String team) {
        super(x, y, 8, 8, Color.ORANGE);
        this.damage = damage;
        this.speed = speed;
        this.target = target;
        this.team = team;
    }

    @Override
    public void update(double deltaTime) {
        if (!active || target == null || !target.isActive()) {
            active = false;
            return;
        }

        double dx = target.getCenterX() - getCenterX();
        double dy = target.getCenterY() - getCenterY();
        double dist = Math.sqrt(dx * dx + dy * dy);

        if (dist < 10) {
            target.takeDamage(damage);
            active = false;
            return;
        }

        double moveX = (dx / dist) * speed * deltaTime * 60;
        double moveY = (dy / dist) * speed * deltaTime * 60;

        x += moveX;
        y += moveY;
    }

    public void setTarget(Unit target) {
        this.target = target;
    }

    public Unit getTarget() {
        return target;
    }

    public double getDamage() {
        return damage;
    }

    public String getTeam() {
        return team;
    }

    @Override
    public void reset() {

    }
}
