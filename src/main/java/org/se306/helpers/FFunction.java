package org.se306.helpers;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.se306.domain.IOTask;
import org.se306.domain.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class to calculate the heuristic estimate f(s) for the A* search
 * algorithm.
 */
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
    int idleTimeEstimate = estimateIdleTime(state, graph, numProcessors, totalComputeTime);
    int bottomLevelEstimate = estimateBottomLevel(state, graph);
    // int dataReadyTimeEstimate = estimateDataReadyTime(state, graph, numProcessors);

    // // Return the maximum of the three components (as written in Oliver's paper)
    return Math.max(idleTimeEstimate, bottomLevelEstimate);

    // I feel DRT doesn't add anything! I've looked at Oliver's explanation over and
    // over and I just feel this just doesn't add anything - it just gets calculated
    // in the next cycle anyway! I'm probably missing something. - Nate
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
  }
}
