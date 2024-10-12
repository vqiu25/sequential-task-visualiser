package org.se306.visualisation.controllers.taskgraph;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graphview.ForceDirectedSpringGravityLayoutStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartRadiusProvider;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

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
    graph.insertVertex("G");
    graph.insertVertex("H");
    graph.insertVertex("I");
    graph.insertVertex("J");
    graph.insertVertex("K");
    graph.insertVertex("L");
    graph.insertVertex("M");
    graph.insertVertex("N");
    graph.insertVertex("O");
    graph.insertVertex("P");
    graph.insertVertex("Q");
    graph.insertVertex("R");
    graph.insertVertex("S");

    // Connect vertices with edges
    graph.insertEdge("A", "B", "EdgeAB");
    graph.insertEdge("A", "C", "EdgeAC");
    graph.insertEdge("A", "D", "EdgeAD");
    graph.insertEdge("B", "E", "EdgeBE");
    graph.insertEdge("C", "F", "EdgeCF");
    graph.insertEdge("D", "G", "EdgeDG");
    graph.insertEdge("E", "H", "EdgeEH");
    graph.insertEdge("F", "I", "EdgeFI");
    graph.insertEdge("G", "J", "EdgeGJ");
    graph.insertEdge("H", "K", "EdgeHK");
    graph.insertEdge("I", "L", "EdgeIL");
    graph.insertEdge("J", "M", "EdgeJM");
    graph.insertEdge("K", "N", "EdgeKN");
    graph.insertEdge("L", "O", "EdgeLO");
    graph.insertEdge("M", "P", "EdgeMP");
    graph.insertEdge("N", "Q", "EdgeNQ");
    graph.insertEdge("O", "R", "EdgeOR");
    graph.insertEdge("P", "S", "EdgePS");
    graph.insertEdge("R", "A", "EdgeRA");
    graph.insertEdge("S", "B", "EdgeSB");
    graph.insertEdge("J", "L", "EdgeJL");
    graph.insertEdge("E", "N", "EdgeEN");
    graph.insertEdge("C", "S", "EdgeCS");
    graph.insertEdge("M", "H", "EdgeMH");

    SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();

    ForceDirectedSpringGravityLayoutStrategy<String> layoutStrategy =
        new ForceDirectedSpringGravityLayoutStrategy<>(1.5, 2.0, 8.0, 0.1, 0.001);

    SmartGraphPanel<String, String> graphView =
        new SmartGraphPanel<>(graph, strategy, layoutStrategy);

    int numVertices = graph.vertices().size();
    SmartRadiusProvider<String> radiusProvider = vertex -> (numVertices >= 20) ? 5.0 : 10.0;
    graphView.setVertexRadiusProvider(radiusProvider);

    graphView.setAutomaticLayout(true);

    // ContentZoomScrollPane zoomPane = new ContentZoomScrollPane(graphView);

    // Add the graph view to the StackPane
    graphStackPane.getChildren().add(graphView);

    // Delay the graphView initialisation until after the scene has been rendered
    Platform.runLater(
        () -> {
          graphView.init();
          graphView.update(); // Ensure radius changes are applied
        });
  }
}
