package leo;

import javafx.application.Application;

/**
 * Launcher class for JavaFX application.
 * This class is required to launch the JavaFX application properly.
 */
public class Launcher extends Application {
    @Override
    public void start(javafx.stage.Stage primaryStage) {
        new LeoFX().start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
