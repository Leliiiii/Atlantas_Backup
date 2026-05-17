package units;

import javafx.scene.paint.Color;
import java.util.List;

public class SeahorseUnit extends Unit {
    private double healAmount = 8;
    private double healCooldown = 2.0;
    private double healTimer = 0;

    public SeahorseUnit(double x, double y) {
        super(x, y, 30, 35, Color.LIGHTGREEN,
                70, 5, 1.0, 100, 2.0, "player", 30);
    }

    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);
        if (healTimer > 0) {
            healTimer -= deltaTime;
        }
    }

    public void healAllies(List<Unit> allies) {
        if (healTimer <= 0) {
            for (Unit ally : allies) {
                if (ally.isActive() && ally != this && distanceTo(ally) <= attackRange) {
                    ally.health = Math.min(ally.health + healAmount, ally.maxHealth);
                    ally.getSubject().notifyObservers("hp_changed", ally);
                }
            }
            healTimer = healCooldown;
        }
    }

    @Override
    public String getUnitName() {
        return "Cavalluccio Curatore";
    }
}
