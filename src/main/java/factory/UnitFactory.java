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
                case "octopus":
                    unit = new OctopusUnit(x, y);
                    break;
                case "crab":
                    unit = new CrabUnit(x, y);
                    break;
                case "jellyfish":
                    unit = new JellyfishUnit(x, y);
                    break;
                case "pufferfish":
                    unit = new PufferfishUnit(x, y);
                    break;
                case "eel":
                    unit = new EelUnit(x, y);
                    break;
                case "seahorse":
                    unit = new SeahorseUnit(x, y);
                    break;
            }
        } else {
            switch (type) {
                case "gondolier":
                    unit = new GondolierUnit(x, y);
                    break;
                case "mask":
                    unit = new MaskUnit(x, y);
                    break;
                case "patrician":
                    unit = new PatricianUnit(x, y);
                    break;
                case "childswarm":
                    unit = new ChildSwarmUnit(x, y);
                    break;
                case "friar":
                    unit = new FriarUnit(x, y);
                    break;
                case "bossdoge":
                    unit = new BossDogeUnit(x, y);
                    break;
            }
        }

        return unit;
    }
}
