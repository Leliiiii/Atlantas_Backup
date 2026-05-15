package ui;

import buildings.Tower;
import engine.GameEngine;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import observer.Observer;
import projectiles.Projectile;
import resources.ResourceManager;
import units.Unit;

import java.util.List;

public class GameGUI {
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final GameEngine engine;
    private final ResourceManager resourceManager;
    private AnimationTimer gameLoop;
    private long lastTime = 0;

    private boolean showGameOver = false;
    private boolean playerWon = false;

    public GameGUI(Canvas canvas, GameEngine engine) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.engine = engine;
        this.resourceManager = ResourceManager.getInstance();

        setupObservers();
    }

    private void setupObservers() {
        engine.addGameStateObserver((event, data) -> {
            if ("game_over".equals(event)) {
                showGameOver = true;
                playerWon = (Boolean) data;
            }
        });
    }

    public void start() {
        lastTime = System.nanoTime();
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double deltaTime = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;

                if (deltaTime > 0.05) deltaTime = 0.05; // Cap delta time

                engine.update(deltaTime);
                render();
            }
        };
        gameLoop.start();
    }

    public void stop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        drawBackground();
        drawLane();
        drawTowers();
        drawUnits();
        drawProjectiles();
        drawUI();

        //TODO COSA SUCCEDE SE SI PERDE (IF)
    }

    private void drawBackground() {
        // Deep ocean gradient effect
        gc.setFill(Color.rgb(5, 20, 40));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Water particles effect
        gc.setFill(Color.rgb(20, 60, 100, 0.3));
        for (int i = 0; i < 20; i++) {
            double px = Math.random() * canvas.getWidth();
            double py = Math.random() * canvas.getHeight();
            gc.fillOval(px, py, 3, 3);
        }
    }

    private void drawLane() {
        double laneY = engine.getLaneY();
        double laneHeight = 80;

        // Lane background
        gc.setFill(Color.rgb(30, 50, 70, 0.6));
        gc.fillRect(0, laneY - laneHeight/2, canvas.getWidth(), laneHeight);

        // Lane border
        gc.setStroke(Color.rgb(60, 120, 160, 0.5));
        gc.setLineWidth(2);
        gc.strokeRect(0, laneY - laneHeight/2, canvas.getWidth(), laneHeight);

        // Center line
        gc.setStroke(Color.rgb(40, 80, 120, 0.3));
        gc.setLineWidth(1);
        gc.strokeLine(canvas.getWidth()/2, laneY - laneHeight/2,
                      canvas.getWidth()/2, laneY + laneHeight/2);
    }

    private void drawTowers() {
        drawTower(engine.getPlayerTower());
        drawTower(engine.getEnemyTower());
    }

    private void drawTower(Tower tower) {
        double x = tower.getX();
        double y = tower.getY();
        double w = tower.getWidth();
        double h = tower.getHeight();

        // Tower body
        Color towerColor = tower.getTeam().equals("player")
            ? Color.DEEPSKYBLUE
            : Color.DARKSEAGREEN;

        gc.setFill(towerColor);
        gc.fillRect(x, y, w, h);

        // Tower details
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeRect(x, y, w, h);

        // Tower top
        gc.setFill(towerColor.brighter());
        gc.fillRect(x - 5, y - 10, w + 10, 15);

        // HP Bar
        drawHPBar(x, y - 25, w, 12, tower.getHp(), tower.getMaxHp(),
                  tower.getTeam().equals("player") ? Color.GREEN : Color.RED);
    }

    private void drawUnits() {
        drawUnitList(engine.getPlayerUnits());
        drawUnitList(engine.getEnemyUnits());
    }

    private void drawUnitList(List<Unit> units) {
        for (Unit unit : units) {
            if (!unit.isActive()) continue;

            double x = unit.getX();
            double y = unit.getY();
            double w = unit.getWidth();
            double h = unit.getHeight();

            // Unit body
            gc.setFill(unit.getColor());
            gc.fillOval(x, y, w, h);

            // Unit border
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(1.5);
            gc.strokeOval(x, y, w, h);

            // Team indicator
            gc.setFill(unit.getTeam().equals("player") ? Color.CYAN : Color.LIME);
            gc.fillOval(x + w/2 - 3, y - 8, 6, 6);

            // HP Bar
            drawHPBar(x, y - 15, w, 8, unit.getHp(), unit.getMaxHp(), Color.GREEN);
        }
    }

    private void drawProjectiles() {
        gc.setFill(Color.ORANGE);
        for (Projectile p : engine.getProjectiles()) {
            if (p.isActive()) {
                gc.fillOval(p.getX(), p.getY(), p.getWidth(), p.getHeight());
            }
        }
    }

    private void drawHPBar(double x, double y, double width, double height,
                           double hp, double maxHp, Color color) {
        double hpPercent = hp / maxHp;

        // Background
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(x, y, width, height);

        // HP fill
        gc.setFill(color);
        gc.fillRect(x, y, width * hpPercent, height);

        // Border
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.strokeRect(x, y, width, height);
    }

    private void drawUI() {
        double energy = resourceManager.getEnergy();
        double maxEnergy = resourceManager.getMaxEnergy();

        // Energy bar background
        gc.setFill(Color.rgb(20, 20, 20, 0.8));
        gc.fillRect(10, 10, 250, 40);

        // Energy bar
        gc.setFill(Color.GOLD);
        gc.fillRect(15, 15, 240 * (energy / maxEnergy), 30);

        // Energy text
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        gc.fillText(String.format("Energia: %.0f / %.0f", energy, maxEnergy), 20, 38);

        // Unit count
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 14));
        gc.fillText("Truppe: " + engine.getPlayerUnits().size(), 280, 35);
        gc.fillText("Nemici: " + engine.getEnemyUnits().size(), 380, 35);
    }

    //TODO METODO SCONFITTA


}
