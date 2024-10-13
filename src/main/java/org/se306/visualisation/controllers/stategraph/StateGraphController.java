package org.se306.visualisation.controllers.stategraph;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.se306.AppState;
import org.se306.domain.State;

public class StateGraphController {

  @FXML private Canvas stateGraphCanvas;

  private GraphicsContext gc;
  private Random random = new Random(); // Random generator for x-position

  private static final int CANVAS_WIDTH = 585;
  private static final int CANVAS_HEIGHT = 155;
  private static final double NODE_SIZE = 5;
  private static final double MAX_VERTICAL_SPACING = CANVAS_HEIGHT - NODE_SIZE * 2;

  // Use this map to track the positions of each state node
  private Map<State, Double[]> nodePositions = new HashMap<>();

  @FXML
  private void initialize() {
    gc = stateGraphCanvas.getGraphicsContext2D();

    // Add listener for changes to the current state in AppState
    AppState.getInstance()
        .currentStateProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue != null) {
                addStateToGraph(newValue);
              }
            });

    AppState.getInstance().setStateGraphController(this);
  }

  // This method adds the new state to the graph, drawing it and linking it to the parent
  public void addStateToGraph(State state) {
    // Calculate the vertical position based on the number of tasks in this state
    double y = getEvenlyDistributedVerticalPosition(state.getIdsToStateTasks().size());

    // Calculate the x position (randomized for each node except the root)
    double x = (state.getParentState() == null) ? CANVAS_WIDTH / 2 : getRandomXPosition();

    // Draw the current state node
    drawNode(x, y);

    // Store the position of this state (x, y)
    nodePositions.put(state, new Double[] {x, y});

    // Draw a line to the parent if it exists
    if (state.getParentState() != null) {
      drawLineToParent(x, y, state.getParentState());
    }
  }

  // Method to calculate vertical position based on the number of tasks (layers)
  private double getEvenlyDistributedVerticalPosition(int taskCount) {
    if (taskCount == 0) {
      return 20;
    }

    // Evenly distribute based on taskCount, with some padding for nodes
    double verticalSpacing =
        (MAX_VERTICAL_SPACING - 20) / (AppState.getInstance().getTaskCount() - 1);

    // Ensure non-root nodes start at a position below 20
    return 10 + NODE_SIZE + taskCount * verticalSpacing;
  }

  // Method to calculate vertical position based on the number of tasks using logarithmic scaling
  private double getLogarithmicVerticalPosition(int taskCount) {
    if (taskCount == 0) {
      return 20; // Root node near the top
    }

    // Apply logarithmic scaling
    double logScale = Math.log(taskCount + 1); // +1 to avoid log(0)
    double scaledTaskCount = (logScale / Math.log(10)) * (MAX_VERTICAL_SPACING + 20);

    return Math.min(scaledTaskCount + NODE_SIZE, CANVAS_HEIGHT - NODE_SIZE);
  }

  // Method to get a random x-position within the canvas width
  private double getRandomXPosition() {
    return 20 + random.nextDouble() * (CANVAS_WIDTH - 20); // Padding 20px on both sides
  }

  // Method to draw a line from the child node to its parent
  private void drawLineToParent(double x, double y, State parentState) {
    Double[] parentPosition = nodePositions.get(parentState);

    if (parentPosition != null) {
      double parentX = parentPosition[0];
      double parentY = parentPosition[1];

      gc.setStroke(Color.web("#B6B9E2", 0.2));
      gc.setLineWidth(0.5);
      gc.strokeLine(x, y, parentX, parentY);
    }
  }

  // Method to draw a node representing a state
  private void drawNode(double x, double y) {
    gc.setFill(Color.web("#b6b9e2")); // Node fill color
    gc.setStroke(Color.web("#6d6f9e")); // Node stroke color
    gc.setLineWidth(2);
    gc.fillOval(x - NODE_SIZE / 2, y - NODE_SIZE / 2, NODE_SIZE, NODE_SIZE); // Draw filled circle
    gc.strokeOval(x - NODE_SIZE / 2, y - NODE_SIZE / 2, NODE_SIZE, NODE_SIZE); // Draw circle border
  }

  // Method to highlight the optimal path in red after the algorithm completes
  public void highlightOptimalPath(State finalState) {
    State currentState = finalState;
    while (currentState != null) {
      // Get the position of the current state
      Double[] currentPosition = nodePositions.get(currentState);
      if (currentPosition != null) {
        double x = currentPosition[0];
        double y = currentPosition[1];

        // Redraw the current node and its connection to the parent in red
        drawNode(x, y, Color.web("#95258A"));

        if (currentState.getParentState() != null) {
          drawLineToParent(x, y, currentState.getParentState(), Color.web("#95258A"));
        }
      }

      // Move to the parent state
      currentState = currentState.getParentState();
    }
  }

  // Method to draw a node representing a state with a specified color
  private void drawNode(double x, double y, Color colour) {
    gc.setFill(colour);
    gc.setStroke(Color.web("#6d6f9e"));
    gc.setLineWidth(2);
    gc.fillOval(x - NODE_SIZE / 2, y - NODE_SIZE / 2, NODE_SIZE, NODE_SIZE);
    gc.strokeOval(x - NODE_SIZE / 2, y - NODE_SIZE / 2, NODE_SIZE, NODE_SIZE);
  }

  // Method to draw a line from the child node to its parent with color
  private void drawLineToParent(double x, double y, State parentState, Color colour) {
    Double[] parentPosition = nodePositions.get(parentState);

    if (parentPosition != null) {
      double parentX = parentPosition[0];
      double parentY = parentPosition[1];

      gc.setStroke(colour); // Set line color
      gc.setLineWidth(0.5);
      gc.strokeLine(x, y, parentX, parentY);
    }
  }
}
