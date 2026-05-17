package units;

import javafx.scene.paint.Color;

public class PatricianUnit extends Unit {
    public PatricianUnit(double x, double y) {
        super(x, y, 60, 55, Color.FORESTGREEN,
                350, 8, 0.6, 60, 1.3, "enemy", 0);
    }

    @Override
    public String getUnitName() {
        return "Patrizio Corrotto";
    }
}
