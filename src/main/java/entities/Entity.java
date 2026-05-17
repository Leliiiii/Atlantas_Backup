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

    public Entity(double x, double y, double width, double height, Color color) {
        this.posX = x;
        this.posY = y;
        this.larghezza = width;
        this.altezza = height;
        this.colore = color;
    }

    // aggiorna logica entity
    public abstract void update(double deltaTime);

    public boolean isActive() {
        return attiva;
    }

    public void setActive(boolean active) {
        this.attiva = active;
    }

    public double getX() {
        return posX;
    }

    public double getY() {
        return posY;
    }

    public double getWidth() {
        return larghezza;
    }

    public double getHeight() {
        return altezza;
    }

    public Color getColor() {
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

    public double distanceTo(Entity other) {
        double deltaX = getCenterX() - other.getCenterX();
        double deltaY = getCenterY() - other.getCenterY();
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
}