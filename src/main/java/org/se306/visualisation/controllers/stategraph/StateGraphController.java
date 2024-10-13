package org.se306.visualisation.controllers.stategraph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class StateGraphController {

  @FXML private Canvas stateGraphCanvas;

  private GraphicsContext gc;
  private Random random = new Random(); // Random generator for x-position

  private static final int CANVAS_WIDTH = 585;
  private static final int CANVAS_HEIGHT = 155;
  private static final double NODE_SIZE = 5;
  private static final double MAX_VERTICAL_SPACING = CANVAS_HEIGHT - NODE_SIZE * 2;

  // Map to store the positions of each node based on stateId
  private Map<Integer, Double[]> nodePositions = new HashMap<>();

  @FXML
  private void initialize() {
    // Get the GraphicsContext from the SceneBuilder canvas
    gc = stateGraphCanvas.getGraphicsContext2D();

    // Create an array list of CustomStateNode objects, each representing a state
    List<CustomStateNode> stateNodes =
        List.of(
            new CustomStateNode(0, 1, null), // Root node, no parent
            new CustomStateNode(1, 2, 1), // Child of state 1
            new CustomStateNode(1, 3, 1), // Child of state 1
            new CustomStateNode(1, 4, 1), // Child of state 2
            new CustomStateNode(2, 5, 2), // Child of state 2
            new CustomStateNode(2, 6, 3), // Child of state 3
            new CustomStateNode(2, 7, 3), // Child of state 3
            new CustomStateNode(3, 8, 5), // Child of state 4
            new CustomStateNode(3, 9, 5), // Child of state 4
            new CustomStateNode(3, 10, 5) // Child of state 5
            );

    // Draw the tree based on the state nodes
    drawStateTree(stateNodes);
  }

  private void drawStateTree(List<CustomStateNode> stateNodes) {
    // Loop through each node and calculate position
    for (CustomStateNode node : stateNodes) {
      // Calculate the vertical position based on task count (logarithmic scaling)
      double y = getLogarithmicVerticalPosition(node.getTaskCount());

      // Randomly position the node across the x-axis
      double x;

      if (node.getTaskCount() <= 0) {
        x = CANVAS_WIDTH / 2; // Center the root node
      } else {
        x = getRandomXPosition();
      }

      // Draw the current state node
      drawNode(x, y);

      // Store the position of this node (x, y) using stateId
      nodePositions.put(node.getStateId(), new Double[] {x, y});

      // Draw a line to the parent if the node has a parent
      if (node.getParentStateId() != null) {
        drawLineToParent(x, y, node.getParentStateId());
      }
    }
  }

  // Method to calculate vertical position based on number of tasks using logarithmic scaling
  private double getLogarithmicVerticalPosition(int taskCount) {
    if (taskCount == 0) {
      return 20; // Root node is at the top
    }

    // Apply logarithmic scaling
    double logScale = Math.log(taskCount + 1); // +1 to avoid log(0)

    // Scale the log result to fit within the canvas height
    double scaledTaskCount = (logScale / Math.log(40)) * MAX_VERTICAL_SPACING;

    // Ensure the y value stays within the canvas bounds (0 to CANVAS_HEIGHT - NODE_SIZE)
    return Math.min(
        scaledTaskCount + NODE_SIZE, CANVAS_HEIGHT - NODE_SIZE - 20); // Add NODE_SIZE for spacing
  }

  // Method to get a random x-position within the canvas width
  private double getRandomXPosition() {
    double padding = 20;
    // Ensure the random x position is between `padding` and `CANVAS_WIDTH - padding - NODE_SIZE`
    return padding + random.nextDouble() * (CANVAS_WIDTH - 2 * padding - NODE_SIZE);
  }

  // Method to draw a line from the child node to its parent
  private void drawLineToParent(double x, double y, int parentStateId) {
    Double[] parentPosition = nodePositions.get(parentStateId);

    if (parentPosition != null) {
      double parentX = parentPosition[0];
      double parentY = parentPosition[1];

      // Set the line color using a hex code
      gc.setStroke(Color.web("#B6B9E2")); // Gray color in hex

      // Draw the line between the child and the parent
      gc.setLineWidth(1); // Set the thickness of the line
      gc.strokeLine(x, y, parentX, parentY);
    }
  }

  private void drawNode(double x, double y) {
    gc.setFill(Color.web("#b6b9e2")); // Node fill color
    gc.setStroke(Color.web("#6d6f9e")); // Node stroke color
    gc.setLineWidth(2);
    gc.fillOval(x - NODE_SIZE / 2, y - NODE_SIZE / 2, NODE_SIZE, NODE_SIZE); // Draw filled circle
    gc.strokeOval(x - NODE_SIZE / 2, y - NODE_SIZE / 2, NODE_SIZE, NODE_SIZE); // Draw circle border
  }
}
