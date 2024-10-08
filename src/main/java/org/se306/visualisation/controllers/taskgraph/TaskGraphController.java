package org.se306.visualisation.controllers.taskgraph;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;

public class TaskGraphController {

  private static final Logger LOGGER = LoggerFactory.getLogger(TaskGraphController.class);

  @FXML
  private void initialize() {
    LOGGER.debug("TaskGraphController initialized");
  }
}
