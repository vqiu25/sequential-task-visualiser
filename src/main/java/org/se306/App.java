package org.se306;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.se306.domain.Task;
import org.se306.utils.GraphParser;
import org.slf4j.Logger;

/** Hello world! */
public class App {

  private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(App.class);

  public static void main(String[] args) {
    LOGGER.info("Hello World!");
    Graph<Task, DefaultWeightedEdge> graph = GraphParser.dotToGraph("dot/Nodes_7_OutTree.dot");
    GraphParser.graphToDot(graph, "test/App.dot");
  }
}
