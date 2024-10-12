package org.se306.visualisation.controllers.stats;

import org.se306.AppState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

public class StatsController {

  private static final Logger LOGGER = LoggerFactory.getLogger(StatsController.class);

  @FXML private Label percentageLabel;
  @FXML private ProgressBar cpuProgressBar;
  @FXML private Label memoryLabel;
  @FXML private ProgressBar memoryProgressBar;
  @FXML private Label processorCountLabel;
  @FXML private Label threadCountLabel;
  @FXML private Label taskCountLabel;
  @FXML private Label processorLabel;
  @FXML private Label threadLabel;
  @FXML private Label taskLabel;
  @FXML private Circle dataCircle;
  @FXML private Circle bottomCircle;
  @FXML private Circle idleCircle;
  @FXML private Label bestEtaLabel;

  private CentralProcessor processor;
  private GlobalMemory memory;
  private long[] prevTicks;
  private double weightedCpuLoad = 0.0;

  @FXML
  private void initialize() {
    LOGGER.debug("StatsController initialized");

    // Initialise system values
    setSystemValues(
        AppState.getInstance().getProcessorCount(),
        AppState.getInstance().getThreadCount(),
        AppState.getInstance().getTaskCount());

    // Reset heuristic indicators
    resetHeuristicIndicators();

    // Initialise OSHI
    SystemInfo systemInfo = new SystemInfo();
    HardwareAbstractionLayer hardware = systemInfo.getHardware();
    processor = hardware.getProcessor();
    memory = hardware.getMemory();
    prevTicks = processor.getSystemCpuLoadTicks();

    // Use Timeline for periodic updates
    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), event -> updateStats()));
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();

    // Bind the bestEtaLabel to the makespan property
    AppState.getInstance()
        .makespanProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              bestEtaLabel.setText(String.format("%d", newValue.intValue()));
            });

    // Bind the heuristic indicator to the heuristic property
    AppState.getInstance()
        .heuristicProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              setHeuristicIndicator(newValue.intValue());
            });
  }

  private void updateStats() {
    // Get CPU load
    double cpuLoad = processor.getSystemCpuLoadBetweenTicks(prevTicks);
    prevTicks = processor.getSystemCpuLoadTicks();

    // Calculate weighted average of CPU load
    weightedCpuLoad = 0.2 * cpuLoad + 0.8 * weightedCpuLoad;

    // Get memory usage
    long totalMemory = memory.getTotal();
    long availableMemory = memory.getAvailable();
    double memoryUsage = (double) (totalMemory - availableMemory) / totalMemory;

    // Calculate CPU and Memory percentages
    int cpuPercentage = (int) Math.round(weightedCpuLoad * 100);
    int memoryPercentage = (int) Math.round(memoryUsage * 100);

    // Smoothly animate CPU progress bar and update colour
    animateProgress(
        cpuProgressBar,
        cpuProgressBar.getProgress(),
        weightedCpuLoad,
        cpuPercentage,
        percentageLabel);
    String cpuColour = getColourForPercentage(cpuPercentage);
    cpuProgressBar.setStyle("-fx-accent: " + cpuColour + ";");

    // Smoothly animate Memory progress bar and update colour
    animateProgress(
        memoryProgressBar,
        memoryProgressBar.getProgress(),
        memoryUsage,
        memoryPercentage,
        memoryLabel);
    String memoryColour = getColourForPercentage(memoryPercentage);
    memoryProgressBar.setStyle("-fx-accent: " + memoryColour + ";");
  }

  private void animateProgress(
      ProgressBar progressBar, double startValue, double targetValue, int percentage, Label label) {
    Timeline timeline =
        new Timeline(
            new KeyFrame(Duration.ZERO, event -> progressBar.setProgress(startValue)),
            new KeyFrame(
                Duration.seconds(0.5),
                new javafx.animation.KeyValue(
                    progressBar.progressProperty(), targetValue, Interpolator.EASE_BOTH)));
    timeline.setOnFinished(event -> label.setText(String.format("%d%%", percentage)));
    timeline.play();
  }

  private String getColourForPercentage(int percentage) {
    if (percentage < 50) {
      return "#bef2c9"; // Light green
    } else if (percentage < 75) {
      return "#f5d3a3"; // Light orange
    } else {
      return "#f6c9c4"; // Light red
    }
  }

  private void setSystemValues(int processorCount, int threadCount, int taskCount) {
    processorCountLabel.setText(String.valueOf(processorCount));
    threadCountLabel.setText(String.valueOf(threadCount));
    taskCountLabel.setText(String.valueOf(taskCount));

    // Set the labels for processor, thread, and task with correct plurality
    processorLabel.setText(processorCount == 1 ? "Processor" : "Processors");
    threadLabel.setText(threadCount == 1 ? "Thread" : "Threads");
    taskLabel.setText(taskCount == 1 ? "Task" : "Tasks");
  }

  private void resetHeuristicIndicators() {
    String fillColor = "#292a34";
    String strokeColor = "#1b1c24";

    dataCircle.setStyle("-fx-fill: " + fillColor + "; -fx-stroke: " + strokeColor + ";");
    bottomCircle.setStyle("-fx-fill: " + fillColor + "; -fx-stroke: " + strokeColor + ";");
    idleCircle.setStyle("-fx-fill: " + fillColor + "; -fx-stroke: " + strokeColor + ";");
  }

  public void setHeuristicIndicator(int heuristic) {
    resetHeuristicIndicators();

    // Apply specific colors based on the heuristic
    switch (heuristic) {
      // Data Ready Time
      case 2:
        dataCircle.setStyle("-fx-fill: #b6b9e2; -fx-stroke: #6d6f9e;");
        break;
      // Bottom Level
      case 1:
        bottomCircle.setStyle("-fx-fill: #b6b9e2; -fx-stroke: #6d6f9e;");
        break;
      // Idle Time
      case 0:
        idleCircle.setStyle("-fx-fill: #b6b9e2; -fx-stroke: #6d6f9e;");
        break;
    }
  }
}
