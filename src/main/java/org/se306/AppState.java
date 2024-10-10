package org.se306;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.se306.domain.IOTask;

public class AppState {

  private static AppState instance;

  private Graph<IOTask, DefaultWeightedEdge> graph;

  public static AppState getInstance() {
    if (instance == null) {
      instance = new AppState();
    }
    return instance;
  }

  public void setGraph(Graph<IOTask, DefaultWeightedEdge> graph) {
    this.graph = graph;
  }

  public Graph<IOTask, DefaultWeightedEdge> getGraph() {
    return graph;
  }
}
