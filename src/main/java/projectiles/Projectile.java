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

    //TODO IL RENDERING DEI PROIETTILI
    public void update(double tempo){

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
