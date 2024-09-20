package org.se306.visualisation;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FxApp extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    AnchorPane root = new AnchorPane();
    Button button = new Button("Click Me");
    root.getChildren().add(button);

    Scene scene = new Scene(root, 640, 480);
    stage.setScene(scene);
    stage.show();
  }
}
