package units;

import javafx.scene.paint.Color;

public class PufferfishUnit extends Unit {
    private boolean exploded = false;

    public PufferfishUnit(double x, double y) {
        super(x, y, 30, 35, Color.YELLOW,
              60, 80, 1.3, 50, 2.0, "player", 40);
    }

    @Override
    public void attack(Unit target) {
        if (attackTimer <= 0 && target != null && target.isActive() && !exploded) {
            target.takeDamage(damage);
            exploded = true;
            hp = 0;
            active = false;
        }
    }

    @Override
    public String getUnitName() {
        return "Pesce Palla Esplosivo";
    }
}
