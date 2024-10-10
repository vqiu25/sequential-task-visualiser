package org.se306.visualisation.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import org.se306.visualisation.FxApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** This controller handles responsiveness when resizing the app */
public class WrapperController {

  private static final Logger LOGGER = LoggerFactory.getLogger(WrapperController.class);

  private static final int DEFAULT_WIDTH = 800;
  private static final int DEFAULT_HEIGHT = 450;

  private double zoom = 1;

  @FXML private Pane panLayout; // Responsible for responsiveness
  @FXML private Pane panPage; // Responsible for swapping pages (unused in this project)

  @FXML
  private void initialize() {
    LOGGER.debug("WrapperController initialized");
    setUpListeners();
  }

  private void setUpListeners() {
    Scene scene = FxApp.getScene();
    scene
        .heightProperty()
        .addListener(
            (obs, oldHeight, newHeight) -> {
              updateLayoutSize(scene.getWidth(), newHeight.doubleValue());
            });
    scene
        .widthProperty()
        .addListener(
            (obs, oldWidth, newWidth) -> {
              updateLayoutSize(newWidth.doubleValue(), scene.getHeight());
            });
  }

  /**
   * Updates the layout dimensions to fill the scene. Will scale inner contents. Will maintain
   * original aspect ratio, adding borders if neccessary (layout centered).
   *
   * @param sceneWidth The current width of the scene.
   * @param sceneHeight The current height of the scene.
   */
  private void updateLayoutSize(double sceneWidth, double sceneHeight) {
    // Calculate layout width (which may be smaller than scene width to maintain
    // aspect ratio)
    double aspectRatio = (double) DEFAULT_WIDTH / DEFAULT_HEIGHT;
    double layoutWidth = Math.min(sceneWidth, sceneHeight * aspectRatio);

    // Calculate zoom and shift (to recentre)
    zoom = layoutWidth / DEFAULT_WIDTH;
    double horizontalShift = (sceneWidth - DEFAULT_WIDTH) / 2;
    double verticalShift = (sceneHeight - DEFAULT_HEIGHT) / 2;

    // Apply zoom and shift
    panLayout.setScaleX(zoom);
    panLayout.setScaleY(zoom);
    panLayout.setLayoutX(horizontalShift);
    panLayout.setLayoutY(verticalShift);
  }

  public double getZoom() {
    return zoom;
  }
}
