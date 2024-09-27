package org.se306;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.se306.domain.Task;

public class AppState {

  private static AppState instance;

  private Graph<Task, DefaultWeightedEdge> graph;

  public static AppState getInstance() {
    if (instance == null) {
      instance = new AppState();
    }
    return instance;
  }

  public void setGraph(Graph<Task, DefaultWeightedEdge> graph) {
    this.graph = graph;
  }

  public Graph<Task, DefaultWeightedEdge> getGraph() {
    return graph;
  }
}
