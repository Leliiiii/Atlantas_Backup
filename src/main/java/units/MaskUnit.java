package units;

import javafx.scene.paint.Color;

public class MaskUnit extends Unit {
    public MaskUnit(double x, double y) {
        super(x, y, 30, 35, Color.LIMEGREEN,
                60, 15, 2.8, 45, 0.7, "enemy", 0);
    }

    @Override
    public String getUnitName() {
        return "Maschera Veloce";
    }
}
