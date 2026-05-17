package units;

import javafx.scene.paint.Color;
import java.util.List;

public class FriarUnit extends Unit {
    private double healAmount = 6;
    private double healCooldown = 2.5;
    private double healTimer = 0;

    public FriarUnit(double x, double y) {
        super(x, y, 35, 40, Color.SEAGREEN,
                80, 4, 0.9, 100, 2.0, "enemy", 0);
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
        return "Frate Corrotto";
    }
}
