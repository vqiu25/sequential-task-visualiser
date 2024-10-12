package org.se306.helpers;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.se306.domain.IOTask;
import org.se306.domain.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Helper class to calculate the heuristic estimate f(s) for the A* search algorithm. */
public class FFunction {

  private static final Logger LOGGER = LoggerFactory.getLogger(FFunction.class);

  /**
   * Calculates the h(s) part of f(s) = g(s) + h(s)
   *
   * <p>- f(s) is the ETA of the current state s - g(s) is the makespan of the current state s -
   * h(s) is the heuristic estimate of the remaining time to complete the schedule
   */
  public static int heuristicEstimate(
      State state,
      Graph<IOTask, DefaultWeightedEdge> graph,
      int numProcessors,
      int totalComputeTime) {
    int idleTimeEstimate = estimateIdleTime(state, graph, numProcessors, totalComputeTime);
    int bottomLevelEstimate = estimateBottomLevel(state, graph);
    int dataReadyTimeEstimate = estimateDataReadyTime(state, graph);

    if (idleTimeEstimate >= bottomLevelEstimate && idleTimeEstimate >= dataReadyTimeEstimate) {
      // Idle Time
      state.setHeuristicType(0);
    } else if (bottomLevelEstimate >= idleTimeEstimate
        && bottomLevelEstimate >= dataReadyTimeEstimate) {
      // Bottom Level
      state.setHeuristicType(1);
    } else {
      // Data Ready Time
      state.setHeuristicType(2);
    }

    // Return the maximum of the three components (as written in Oliver's paper)
    // Returns a minimum of 0 to prevent negative values
    return Math.max(
        Math.max(idleTimeEstimate, bottomLevelEstimate), Math.max(dataReadyTimeEstimate, 0));
  }

  /**
   * Estimate the difference between the ETA calculated from the idle-time estimate and the current
   * makespan.
   */
  private static int estimateIdleTime(
      State state,
      Graph<IOTask, DefaultWeightedEdge> graph,
      int numProcessors,
      int totalComputationTime) {
    int idleTimeEtaEstimate =
        (totalComputationTime + state.getIdleTime()) / numProcessors; // ETA based on idle time
    return idleTimeEtaEstimate - state.getMakespan();
  }

  /**
   * Estimate the difference between the ETA calculated from the bottom-level estimate and the
   * current makespan.
   */
  private static int estimateBottomLevel(State state, Graph<IOTask, DefaultWeightedEdge> graph) {
    return state.getBottomLevelEta() - state.getMakespan();
  }

  /**
   * Estimate the difference between the ETA calculated from the data-ready-time estimate and the
   * current makespan.
   */
  private static int estimateDataReadyTime(State state, Graph<IOTask, DefaultWeightedEdge> graph) {
    return state.getDRT(graph) - state.getMakespan();
  }
}
