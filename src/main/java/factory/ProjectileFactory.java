package factory;

import pool.ObjectPool;
import projectiles.Projectile;
import units.Unit;

public class ProjectileFactory {
    private static ProjectileFactory instance;
    private ObjectPool<Projectile> projectilePool;

    private ProjectileFactory() {
        projectilePool = new ObjectPool<>(
            () -> new Projectile(0, 0, 0, 0, null, ""),
            20, 100
        );
    }

    public static synchronized ProjectileFactory getInstance() {
        if (instance == null) {
            instance = new ProjectileFactory();
        }
        return instance;
    }

    public Projectile createProjectile(double x, double y, double damage, double speed, Unit target, String team) {
        Projectile p = projectilePool.obtain();
        if (p == null) {
            p = new Projectile(x, y, damage, speed, target, team);
        } else {
            p.setX(x);
            p.setY(y);
            p.setTarget(target);
        }
        return p;
    }

    public void returnProjectile(Projectile p) {
        projectilePool.free(p);
    }
}
