package org.se306.visualisation.controllers.header;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import org.se306.AppState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeaderController {

  private static final Logger LOGGER = LoggerFactory.getLogger(HeaderController.class);

  private Timeline timeline;
  private long storedSeconds;
  private Instant startTime;

  @FXML private Label timerLabel;
  @FXML private Circle playPause; // TODO: implement playPause button

  @FXML
  private void initialize() {
    LOGGER.debug("HeaderController initialized");

    timeline = new Timeline(new KeyFrame(Duration.millis(1000), event -> updateTimer()));
    timeline.setCycleCount(Timeline.INDEFINITE);
    playPause.setOnMouseClicked(this::handlePlayPause);

    // timeline.stop(); // TODO: add event listener for when the algorithm finishes to stop timer?
  }

  private void handlePlayPause(MouseEvent event) {
    if (storedSeconds == 0) {
      startAlgorithm();
    } else if (AppState.getInstance().isRunning()) { // or however we know the algorithm is running
      pauseAlgorithm();
    } else {
      resumeAlgorithm();
    }
  }

  private void startAlgorithm() {
    AppState.getInstance().setRunning(true);
    storedSeconds = 0;
    startTime = Instant.now();
    timeline.play();
  }

  private void updateTimer() {
    Instant now = Instant.now();
    long secondsElapsed = ChronoUnit.SECONDS.between(startTime, now) + storedSeconds;
    long minutes = secondsElapsed / 60;
    long seconds = secondsElapsed % 60;
    timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
  }

  private void pauseAlgorithm() {
    // TODO: or whatever way we have of notifying the algorithm to pause
    AppState.getInstance().setRunning(false);
    long secondsElapsed = ChronoUnit.SECONDS.between(startTime, Instant.now());
    storedSeconds = storedSeconds + secondsElapsed;
    timeline.pause();
  }

  private void resumeAlgorithm() {
    startTime = Instant.now();
    timeline.play();
  }
}
