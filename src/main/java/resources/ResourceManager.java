package resources;

import observer.Observer;
import observer.Subject;

public class ResourceManager {
    private static ResourceManager instance;
    private final Subject subject;

    private double energy = 50;
    private double maxEnergy = 100;
    private double regenRate = 0.3;

    private ResourceManager() {
        this.subject = new Subject();
    }

    public static synchronized ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager();
        }
        return instance;
    }

    public void addObserver(Observer observer) {
        subject.attach(observer);
    }

    public void update(double deltaTime) {
        if (energy < maxEnergy) {
            energy += regenRate * deltaTime;
            if (energy > maxEnergy) {
                energy = maxEnergy;
            }
            subject.notifyObservers("energy_changed", energy);
        }
    }

    public boolean canAfford(double cost) {
        return energy >= cost;
    }

    public boolean spendEnergy(double cost) {
        if (canAfford(cost)) {
            energy -= cost;
            subject.notifyObservers("energy_changed", energy);
            return true;
        }
        return false;
    }

    public double getEnergy() {
        return energy;
    }

    public double getMaxEnergy() {
        return maxEnergy;
    }

    public void reset() {
        energy = 50;
        subject.notifyObservers("energy_changed", energy);
    }

    public Subject getSubject() {
        return subject;
    }
}
