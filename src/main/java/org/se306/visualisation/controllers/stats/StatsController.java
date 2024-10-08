package org.se306.visualisation.controllers.stats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    // Get memory usage
    long totalMemory = memory.getTotal();
    long availableMemory = memory.getAvailable();
    double memoryUsage = (double) (totalMemory - availableMemory) / totalMemory;

    // Calculate CPU and Memory percentages
    int cpuPercentage = (int) Math.round(cpuLoad * 100);
    int memoryPercentage = (int) Math.round(memoryUsage * 100);

    // Update CPU progress bar and label
    cpuProgressBar.setProgress(cpuLoad);
    percentageLabel.setText(String.format("%d%%", cpuPercentage));

    // Update Memory progress bar and label
    memoryProgressBar.setProgress(memoryUsage);
    memoryLabel.setText(String.format("%d%%", memoryPercentage));

    // Adjust the color of the CPU progress bar based on the percentage
    String cpuColor = getColorForPercentage(cpuPercentage);
    cpuProgressBar.setStyle("-fx-accent: " + cpuColor + ";");

    // Adjust the color of the Memory progress bar based on the percentage
    String memoryColor = getColorForPercentage(memoryPercentage);
    memoryProgressBar.setStyle("-fx-accent: " + memoryColor + ";");
  }

  private String getColorForPercentage(int percentage) {
    if (percentage < 50) {
      return "#bef2c9";
    } else if (percentage < 75) {
      return "#f5d3a3";
    } else {
      return "#f6c9c4";
    }
  }
}
