package game;

import javafx.application.Application;

/**
 * Launcher che avvia JavaFX senza richiedere --add-modules.
 * Il trucco: la classe main NON estende Application,
 * quindi Java non richiede JavaFX runtime all'avvio.
 */
public class Launcher {
    public static void main(String[] args) {
        Application.launch(Game.class, args);
    }
}
