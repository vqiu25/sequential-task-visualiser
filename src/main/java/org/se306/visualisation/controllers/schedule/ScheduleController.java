package org.se306.visualisation.controllers.schedule;

import java.util.ArrayList;
import java.util.List;

import org.se306.domain.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ScheduleController {

  private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleController.class);

  @FXML HBox scheduleHBox;

  private List<Pane> processorPanes = new ArrayList<>();

  @FXML
  private void initialize() {
    LOGGER.debug("ScheduleController initialized");

    // Example call to add rectangles and labels (adjust the number as needed)
    createHBoxWithRectanglesAndLabels(6, 100);
    ArrayList<Task> tasks = new ArrayList<>();

    // Add sample tasks to the list
    tasks.add(new Task("T1", 100, 0, 1));
    tasks.add(new Task("T2", 20, 10, 2));
    tasks.add(new Task("T3", 50, 40, 2));
    tasks.add(new Task("T4", 50, 40, 3));
    tasks.add(new Task("T5", 20, 80, 4));

    populateTaskRectangles(tasks, 100);
  }

  // Function to create rectangles with labels and add them to the scheduleHBox with even spacing
  private void createHBoxWithRectanglesAndLabels(int numberOfRectangles, int maxYValue) {
    // Set alignment to center for even spacing
    scheduleHBox.setAlignment(Pos.CENTER);
    double spacing = 10;
    scheduleHBox.setSpacing(spacing);

    // Get the width of the scheduleHBox
    double availableWidth = 220;

    // Calculate the dynamic width of each rectangle
    double totalSpacing = (numberOfRectangles - 1) * spacing;
    LOGGER.debug("Total spacing: {}", totalSpacing);
    double rectangleWidth = (availableWidth - totalSpacing) / numberOfRectangles;

    // Create a VBox to hold the y-axis numbers (only for the first rectangle)
    VBox yAxis = new VBox();
    yAxis.setAlignment(Pos.BOTTOM_RIGHT);

    // Add numbers to the y-axis (1 at the bottom, 6 at the top)
    int totalNumbers = 6;

    for (int j = 0; j < totalNumbers; j++) {
      // Calculate the label value (evenly spaced between 0 and maxYValue)
      int labelValue =
          (int) Math.round((double) maxYValue * (totalNumbers - 1 - j) / (totalNumbers - 1));

      // Create and style the label
      Label numberLabel = new Label(String.valueOf(labelValue));
      numberLabel.setStyle("-fx-font-weight: bold;");
      numberLabel.setMinWidth(25);
      numberLabel.setAlignment(Pos.CENTER_RIGHT);

      // Add the label to the yAxis VBox
      yAxis.getChildren().add(numberLabel);
    }

    // Apply the calculated spacing
    yAxis.setSpacing(10);

    for (int i = 0; i < numberOfRectangles; i++) {
      Pane processorPane = new Pane();
      processorPane.setPrefSize(rectangleWidth, 145);
      processorPane.setStyle(
          "-fx-background-color: null; -fx-border-color: #B6B9E2; -fx-border-width: 2;"
              + " -fx-background-radius: 10; -fx-border-radius: 10;");

      // Add the processorPane to the list
      processorPanes.add(processorPane);

      // Create a label for the rectangle
      Label taskLabel = new Label("P " + (i + 1));
      taskLabel.setStyle("-fx-font-weight: bold;");

      // Create a VBox to hold the label and the rectangle
      VBox rectangleVBox = new VBox(5);
      rectangleVBox.setAlignment(Pos.CENTER);

      // Add the label and rectangle to the VBox
      rectangleVBox.getChildren().addAll(taskLabel, processorPane);

      // If this is the first rectangle, add both y-axis and rectangle
      if (i == 0) {
        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(yAxis, rectangleVBox);
        scheduleHBox.getChildren().add(hbox);
      } else {
        // For subsequent rectangles, only add the rectangle
        scheduleHBox.getChildren().add(rectangleVBox);
      }
    }
  }

  // Method to populate task rectangles within processor panes
  private void populateTaskRectangles(ArrayList<Task> tasks, int maxYValue) {
    for (Task task : tasks) {
      int processorIndex = task.getProcessor() - 1;
      if (processorIndex < 0 || processorIndex >= processorPanes.size()) {
        LOGGER.warn("Task {} has invalid processor index {}", task.getId(), processorIndex);
        continue;
      }

      Pane processorPane = processorPanes.get(processorIndex);
      double paneWidth = processorPane.getPrefWidth();
      double paneHeight = processorPane.getPrefHeight() - 4;

      // Calculate the position and size of the task rectangle
      double taskStartTime = task.getStartTime();
      double taskLength = task.getTaskLength();

      // Calculate the height of the task rectangle
      double taskHeight = taskLength * paneHeight / maxYValue;

      // Calculate the y position (from top) of the task rectangle
      double yPosition = paneHeight - ((taskStartTime + taskLength) * paneHeight / maxYValue) + 2;

      // Create the task rectangle
      Rectangle taskRectangle = new Rectangle(paneWidth - 4, taskHeight);
      taskRectangle.setFill(Color.web("#b1dff7"));
      taskRectangle.setArcWidth(17);
      taskRectangle.setArcHeight(17);

      // Create a StackPane for the task rectangle and label
      StackPane taskPane = new StackPane();
      taskPane.getChildren().addAll(taskRectangle);

      // Set the position of the taskPane within the processorPane
      taskPane.setLayoutY(yPosition);

      double taskWidth = taskRectangle.getWidth();
      double layoutX = (paneWidth - taskWidth) / 2;
      taskPane.setLayoutX(layoutX);

      // Add the taskPane to the processorPane
      processorPane.getChildren().add(taskPane);
    }
  }
}
