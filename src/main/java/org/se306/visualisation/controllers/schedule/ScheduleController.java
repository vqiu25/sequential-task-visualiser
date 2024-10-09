package org.se306.visualisation.controllers.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ScheduleController {

  private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleController.class);

  @FXML StackPane scheduleStackPane;
  @FXML HBox scheduleHBox;

  @FXML
  private void initialize() {
    LOGGER.debug("ScheduleController initialized");

    // Example call to add rectangles and labels (adjust the number as needed)
    createHBoxWithRectanglesAndLabels(8, 200);
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
      // Create a rectangle with specified width, height, and color
      Rectangle rectangle = new Rectangle(rectangleWidth, 145);
      rectangle.setFill(null);
      rectangle.setStroke(Color.web("#B6B9E2"));
      rectangle.setStrokeWidth(2);
      rectangle.setArcWidth(10);
      rectangle.setArcHeight(10);

      // Create a label for the rectangle
      Label taskLabel = new Label("P " + (i + 1));
      taskLabel.setStyle("-fx-font-weight: bold;"); // Bold label

      // Create a VBox to hold the label and the rectangle
      VBox rectangleVBox = new VBox(5);
      rectangleVBox.setAlignment(Pos.CENTER);

      // Add the label and rectangle to the VBox
      rectangleVBox.getChildren().addAll(taskLabel, rectangle);

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
}
