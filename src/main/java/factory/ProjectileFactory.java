package factory;

import pool.ObjectPool;
import projectiles.Projectile;
import units.Unit;

public class ProjectileFactory {
    private static ProjectileFactory instance;
    private ObjectPool<Projectile> projectilePool;

    private ProjectileFactory() {
        // Lambda che fa lo stesso di questo
        // Quando il pool è vuoto e serve un nuovo oggetto:
        projectilePool = new ObjectPool<>(() -> new Projectile(0, 0, 0, 0, null, ""), 20, 100);
    }

    public static synchronized ProjectileFactory getInstance() {
        if (instance == null) {
            instance = new ProjectileFactory();
        }
        return instance;
    }



    public void returnProjectile(Projectile p) {
        projectilePool.free(p);
    }
}
