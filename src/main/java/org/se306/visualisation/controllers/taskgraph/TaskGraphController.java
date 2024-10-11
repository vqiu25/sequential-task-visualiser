package org.se306.visualisation.controllers.taskgraph;

import javafx.fxml.FXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskGraphController {

  private static final Logger LOGGER = LoggerFactory.getLogger(TaskGraphController.class);

  @FXML
  private void initialize() {
    LOGGER.debug("TaskGraphController initialized");
  }
}
