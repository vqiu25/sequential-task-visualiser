package org.se306.visualisation.controllers.header;

import javafx.fxml.FXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeaderController {

  private static final Logger LOGGER = LoggerFactory.getLogger(HeaderController.class);

  @FXML
  private void initialize() {
    LOGGER.debug("HeaderController initialized");
  }
}
