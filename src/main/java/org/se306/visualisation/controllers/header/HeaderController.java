package org.se306.visualisation.controllers.header;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;

public class HeaderController {

  private static final Logger LOGGER = LoggerFactory.getLogger(HeaderController.class);

  @FXML
  private void initialize() {
    LOGGER.debug("HeaderController initialized");
  }
}
