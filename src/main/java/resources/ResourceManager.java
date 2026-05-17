package resources;

import observer.Observer;
import observer.Subject;

public class ResourceManager {
    private static ResourceManager instance;
    private final Subject observer;
    // Energia attuale del player
    private double energiaAttuale = 50;
    // Energia massima possibile
    private double energiaMassima = 100;
    // Quanto velocemente si rigenera l energia
    private double velocitaRegen = 0.8;

    private ResourceManager() {
        this.observer = new Subject();
    }

    public static synchronized ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager();
        }
        return instance;
    }

    public void addObserver(Observer observer) {
        this.observer.attach(observer);
    }

    public void update(double deltaTime) {
        if (energiaAttuale < energiaMassima) {
            energiaAttuale += velocitaRegen * deltaTime;
            if (energiaAttuale > energiaMassima) {
                energiaAttuale = energiaMassima;
            }
            observer.notifyObservers("energy_changed", energiaAttuale);
        }
    }

    public boolean canAfford(double cost) {
        return energiaAttuale >= cost;
    }

    public boolean spendEnergy(double cost) {
        if (canAfford(cost)) {
            energiaAttuale = energiaAttuale - cost;
            observer.notifyObservers("energy_changed", energiaAttuale);
            return true;
        }
        return false;
    }

    public double getEnergy() {
        return energiaAttuale;
    }

    public double getMaxEnergy() {
        return energiaMassima;
    }

    public void reset() {
        energiaAttuale = 50;
        observer.notifyObservers("energy_changed", energiaAttuale);
    }

    public Subject getSubject() {
        return observer;
    }
}