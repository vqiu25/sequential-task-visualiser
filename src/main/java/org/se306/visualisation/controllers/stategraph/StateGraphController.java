package org.se306.visualisation.controllers.stategraph;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;

public class StateGraphController {

  private static final Logger LOGGER = LoggerFactory.getLogger(StateGraphController.class);

  @FXML
  private void initialize() {
    LOGGER.debug("StateGraphController initialized");
  }
}
