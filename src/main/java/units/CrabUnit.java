package units;

import javafx.scene.paint.Color;

public class CrabUnit extends Unit {
    public CrabUnit(double x, double y) {
        super(x, y, 55, 50, Color.DEEPSKYBLUE,
              300, 10, 0.8, 55, 1.2, "player", 35);
    }

    @Override
    public String getUnitName() {
        return "Granchio Corazzato";
    }
}
