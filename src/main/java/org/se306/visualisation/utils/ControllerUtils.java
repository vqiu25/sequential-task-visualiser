package org.se306.visualisation.utils;

import java.io.IOException;

import org.se306.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

public class ControllerUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(ControllerUtils.class);

  public static void swapComponent(Pane parent, Node newChild) {
    parent.getChildren().clear();
    parent.getChildren().add(newChild);
  }

  /**
   * Load an FXML file and return the root node and controller.
   *
   * @param <T> the type of the controller
   * @param url the url of the FXML file
   * @return a pair (aka Python tuple) containing the root node (key) and controller (value)
   */
  public static <T> Pair<Parent, T> loadFxml(String url) {
    LOGGER.trace("Loading FXML: {}", url);
    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("fxml/" + url));

    try {
      fxmlLoader.load();
      Parent rootNode = fxmlLoader.getRoot();
      T controller = fxmlLoader.getController();
      return new Pair<>(rootNode, controller);
    } catch (IOException e) {
      LOGGER.error("Error loading FXML: {}", url);
      throw new RuntimeException(e);
    }
  }

}
