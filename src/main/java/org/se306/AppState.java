package org.se306;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.se306.domain.IOTask;
import org.se306.domain.State;
import org.se306.utils.SchedulerCommand;

public class AppState {

  private static AppState instance;

  private BlockingQueue<State> stateQueue = new LinkedBlockingQueue<>();
  private final IntegerProperty fScoreProperty = new SimpleIntegerProperty(0);
  private final IntegerProperty heuristicTypeProperty = new SimpleIntegerProperty(0);
  private boolean running = true;

  private Graph<IOTask, DefaultWeightedEdge> graph;
  private final ObjectProperty<State> currentStateProperty = new SimpleObjectProperty<>();
  private int processorCount;
  private int threadCount;
  private int taskCount;

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

  public ObjectProperty<State> currentStateProperty() {
    return currentStateProperty;
  }

  public State getCurrentState() {
    return currentStateProperty.get();
  }

  public void setCurrentState(State state) {
    this.currentStateProperty.set(state);
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

  public IntegerProperty fScoreProperty() {
    return fScoreProperty;
  }

  public int getFScoreProperty() {
    return fScoreProperty.get();
  }

  public void setFScoreProperty(int fScore) {
    this.fScoreProperty.set(fScore);
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
