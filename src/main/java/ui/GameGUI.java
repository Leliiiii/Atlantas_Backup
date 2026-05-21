package ui;

import buildings.Tower;
import engine.GameEngine;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import projectiles.Projectile;
import resources.ResourceManager;
import units.Unit;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

public class GameGUI {
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final GameEngine engine;
    private final ResourceManager resourceManager;
    private AnimationTimer gameLoop;
    private long lastTime = 0;
    // Background come attributo injvece di caricarlo ogni volta
    private Image backgroundImage;
    private Image playerTowerImage;
    private Image enemyTowerImage;

    private boolean showGameOver = false;
    private boolean playerWon = false;

    public GameGUI(Canvas canvas, GameEngine engine) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.engine = engine;
        this.resourceManager = ResourceManager.getInstance();
        this.backgroundImage = new Image(getClass().getResourceAsStream("/img/background.png"), canvas.getWidth(), canvas.getHeight(), false, true);
        // Carica immagini torri
        this.playerTowerImage = new Image(getClass().getResourceAsStream("/img/playerTower.png"));
        this.enemyTowerImage = new Image(getClass().getResourceAsStream("/img/enemyTower.png"));
        setupObservers();
    }

    // Aggiungo il mio evento setup in caso di partita persa
    private void setupObservers() {
        engine.addGameStateObserver((event, data) -> {
            if ("game_over".equals(event)) {
                showGameOver = true;
                playerWon = (Boolean) data;
            }
        });
    }


    public void start() {
        // Salva il tempo iniziale
        lastTime = System.nanoTime();

        // Game loop principale
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Tempo passato dal frame precedente
                double deltaTime = (now - lastTime) / 1_000_000_000.0;

                // Aggiorna il tempo vecchio
                lastTime = now;

                // Evita lag enormi perchè con
                if (deltaTime > 0.05) {
                    deltaTime = 0.05;
                }

                // Aggiorna il gioco
                engine.update(deltaTime);

                // Ridisegna tutto
                render();
            }
        };
        // Start loop
        gameLoop.start();
    }

    public void stop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }

    public void render() {
        // Riidisegna e resetta
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        drawBackground();
        drawLane();
        drawTowers();
        drawUnits();
        drawProjectiles();
        drawUI();

        //TODO COSA SUCCEDE SE SI PERDE (IF)
        if (showGameOver) {
            drawGameOver();
        }

    }

    //TODO NON FUNZIONA BENE
    private void drawBackground() {
        gc.drawImage(backgroundImage, 0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void drawLane() {
        double laneY = engine.getLaneY();
        double laneHeight = 80;

        // Profondità per la lane
        gc.setFill(Color.rgb(30, 50, 70, 0.6));
        gc.fillRect(0, laneY - laneHeight/2, canvas.getWidth(), laneHeight);

        // Con bordo
        gc.setStroke(Color.rgb(60, 120, 160, 0.5));
        gc.setLineWidth(2);
        gc.strokeRect(0, laneY - laneHeight/2, canvas.getWidth(), laneHeight);

        // Linea centrale
        gc.setStroke(Color.rgb(40, 80, 120, 0.3));
        gc.setLineWidth(1);
        gc.strokeLine(canvas.getWidth()/2, laneY - laneHeight/2, canvas.getWidth()/2, laneY + laneHeight/2);
    }

    private void drawTowers() {
        drawTower(engine.getPlayerTower());
        drawTower(engine.getEnemyTower());
    }

    //TODO DA SOSTITUIRE A CAUSA DEGLI SPRITE
    private void drawTower(Tower tower) {
        double x = tower.getX();
        double y = tower.getY();
        double w = tower.getWidth();
        double h = tower.getHeight();

        // Disegna immagine torre invece di forme
        if (tower.getTeam().equals("player")) {
            gc.drawImage(playerTowerImage, x - 20, y - 200, w, h);
        } else {
            gc.drawImage(enemyTowerImage, x - 100, y - 200, w, h);
        }

        // Barra HP sopra la torre
        Color hpColor;

        if (tower.getTeam().equals("player")) {
            hpColor = Color.GREEN;
        } else {
            hpColor = Color.RED;
            x += -80;
        }
        drawHPBar(x, y - 220, w - 20, 12, tower.getHp(), tower.getMaxHp(), hpColor);
    }

    private void drawUnits() {
        drawUnitList(engine.getPlayerUnits());
        drawUnitList(engine.getEnemyUnits());
    }

    //TODO DA SISTEMARE CON GLI SPRITE
    private void drawUnitList(List<Unit> units) {
        for (int i = 0; i < units.size(); i++) {
            Unit currentUnit = units.get(i);

            // Skip se morto
            if (!currentUnit.isActive()) {
                continue;
            }

            double unitX = currentUnit.getPosX();
            double unitY = currentUnit.getPosY();
            double unitWidth = currentUnit.getLarghezza();
            double unitHeight = currentUnit.getAltezza();

            // Corpo unita
            gc.setFill(currentUnit.getColore());
            gc.fillOval(unitX, unitY, unitWidth, unitHeight);

            // Bordo unita
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(1.5);
            gc.strokeOval(unitX, unitY, unitWidth, unitHeight);

            // Colore l'unit di un team
            Color teamColor;

            if (currentUnit.getTeam().equals("player")) {
                teamColor = Color.CYAN;
            } else {
                teamColor = Color.LIME;
            }

            // Pallino team
            gc.setFill(teamColor);
            gc.fillOval(unitX + unitWidth / 2 - 3, unitY - 8, 6, 6);

            // Barra hp
            drawHPBar(unitX, unitY - 15, unitWidth, 8, currentUnit.getHp(), currentUnit.getMaxHp(), Color.GREEN);
        }
    }

    
    private void drawProjectiles() {
        // Colore proiettili
        gc.setFill(Color.ORANGE);

        for (int i = 0; i < engine.getProjectiles().size(); i++) {
            Projectile proj = engine.getProjectiles().get(i);
            // Skip se non attivo
            if (!proj.isActive()) {
                continue;
            }

            gc.fillOval(proj.getPosX(), proj.getPosY(), proj.getLarghezza(), proj.getAltezza());
        }
    }

    private void drawHPBar(double x, double y, double width, double height, double hp, double maxHp, Color color) {
        double hpPercent = hp / maxHp;

        // Background
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(x, y, width, height);

        // HP filla col colore
        gc.setFill(color);
        gc.fillRect(x, y, width * hpPercent, height);

        // Bordi
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.strokeRect(x, y, width, height);
    }

    private void drawUI() {
        double energy = resourceManager.getEnergy();
        double maxEnergy = resourceManager.getMaxEnergy();

        // Background barra dell'energia
        gc.setFill(Color.rgb(20, 20, 20, 0.8));
        gc.fillRect(10, 10, 250, 40);

        // Barra energis
        gc.setFill(Color.GOLD);
        gc.fillRect(15, 15, 240 * (energy / maxEnergy), 30);

        // Testo per l'energia
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        // Numero decimale ma senza cifre dopo la virgola
        gc.fillText(String.format("Energia: %.0f / %.0f", energy, maxEnergy), 20, 38);

        // Contatore truppe
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 14));
        gc.fillText("Truppe: " + engine.getPlayerUnits().size(), 280, 35);
        gc.fillText("Nemici: " + engine.getEnemyUnits().size(), 380, 35);
    }

    //TODO METODO SCONFITTA
    
