package org.se306;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.se306.domain.IOTask;
import org.se306.domain.State;

public class AppState {

  private static AppState instance;

  private Graph<IOTask, DefaultWeightedEdge> graph;
  private State currentState;
  private int processorCount;
  private int threadCount;
  private int taskCount;

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

  public int getProcessorCount() {
    return processorCount;
  }

  public void setProcessorCount(int processorCount) {
    this.processorCount = processorCount;
  }

  public int getThreadCount() {
    return threadCount;
  }

  public void setThreadCount(int threadCount) {
    this.threadCount = threadCount;
  }

  public int getTaskCount() {
    return taskCount;
  }

  public void setTaskCount(int taskCount) {
    this.taskCount = taskCount;
  }

  public void setCurrentState(State state) {
    this.currentState = state;
  }

  public State getCurrentState() {
    return currentState;
  }
}
