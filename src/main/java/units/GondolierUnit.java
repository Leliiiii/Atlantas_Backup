package units;

import javafx.scene.paint.Color;

public class GondolierUnit extends Unit {
    public GondolierUnit(double x, double y) {
        super(x, y, 40, 40, Color.DARKSEAGREEN,
                100, 12, 1.2, 55, 1.0, "enemy", 0);
    }

    @Override
    public String getUnitName() {
        return "Gondoliere NecroAcquatico";
    }
}
