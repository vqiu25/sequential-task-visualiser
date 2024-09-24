package org.se306;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.se306.domain.Task;
import org.se306.utils.GraphParser;

/** Hello world! */
public class App {
  public static void main(String[] args) {
    System.out.println("Hello World!");
    Graph<Task, DefaultWeightedEdge> graph = GraphParser.dotToGraph("dot/Nodes_7_OutTree.dot");
    GraphParser.graphToDot(graph, "test/App.dot");
  }
}
