package entities;

import javafx.scene.paint.Color;

public abstract class Entity {
    protected double x, y;
    protected double width, height;
    protected boolean active = true;
    protected Color color;

    public Entity(double x, double y, double width, double height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public abstract void update(double deltaTime);

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public Color getColor() { return color; }

    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }

    public double getCenterX() {
        return x + width / 2;
    }

    public double getCenterY() {
        return y + height / 2;
    }

    //TODO IN CASO DI INTERSECAZIONE

    public double distanceTo(Entity other) {
        double dx = getCenterX() - other.getCenterX();
        double dy = getCenterY() - other.getCenterY();
        return Math.sqrt(dx * dx + dy * dy);
    }
}
