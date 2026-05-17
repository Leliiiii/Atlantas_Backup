package units;

import javafx.scene.paint.Color;

public class ChildSwarmUnit extends Unit {
    public ChildSwarmUnit(double x, double y) {
        super(x, y, 20, 25, Color.PALEGREEN, 25, 8, 1.8, 40, 0.6, "enemy", 0);
    }

    @Override
    public String getUnitName() {
        return "Sciame Infantile";
    }
}
