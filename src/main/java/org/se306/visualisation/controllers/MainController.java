package org.se306.visualisation.controllers;

import javafx.fxml.FXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainController {

  private static final Logger LOGGER = LoggerFactory.getLogger(WrapperController.class);

  @FXML
  private void initialize() {
    LOGGER.debug("MainController initialized");
  }
}
