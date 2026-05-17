package units;

import javafx.scene.paint.Color;

public class BossDogeUnit extends Unit {
    public BossDogeUnit(double x, double y) {
        super(x, y, 80, 80, Color.DARKGREEN, 800, 30, 0.5, 70, 1.5, "enemy", 0);
    }

    @Override
    public String getUnitName() {
        return "Doge Necrotico";
    }
}
