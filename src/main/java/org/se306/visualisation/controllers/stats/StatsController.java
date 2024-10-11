package org.se306.visualisation.controllers.stats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
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

  private CentralProcessor processor;
  private GlobalMemory memory;
  private long[] prevTicks;
  private double weightedCpuLoad = 0.0;

  @FXML
  private void initialize() {
    LOGGER.debug("StatsController initialized");

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
}
