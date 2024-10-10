package org.se306.helpers;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.se306.domain.IOTask;
import org.se306.domain.State;
import org.se306.domain.StateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class to calculate the heuristic estimate f(s) for the A* search
 * algorithm.
 */
// TODO: need to optimize all the methods in this class
public class FFunction {

  private static final Logger LOGGER = LoggerFactory.getLogger(FFunction.class);

  /**
   * Calculates the h(s) part of f(s) = g(s) + h(s)
   *
   * - f(s) is the ETA of the current state s
   * - g(s) is the makespan of the current state s
   * - h(s) is the heuristic estimate of the remaining time to complete the schedule
   */
  public static int heuristicEstimate(State state, Graph<IOTask, DefaultWeightedEdge> graph, int numProcessors,
      int totalComputeTime) {
    // int idleTimeEstimate = estimateIdleTime(state, graph, numProcessors, totalComputeTime);
    int bottomLevelEstimate = estimateBottomLevel(state, graph);
    // int dataReadyTimeEstimate = estimateDataReadyTime(state, graph, numProcessors);

    // // Return the maximum of the three components (as written in Oliver's paper)
    return bottomLevelEstimate;
    // return Math.max(Math.max(idleTimeEstimate, bottomLevelEstimate), dataReadyTimeEstimate);
  }

  /**
   * Estimate the difference between the ETA calculated from the idle-time
   * estimate and the current makespan.
   *
   * @return The estimated idle time or 0 if the idle time estimate is less than the current makespan
   */
  private static int estimateIdleTime(State state, Graph<IOTask, DefaultWeightedEdge> graph, int numProcessors,
      int totalComputationTime) {
    int idleTimeEtaEstimate = (totalComputationTime + state.getIdleTime()) / numProcessors; // ETA based on idle time
    return Math.max(idleTimeEtaEstimate - state.getMakespan(), 0);
  }

  // Estimate the bottom level for tasks already scheduled
  private static int estimateBottomLevel(State state, Graph<IOTask, DefaultWeightedEdge> graph) {
    return state.getBottomLevel();



    // int maxBottomLevel = 0;
    // for (StateTask taskInfo : state.getIdsToStateTasks().values()) {
    //   IOTask task = state.getTaskById(taskInfo.getProcessor() + "", graph);
    //   int bottomLevel = calculateBottomLevel(task, state, graph);
    //   maxBottomLevel = Math.max(maxBottomLevel, taskInfo.getStartTime() + bottomLevel);
    // }
    // return maxBottomLevel;
  }

  // Estimate the data ready time for unscheduled tasks
  private static int estimateDataReadyTime(State state, Graph<IOTask, DefaultWeightedEdge> graph, int numProcessors) {
    int maxDRT = 0;
    for (String taskId : state.getUnscheduledTaskIds()) {
      IOTask task = state.getTaskById(taskId, graph);
      int minDRT = Integer.MAX_VALUE;
      for (int processor = 0; processor < numProcessors; processor++) {
        int drt = calculateDataReadyTime(task, processor, state, graph);
        minDRT = Math.min(minDRT, drt);
      }
      int bottomLevel = calculateBottomLevel(task, state, graph);
      maxDRT = Math.max(maxDRT, minDRT + bottomLevel);
    }
    return maxDRT;
  }

  // Helper method to calculate the bottom level (critical path) of a task
  private static int calculateBottomLevel(
      IOTask task, State state, Graph<IOTask, DefaultWeightedEdge> graph) {
    int longestPath = 0;
    for (DefaultWeightedEdge edge : graph.outgoingEdgesOf(task)) {
      IOTask successor = graph.getEdgeTarget(edge);
      int pathLength = task.getTaskLength() + calculateBottomLevel(successor, state, graph);
      longestPath = Math.max(longestPath, pathLength);
    }
    return longestPath;
  }

  // Helper method to calculate the data ready time for a task on a specific
  // processor
  private static int calculateDataReadyTime(
      IOTask task, int processor, State state, Graph<IOTask, DefaultWeightedEdge> graph) {
    int maxReadyTime = 0;
    for (DefaultWeightedEdge edge : graph.incomingEdgesOf(task)) {
      IOTask predecessor = graph.getEdgeSource(edge);
      StateTask predecessorInfo = state.getIdsToStateTasks().get(predecessor.getId());
      int finishTime = predecessorInfo != null
          ? predecessorInfo.getStartTime() + predecessorInfo.getDuration()
          : predecessor.getTaskLength();
      int communicationDelay = (int) graph.getEdgeWeight(edge);
      if (predecessorInfo == null || predecessorInfo.getProcessor() != processor) {
        finishTime += communicationDelay;
      }
      maxReadyTime = Math.max(maxReadyTime, finishTime);
    }
    return maxReadyTime;
  }
}
