package org.se306.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

public class State {

  private Map<String, StateTask> taskInfoMap;
  private Set<String> unscheduledTasks;
  private int gScore;
  private int fScore;
  private int[] processorAvailableTime;

  // Initial state constructor
  public State(int numProcessors) {
    this.taskInfoMap = new HashMap<>();
    this.unscheduledTasks = new HashSet<>();
    this.gScore = 0;
    this.fScore = 0;
    this.processorAvailableTime = new int[numProcessors];
    Arrays.fill(this.processorAvailableTime, 0);
  }

  // Copy constructor
  public State copyOf() {
    State newState = new State(this.getNumProcessors());
    newState.taskInfoMap = new HashMap<>(this.taskInfoMap);
    newState.unscheduledTasks = new HashSet<>(this.unscheduledTasks);
    newState.gScore = this.gScore;
    newState.fScore = this.fScore;
    newState.processorAvailableTime = Arrays.copyOf(this.processorAvailableTime, this.getNumProcessors());
    return newState;
  }

  // Get a unique key representing the state
  public String getStateKey() {
    // Key based on scheduled tasks and their assignments
    StringBuilder keyBuilder = new StringBuilder();
    taskInfoMap.entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .forEach(
            entry ->
                keyBuilder
                    .append(entry.getKey())
                    .append(":")
                    .append(entry.getValue().processor)
                    .append(":")
                    .append(entry.getValue().startTime)
                    .append("|"));
    return keyBuilder.toString();
  }

  // Get tasks that are ready to be scheduled
  public List<IOTask> getReadyTasks(Graph<IOTask, DefaultWeightedEdge> graph) {
    List<IOTask> readyTasks = new ArrayList<>();
    for (String taskId : unscheduledTasks) {
      IOTask task = getTaskById(taskId, graph);
      boolean allPredecessorsScheduled = true;
      for (DefaultWeightedEdge edge : graph.incomingEdgesOf(task)) {
        IOTask predecessor = graph.getEdgeSource(edge);
        if (!taskInfoMap.containsKey(predecessor.getId())) {
          allPredecessorsScheduled = false;
          break;
        }
      }
      if (allPredecessorsScheduled) {
        readyTasks.add(task);
      }
    }
    return readyTasks;
  }

  // Schedule a task and return a new state
  public State scheduleTask(IOTask task, int processor, Graph<IOTask, DefaultWeightedEdge> graph) {
    State newState = this.copyOf();

    // Determine earliest start time
    int earliestStartTime = newState.processorAvailableTime[processor];

    // Consider dependencies
    for (DefaultWeightedEdge edge : graph.incomingEdgesOf(task)) {
      IOTask predecessor = graph.getEdgeSource(edge);
      StateTask predecessorInfo = newState.taskInfoMap.get(predecessor.getId());

      // Check if predecessorInfo is null (should not happen)
      if (predecessorInfo == null) {
        throw new RuntimeException("Predecessor " + predecessor.getId() + " not scheduled yet.");
      }

      int finishTime = predecessorInfo.startTime + predecessorInfo.duration;
      int communicationDelay = (int) graph.getEdgeWeight(edge);

      if (predecessorInfo.processor != processor) {
        finishTime += communicationDelay;
      }

      earliestStartTime = Math.max(earliestStartTime, finishTime);
    }

    // Schedule the task
    int taskDuration = task.getTaskLength();
    newState.taskInfoMap.put(
        task.getId(), new StateTask(processor, earliestStartTime, taskDuration));
    newState.processorAvailableTime[processor] = earliestStartTime + taskDuration;
    newState.unscheduledTasks.remove(task.getId());

    // Update gScore (makespan)
    newState.gScore = newState.getMakespan();

    return newState;
  }

  // Get the makespan of the current schedule
  public int getMakespan() {
    return Arrays.stream(processorAvailableTime).max().orElse(0);
  }

  // Calculate idle time for all processors (this is important for oliver's heurstiic)
  public int getIdleTime() {
    int makespan = getMakespan();
    int totalUsedTime = 0;
    for (int availableTime : processorAvailableTime) {
      totalUsedTime += availableTime;
    }
    // Idle time is the difference between makespan * number of processors and total used time
    return (makespan * getNumProcessors()) - totalUsedTime;
  }

  // Helper method to get Task by ID
  public IOTask getTaskById(String id, Graph<IOTask, DefaultWeightedEdge> graph) {
    for (IOTask task : graph.vertexSet()) {
      if (task.getId().equals(id)) {
        return task;
      }
    }
    return null;
  }

  public Map<String, StateTask> getTaskInfoMap() {
    return taskInfoMap;
  }

  public void setTaskMap(Map<String, StateTask> taskInfoMap) {
    this.taskInfoMap = taskInfoMap;
  }

  public Set<String> getUnscheduledTasks() {
    return unscheduledTasks;
  }

  public void setUnscheduledTasks(Set<String> unscheduledTasks) {
    this.unscheduledTasks = unscheduledTasks;
  }

  public int getgScore() {
    return gScore;
  }

  public void setgScore(int gScore) {
    this.gScore = gScore;
  }

  public int getfScore() {
    return fScore;
  }

  public void setfScore(int fScore) {
    this.fScore = fScore;
  }

  public int[] getProcessorAvailableTime() {
    return processorAvailableTime;
  }

  public void setProcessorAvailableTime(int[] processorAvailableTime) {
    this.processorAvailableTime = processorAvailableTime;
  }

  private int getNumProcessors() {
    return processorAvailableTime.length;
  }
}
