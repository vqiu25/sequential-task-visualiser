package org.se306;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.se306.domain.IOTask;
import org.se306.utils.SchedulerCommand;

public class AppState {

  private static AppState instance;

  private boolean running;

  private Graph<IOTask, DefaultWeightedEdge> graph;

  private SchedulerCommand command;

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

  public SchedulerCommand getCommand() {
    return command;
  }

  public void setCommand(SchedulerCommand command) {
    this.command = command;
  }

  public boolean isRunning() {
    return running;
  }

  public void setRunning(boolean running) {
    this.running = running;
  }
}
