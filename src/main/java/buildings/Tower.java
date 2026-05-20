package buildings;

import javafx.scene.image.Image;
import observer.Subject;

public class Tower {
    protected double x, y;
    protected double width, height;
    protected double hp;
    protected double maxHp;
    protected String team;
    protected boolean destroyed = false;
    protected Subject subject;

    public Tower(double x, double y, double width, double height, double hp, String team) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hp = hp;
        this.maxHp = hp;
        this.team = team;
        this.subject = new Subject();
    }

    public void takeDamage(double damage) {
        hp -= damage;
        if (hp <= 0) {
            hp = 0;
            destroyed = true;
        }
        subject.notifyObservers("tower_hp_changed", this);
    }


    public boolean isDestroyed() {
        return destroyed;
    }

    public double getHp() { return hp; }
    public double getMaxHp() { return maxHp; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public String getTeam() { return team; }
}
