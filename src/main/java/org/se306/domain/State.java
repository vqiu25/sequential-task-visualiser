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

  /** IOTask IDs --> StateTasks */
  private Map<String, StateTask> idsToStateTasks;

  /** IOTask IDs of unscheduled tasks */
  private Set<String> unscheduledTaskIds;

  /** Indexes represent 0-indexed processors, value is the finish time of last scheduled task on the processor */
  private int[] processorAvailableTimes;

  private int gScore;
  private int fScore;

  /** Initial state constructor */
  public State(int numProcessors) {
    this.idsToStateTasks = new HashMap<>();
    this.unscheduledTaskIds = new HashSet<>();
    this.gScore = 0;
    this.fScore = 0;
    this.processorAvailableTimes = new int[numProcessors];
    Arrays.fill(this.processorAvailableTimes, 0);
  }

  /** Returns deep clone of this state */
  public State copyOf() {
    State newState = new State(this.getNumProcessors());
    newState.idsToStateTasks = new HashMap<>(this.idsToStateTasks);
    newState.unscheduledTaskIds = new HashSet<>(this.unscheduledTaskIds);
    newState.gScore = this.gScore;
    newState.fScore = this.fScore;
    newState.processorAvailableTimes = Arrays.copyOf(this.processorAvailableTimes, this.getNumProcessors());
    return newState;
  }

  // Get tasks that are ready to be scheduled
  public List<IOTask> getReadyTasks(Graph<IOTask, DefaultWeightedEdge> graph) {
    List<IOTask> readyTasks = new ArrayList<>();
    for (String taskId : unscheduledTaskIds) {
      IOTask task = getTaskById(taskId, graph);
      boolean allPredecessorsScheduled = true;
      for (DefaultWeightedEdge edge : graph.incomingEdgesOf(task)) {
        IOTask predecessor = graph.getEdgeSource(edge);
        if (!idsToStateTasks.containsKey(predecessor.getId())) {
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
    int earliestStartTime = newState.processorAvailableTimes[processor];

    // Consider dependencies
    for (DefaultWeightedEdge edge : graph.incomingEdgesOf(task)) {
      IOTask predecessor = graph.getEdgeSource(edge);
      StateTask predecessorInfo = newState.idsToStateTasks.get(predecessor.getId());

      // Check if predecessorInfo is null (should not happen)
      if (predecessorInfo == null) {
        throw new RuntimeException("Predecessor " + predecessor.getId() + " not scheduled yet.");
      }

      int finishTime = predecessorInfo.getStartTime() + predecessorInfo.getDuration();
      int communicationDelay = (int) graph.getEdgeWeight(edge);

      if (predecessorInfo.getProcessor() != processor) {
        finishTime += communicationDelay;
      }

      earliestStartTime = Math.max(earliestStartTime, finishTime);
    }

    // Schedule the task
    newState.idsToStateTasks.put(
        task.getId(), new StateTask(task, processor, earliestStartTime));
    newState.processorAvailableTimes[processor] = earliestStartTime + task.getTaskLength();
    newState.unscheduledTaskIds.remove(task.getId());

    // Update gScore (makespan)
    newState.gScore = newState.getMakespan();

    return newState;
  }

  // Get the makespan of the current schedule
  public int getMakespan() {
    return Arrays.stream(processorAvailableTimes).max().orElse(0);
  }

  // Calculate idle time for all processors (this is important for oliver's heurstiic)
  public int getIdleTime() {
    int makespan = getMakespan();
    int totalUsedTime = 0;
    for (int availableTime : processorAvailableTimes) {
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

  public Map<String, StateTask> getIdsToStateTasks() {
    return idsToStateTasks;
  }

  public void setTaskMap(Map<String, StateTask> taskInfoMap) {
    this.idsToStateTasks = taskInfoMap;
  }

  public Set<String> getUnscheduledTaskIds() {
    return unscheduledTaskIds;
  }

  public void setUnscheduledTaskIds(Set<String> unscheduledTasks) {
    this.unscheduledTaskIds = unscheduledTasks;
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

  public int[] getProcessorAvailableTimes() {
    return processorAvailableTimes;
  }

  public void setProcessorAvailableTimes(int[] processorAvailableTime) {
    this.processorAvailableTimes = processorAvailableTime;
  }

  private int getNumProcessors() {
    return processorAvailableTimes.length;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((idsToStateTasks == null) ? 0 : idsToStateTasks.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    State other = (State) obj;
    if (idsToStateTasks == null) {
      if (other.idsToStateTasks != null)
        return false;
    } else if (!idsToStateTasks.equals(other.idsToStateTasks))
      return false;
    return true;
  }

}
