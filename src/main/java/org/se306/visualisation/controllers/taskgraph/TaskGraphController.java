package org.se306.visualisation.controllers.taskgraph;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.se306.AppState;
import org.se306.domain.IOTask;
import org.se306.visualisation.controllers.shared.ColourMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graphview.ForceDirectedSpringGravityLayoutStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartRadiusProvider;
import com.brunomnsilva.smartgraph.graphview.SmartStylableNode;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class TaskGraphController {

  private static final Logger LOGGER = LoggerFactory.getLogger(TaskGraphController.class);

  @FXML private StackPane graphStackPane;

  @FXML
  private void initialize() {
    LOGGER.debug("TaskGraphController initialized");

    // Get the graph from AppState
    Graph<IOTask, DefaultWeightedEdge> jGraphT = AppState.getInstance().getGraph();

    // Convert JGraphT graph to the SmartGraph compatible graph
    Digraph<String, String> graph = convertToSmartGraph(jGraphT);

    SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();

    ForceDirectedSpringGravityLayoutStrategy<String> layoutStrategy =
        new ForceDirectedSpringGravityLayoutStrategy<>(1.5, 2.0, 8.0, 0.1, 0.001);

    SmartGraphPanel<String, String> graphView =
        new SmartGraphPanel<>(graph, strategy, layoutStrategy);

    graph
        .vertices()
        .forEach(
            vertex -> {
              SmartStylableNode node = graphView.getStylableVertex(vertex);
              String[] colors =
                  ColourMapping.getColours(
                      extractVertexId(vertex.toString())); // Fetch colors based on vertex ID
              node.setStyleInline("-fx-fill: " + colors[0] + "; -fx-stroke: " + colors[1]);
            });

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

  private Digraph<String, String> convertToSmartGraph(Graph<IOTask, DefaultWeightedEdge> jGraphT) {
    Digraph<String, String> graph = new DigraphEdgeList<>();
    jGraphT.vertexSet().forEach(vertex -> graph.insertVertex(vertex.toString()));
    jGraphT
        .edgeSet()
        .forEach(
            edge -> {
              IOTask source = jGraphT.getEdgeSource(edge);
              IOTask target = jGraphT.getEdgeTarget(edge);
              graph.insertEdge(source.toString(), target.toString(), edge.toString());
            });
    return graph;
  }

  public String extractVertexId(String vertexString) {
    Pattern pattern = Pattern.compile("Vertex\\{(.*?)\\}");
    Matcher matcher = pattern.matcher(vertexString);
    if (matcher.find()) {
      return matcher.group(1); // Returns the content within the curly braces
    }
    return null;
  }
}
