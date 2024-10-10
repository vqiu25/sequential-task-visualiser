package org.se306.visualisation.controllers.taskgraph;

import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskGraphController {

  private static final Logger LOGGER = LoggerFactory.getLogger(TaskGraphController.class);

  @FXML private StackPane graphStackPane;

  @FXML
  private void initialize() {
    LOGGER.debug("TaskGraphController initialized");

    // Create the graph and set its layout strategy
    Digraph<String, String> graph = new DigraphEdgeList<>();
    graph.insertVertex("A");
    graph.insertVertex("B");
    graph.insertVertex("C");
    graph.insertVertex("D");
    graph.insertVertex("E");
    graph.insertVertex("F");
    graph.insertEdge("A", "B", "EdgeAB");
    graph.insertEdge("A", "C", "EdgeAC");
    graph.insertEdge("A", "D", "EdgeAD");
    graph.insertEdge("D", "E", "EdgeDE");
    graph.insertEdge("D", "F", "EdgeDF");

    SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
    SmartGraphPanel<String, String> graphView = new SmartGraphPanel<>(graph, strategy);
    graphView.setAutomaticLayout(true);

    // ContentZoomScrollPane zoomPane = new ContentZoomScrollPane(graphView);

    // Add the graph view to the StackPane
    graphStackPane.getChildren().add(graphView);

    // Delay the graphView initialisation until after the scene has been rendered
    Platform.runLater(
        () -> {
          graphView.init();

          // Apply the custom CSS stylesheet
          Scene scene = graphStackPane.getScene();
          if (scene != null) {
            scene
                .getStylesheets()
                .add(getClass().getResource("/org/se306/css/smartgraph.css").toExternalForm());
          }
        });
  }
}
