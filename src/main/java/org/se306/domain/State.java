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

  /**
   * Indexes represent 0-indexed processors, value is the finish time of last scheduled task on the
   * processor
   */
  private int[] processorAvailableTimes;

  /** fScore = idleTime + bottomLevel + dataReadyTime */
  private int fScore;

  // --- The following fields are used for dynamic programming only ---
  // They're updated at the end of each scheduleTask() call
  private int makespan;
  private int idleTime; // Sum of idle time (so far) on all processors
  private int bottomLevelEta; // Max of start times + bottom levels of all scheduled tasks
  private int heuristicType;

  /** Initial state constructor */
  public State(int numProcessors) {
    this.idsToStateTasks = new HashMap<>();
    this.unscheduledTaskIds = new HashSet<>();
    this.fScore = 0;
    this.makespan = 0;
    this.idleTime = 0;
    this.bottomLevelEta = -1; // Invalid at first
    this.processorAvailableTimes = new int[numProcessors];
    Arrays.fill(this.processorAvailableTimes, 0);
  }

  /** Returns deep clone of this state */
  public State copyOf() {
    State newState = new State(this.getNumProcessors());
    newState.idsToStateTasks = new HashMap<>(this.idsToStateTasks);
    newState.unscheduledTaskIds = new HashSet<>(this.unscheduledTaskIds);
    newState.fScore = this.fScore;
    newState.makespan = this.makespan;
    newState.idleTime = this.idleTime;
    newState.processorAvailableTimes =
        Arrays.copyOf(this.processorAvailableTimes, this.getNumProcessors());
    return newState;
  }

  // Get tasks that are ready to be scheduled
  // TODO: track number of remaining parents in StateTask
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
  // TODO: track DRT(processor) in StateTask
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
    newState.idsToStateTasks.put(task.getId(), new StateTask(task, processor, earliestStartTime));
    newState.unscheduledTaskIds.remove(task.getId());

    // Update bookkeeping
    newState.idleTime += earliestStartTime - newState.processorAvailableTimes[processor];
    newState.processorAvailableTimes[processor] = earliestStartTime + task.getTaskLength();
    newState.makespan = Math.max(this.makespan, newState.getProcessorAvailableTimes()[processor]);
    int newPotentialBottomLevelETA = earliestStartTime + task.getBottomLevel();
    newState.bottomLevelEta = Math.max(this.bottomLevelEta, newPotentialBottomLevelETA);

    return newState;
  }

  // Helper method to get Task by ID
  // TODO: move to a utility class + optimise with hashmap
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

  public Set<String> getUnscheduledTaskIds() {
    return unscheduledTaskIds;
  }

  public int getfScore() {
    return fScore;
  }

  public void setfScore(int fScore) {
    this.fScore = fScore;
  }

  public int getMakespan() {
    return makespan;
  }

  public void setMakespan(int gScore) {
    this.makespan = gScore;
  }

  public int getIdleTime() {
    return idleTime;
  }

  public int[] getProcessorAvailableTimes() {
    return processorAvailableTimes;
  }

  public int getNumProcessors() {
    return processorAvailableTimes.length;
  }

  public int getBottomLevelEta() {
    return bottomLevelEta;
  }

  public int getHeuristicType() {
    return heuristicType;
  }

  public void setHeuristicType(int heuristic) {
    this.heuristicType = heuristic;
  }

  /** Helper method to get StateTask by IOTask */
  public StateTask getStateTaskFromIOTask(IOTask task) {
    return idsToStateTasks.get(task.getId());
  }

  /**
   * Sorry, this method isn't documented very well and I don't intend to until a later PR when I
   * might make it faster :) It's exactly what Oliver says to do in [22]
   */
  public int getDRT(Graph<IOTask, DefaultWeightedEdge> graph) {
    int DRTPlusBottomLevel = 0; // Maximise
    for (IOTask ioTask : getReadyTasks(graph)) {
      int DRTForNode = Integer.MAX_VALUE; // Minimise
      for (int processor = 0; processor < getNumProcessors(); processor++) {
        int DRTForNodeProcessor = 0; // Maximise
        for (DefaultWeightedEdge inEdge : graph.incomingEdgesOf(ioTask)) {
          String parentId = graph.getEdgeSource(inEdge).getId();
          StateTask parent = idsToStateTasks.get(parentId);
          int communicationDelay =
              (processor == parent.getProcessor()) ? 0 : (int) graph.getEdgeWeight(inEdge);
          DRTForNodeProcessor =
              Math.max(DRTForNodeProcessor, parent.getEndTime() + communicationDelay);
        }
        DRTForNode = Math.min(DRTForNode, DRTForNodeProcessor);
      }
      DRTPlusBottomLevel = Math.max(DRTPlusBottomLevel, DRTForNode + ioTask.getBottomLevel());
    }
    return DRTPlusBottomLevel;
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
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    State other = (State) obj;
    if (idsToStateTasks == null) {
      if (other.idsToStateTasks != null) return false;
    } else if (!idsToStateTasks.equals(other.idsToStateTasks)) return false;
    return true;
  }
}
