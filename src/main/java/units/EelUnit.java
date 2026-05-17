package units;

import javafx.scene.paint.Color;

public class EelUnit extends Unit {
    public EelUnit(double x, double y) {
        super(x, y, 25, 30, Color.AQUAMARINE,
                50, 20, 2.5, 50, 0.8, "player", 25);
    }

    @Override
    public String getUnitName() {
        return "Anguilla Assassina";
    }
}
