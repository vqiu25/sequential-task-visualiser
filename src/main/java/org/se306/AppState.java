package org.se306;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.se306.domain.IOTask;
import org.se306.domain.State;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class AppState {

  private static AppState instance;

  private BlockingQueue<State> stateQueue = new LinkedBlockingQueue<>();
  private final IntegerProperty makespanProperty = new SimpleIntegerProperty(0);
  private final IntegerProperty heuristicTypeProperty = new SimpleIntegerProperty(0);
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

  public BlockingQueue<State> getStateQueue() {
    return stateQueue;
  }

  public void addStateToQueue(State state) {
    try {
      stateQueue.put(state); // Blocking if the queue is full
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  public IntegerProperty makespanProperty() {
    return makespanProperty;
  }

  public int getMakespan() {
    return makespanProperty.get();
  }

  public void setMakespan(int makespan) {
    this.makespanProperty.set(makespan);
  }

  public IntegerProperty heuristicProperty() {
    return heuristicTypeProperty;
  }

  public int getHeuristicType() {
    return heuristicTypeProperty.get();
  }

  public void setHeuristicType(int heuristicType) {
    this.heuristicTypeProperty.set(heuristicType);
  }
}
