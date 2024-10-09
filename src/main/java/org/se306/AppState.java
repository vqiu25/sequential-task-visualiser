package org.se306;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.se306.domain.Task;
import org.se306.utils.SchedulerCommand;

public class AppState {

  private static AppState instance;

  private Graph<Task, DefaultWeightedEdge> graph;

  private SchedulerCommand command;

  public static AppState getInstance() {
    if (instance == null) {
      instance = new AppState();
    }
    return instance;
  }

  public Graph<Task, DefaultWeightedEdge> getGraph() {
    return graph;
  }

  public void setGraph(Graph<Task, DefaultWeightedEdge> graph) {
    this.graph = graph;
  }

  public SchedulerCommand getCommand() {
    return command;
  }

  public void setCommand(SchedulerCommand command) {
    this.command = command;
  }
}
