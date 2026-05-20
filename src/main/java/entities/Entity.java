package entities;

import javafx.scene.paint.Color;

public abstract class Entity {
    // posizione entity
    protected double posX;
    protected double posY;
    // dimensioni entity
    protected double larghezza;
    protected double altezza;
    // se è viva o attiva nel gioco
    protected boolean attiva = true;
    // colore grafico
    protected Color colore;

    public Entity(double posX, double posY, double larghezza, double altezza, Color colore) {
        this.posX = posX;
        this.posY = posY;
        this.larghezza = larghezza;
        this.altezza = altezza;
        this.colore = colore;
    }

    // aggiorna logica entity
    public abstract void update(double deltaTime);

    public boolean isActive() {
        return attiva;
    }

    public void setActive(boolean active) {
        this.attiva = active;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public double getLarghezza() {
        return larghezza;
    }

    public double getAltezza() {
        return altezza;
    }

    public Color getColore() {
        return colore;
    }

    public void setX(double x) {
        this.posX = x;
    }

    public void setY(double y) {
        this.posY = y;
    }

    // centro entity in X
    public double getCenterX() {
        return posX + larghezza / 2;
    }

    // centro entity in Y
    public double getCenterY() {
        return posY + altezza / 2;
    }

    // TODO gestione collisioni

    public double distanceTo(Entity e) {
        double deltaX = getCenterX() - e.getCenterX();
        double deltaY = getCenterY() - e.getCenterY();
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
}