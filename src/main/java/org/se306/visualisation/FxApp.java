package org.se306.visualisation;

import java.io.IOException;

import org.se306.visualisation.controllers.MainController;
import org.se306.visualisation.utils.ControllerUtils;
import org.se306.visualisation.utils.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;

public class FxApp extends Application {

  private static final Logger LOGGER = LoggerFactory.getLogger(FxApp.class);
  private static Stage stage;
  private static Scene scene;

  public static void main(String[] args) {
    launch();
  }

  @Override
  public void start(Stage stage) throws IOException {
    LOGGER.info("Starting application");
    FxApp.stage = stage;

    // Load scene
    scene = new Scene(new AnchorPane(), 640, 480);
    stage.setScene(scene);
    Pair<Parent, MainController> main = ControllerUtils.loadFxml("main.fxml");
    scene.setRoot(main.getKey());

    // Load resources
    ResourceUtils.loadFont("Montserrat-Medium.ttf");
    scene.getStylesheets().add(ResourceUtils.loadCss("globals.css"));

    // Set up stage
    scene.setFill(Color.web("#131d23"));
    stage.setTitle(":D");
    stage.getIcons().add(ResourceUtils.loadImage("oliver.png"));

    // Set up fullscreen
    stage.setFullScreen(true);
    stage.setFullScreenExitHint("Press F11 to exit full screen mode.");
    scene.setOnKeyPressed(
        event -> {
          if (event.getCode() == KeyCode.F11)
            stage.setFullScreen(!stage.isFullScreen());
        });

    stage.show();
  }

  @Override
  public void stop() throws IOException {
    LOGGER.info("Stopping application");
  }

  public static Stage getStage() {
    return stage;
  }

  public static Scene getScene() {
    return scene;
  }
}