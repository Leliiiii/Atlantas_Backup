package units;

import javafx.scene.paint.Color;

public class JellyfishUnit extends Unit {
    public JellyfishUnit(double x, double y) {
        super(x, y, 35, 45, Color.LIGHTBLUE,
              80, 25, 1.0, 180, 1.5, "player", 30);
    }

    @Override
    public String getUnitName() {
        return "Medusa Elettrica";
    }
}
