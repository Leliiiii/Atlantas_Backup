package factory;

import units.*;

public class UnitFactory {
    private static UnitFactory instance;

    private UnitFactory() {}

    public static synchronized UnitFactory getInstance() {
        if (instance == null) {
            instance = new UnitFactory();
        }
        return instance;
    }

    public Unit createUnit(String type, double x, double y, String team) {
        Unit unit = null;

        if ("player".equals(team)) {
            switch (type) {
                case "crab":
                    unit = new CrabUnit(x, y);
                    break;
            }
        } else {
            switch (type) {
                case "childswarm":
                    unit = new ChildSwarmUnit(x, y);
                    break;
                case "bossdoge":
                    unit = new BossDogeUnit(x, y);
                    break;
            }
        }

        return unit;
    }
}
