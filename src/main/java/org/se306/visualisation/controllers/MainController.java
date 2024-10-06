package org.se306.visualisation.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;

public class MainController {

  private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

  @FXML
  private void initialize() {
    LOGGER.debug("MainController initialized");
  }
}
