package org.se306.visualisation.controllers.stategraph;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class StateGraphController {

  private static final Logger LOGGER = LoggerFactory.getLogger(StateGraphController.class);

  @FXML StackPane stateGraphStackPane;

  private Canvas canvas;
  private GraphicsContext gc;

  @FXML
  private void initialize() {
    LOGGER.debug("StateGraphController initialized");
    setupCanvas();
    drawInitialTree();
  }

  private void setupCanvas() {
    canvas = new Canvas(585, 155); // You can adjust the size as needed
    gc = canvas.getGraphicsContext2D();
    stateGraphStackPane.getChildren().add(canvas);
  }

  private void drawInitialTree() {
    // Placeholder for tree drawing logic
    // This method should include the logic to visualize the B&B tree
    drawRoot(292.5, 11);
  }

  private void drawRoot(double x, double y) {
    final double nodeSize = 10;
    gc.setFill(Color.web("#b6b9e2"));
    gc.setStroke(Color.web("#6d6f9e"));
    gc.setLineWidth(2);
    gc.fillOval(x - nodeSize / 2, y - nodeSize / 2, nodeSize, nodeSize);
    gc.strokeOval(x - nodeSize / 2, y - nodeSize / 2, nodeSize, nodeSize);
  }
}
