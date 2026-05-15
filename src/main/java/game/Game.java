package game;

import engine.GameEngine;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ui.GameGUI;

public class Game extends Application {
    private static final double SCREEN_WIDTH = 1200;
    private static final double SCREEN_HEIGHT = 700;

    private GameEngine engine;
    private GameGUI gui;
    private Canvas canvas;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("L'Eclissi della Laguna");

        // Main layout
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #051428;");

        // Title
        Text title = new Text("L'Eclissi della Laguna");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setFill(Color.DEEPSKYBLUE);

        Text subtitle = new Text("2147 - Venezia è sprofondata. Atlantide deve sopravvivere.");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setFill(Color.LIGHTBLUE);

        VBox titleBox = new VBox(5, title, subtitle);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(10));
        root.setTop(titleBox);

        // Game Canvas
        canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT - 200);
        root.setCenter(canvas);

        // Initialize game
        engine = new GameEngine(SCREEN_WIDTH, SCREEN_HEIGHT - 200);
        gui = new GameGUI(canvas, engine);

        // Unit buttons
        HBox buttonBox = createUnitButtons();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setSpacing(10);
        root.setBottom(buttonBox);

        // Scene
        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);

        // Keyboard input for restart
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case R:
                    restartGame();
                    break;
                case DIGIT1:
                    engine.spawnPlayerUnit("octopus");
                    break;
                case DIGIT2:
                    engine.spawnPlayerUnit("crab");
                    break;
                case DIGIT3:
                    engine.spawnPlayerUnit("jellyfish");
                    break;
                case DIGIT4:
                    engine.spawnPlayerUnit("pufferfish");
                    break;
                case DIGIT5:
                    engine.spawnPlayerUnit("eel");
                    break;
                case DIGIT6:
                    engine.spawnPlayerUnit("seahorse");
                    break;
            }
        });

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        gui.start();
    }

    private HBox createUnitButtons() {
        HBox box = new HBox();

        Button btnCrab = createUnitButton("Granchio (35)", "crab", Color.DEEPSKYBLUE);

        box.getChildren().addAll(btnCrab);
        return box;
    }

    private Button createUnitButton(String text, String unitType, Color color) {
        Button btn = new Button(text);
        btn.setPrefWidth(140);
        btn.setPrefHeight(50);
        btn.setStyle(
            "-fx-background-color: " + toHex(color) + ";" +
            "-fx-text-fill: black;" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 12px;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;"
        );

        btn.setOnAction(e -> engine.spawnPlayerUnit(unitType));
        return btn;
    }

    private String toHex(Color color) {
        return String.format("#%02X%02X%02X",
            (int)(color.getRed() * 255),
            (int)(color.getGreen() * 255),
            (int)(color.getBlue() * 255));
    }

    private void restartGame() {
        gui.stop();
        engine.reset();
        gui.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
