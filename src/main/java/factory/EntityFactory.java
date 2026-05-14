package factory;

import units.Unit;
import projectiles.Projectile;

public class EntityFactory {
    private static EntityFactory instance;
    private final UnitFactory unitFactory;
    private final ProjectileFactory projectileFactory;

    private EntityFactory() {
        this.unitFactory = UnitFactory.getInstance();
        this.projectileFactory = ProjectileFactory.getInstance();
    }

    public static synchronized EntityFactory getInstance() {
        if (instance == null) {
            instance = new EntityFactory();
        }
        return instance;
    }

    public Unit createUnit(String type, double x, double y, String team) {
        return unitFactory.createUnit(type, x, y, team);
    }

    public Projectile createProjectile(double x, double y, double damage, double speed, Unit target, String team) {
        return projectileFactory.createProjectile(x, y, damage, speed, target, team);
    }

    public void returnProjectile(Projectile p) {
        projectileFactory.returnProjectile(p);
    }
}
