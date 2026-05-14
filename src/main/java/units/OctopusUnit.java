package units;

import javafx.scene.paint.Color;

public class OctopusUnit extends Unit {
    public OctopusUnit(double x, double y) {
        super(x, y, 40, 40, Color.CYAN,
              120, 15, 1.5, 60, 1.0, "player", 20);
    }

    @Override
    public String getUnitName() {
        return "Polpo Tattico";
    }
}