private void drawGameOver() {
        // overlay scuro
        gc.setFill(Color.rgb(0, 0, 0, 0.7));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Game Over 
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        if (playerWon) {
            gc.setFill(Color.GOLD);
            gc.fillText("VITTORIA!", canvas.getWidth()/2 - 120, canvas.getHeight()/2 - 20);
            gc.setFont(Font.font("Arial", 18));
            gc.setFill(Color.WHITE);
            gc.fillText("Atlantide è salva!", canvas.getWidth()/2 - 80, canvas.getHeight()/2 + 20);
        } else {
            gc.setFill(Color.DARKRED);
            gc.fillText("SCONFITTA!", canvas.getWidth()/2 - 130, canvas.getHeight()/2 - 20);
            gc.setFont(Font.font("Arial", 18));
            gc.setFill(Color.WHITE);
            gc.fillText("Il Doge Necrotico ha vinto...", canvas.getWidth()/2 - 110, canvas.getHeight()/2 + 20);
        }

        gc.setFont(Font.font("Arial", 14));
        gc.setFill(Color.LIGHTGRAY);
        gc.fillText("Premi R per riprovare", canvas.getWidth()/2 - 80, canvas.getHeight()/2 + 60);
    }

    public boolean isShowGameOver() {
        return showGameOver;
    }

    public void reset() {
        showGameOver = false;
        playerWon = false;
    }





    
}
