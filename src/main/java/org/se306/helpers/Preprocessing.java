package org.se306.helpers;

import java.util.ArrayDeque;
import java.util.Deque;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;
import org.se306.domain.IOTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Preprocessing {

  private static final Logger LOGGER = LoggerFactory.getLogger(Preprocessing.class);

  /** Returns the sum of all task weights in the graph (but not edge weights). */
  public static int getTotalComputeTime(Graph<IOTask, DefaultWeightedEdge> graph) {
    LOGGER.info("Begin calculate total compute time");

    int totalComputeTime = 0;
    for (IOTask task : graph.vertexSet()) {
      totalComputeTime += task.getTaskLength();
    }

    LOGGER.debug("Total compute time: {}", totalComputeTime);
    return totalComputeTime;
  }

  public static void calculateBottomLevels(Graph<IOTask, DefaultWeightedEdge> graph) {
    LOGGER.info("Begin calculate bottom levels");

    // Get reverse topological order of tasks
    TopologicalOrderIterator<IOTask, DefaultWeightedEdge> iterator =
        new TopologicalOrderIterator<>(graph);
    Deque<IOTask> reverseTopologicalOrder = new ArrayDeque<>(); // Stack to reverse order
    while (iterator.hasNext()) {
      reverseTopologicalOrder.push(iterator.next());
    }

    LOGGER.debug("Found reverse topological order");

    // Calculate bottom levels
    for (IOTask task : reverseTopologicalOrder) {
      int maxChildBottomLevel = 0;
      for (DefaultWeightedEdge edge : graph.outgoingEdgesOf(task)) {
        IOTask child = graph.getEdgeTarget(edge);
        maxChildBottomLevel = Math.max(maxChildBottomLevel, child.getBottomLevel());
      }
      task.setBottomLevel(maxChildBottomLevel + task.getTaskLength());
    }

    LOGGER.debug("Finished calculating bottom levels");
  }
}
