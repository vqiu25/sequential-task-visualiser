package org.se306.helpers;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.se306.domain.IOTask;

public class Preprocessing {

  /**
   * Returns the sum of all task weights in the graph (but not edge weights).
   */
  public static int getTotalComputeTime(Graph<IOTask, DefaultWeightedEdge> graph) {
    int totalComputeTime = 0;
    for (IOTask task : graph.vertexSet()) {
      totalComputeTime += task.getTaskLength();
    }
    return totalComputeTime;
  }

  // public static void calculateBottomLevels(Graph<IOTask, DefaultWeightedEdge> graph) {
  //   // Get reverse topological order of tasks
  //   TopologicalOrderIterator<IOTask, DefaultWeightedEdge> iterator = new TopologicalOrderIterator<>(graph);
  //   Stack<IOTask> reverseTopologicalOrder = new Stack<>();

  //   while (iterator.hasNext()) {
  //     reverseTopologicalOrder.push(iterator.next());
  //   }

  //   // Calculate bottom levels
  //   for (IOTask task : reverseTopologicalOrder) {
  //     if (graph.outgoingEdgesOf(task).isEmpty()) {
  //       task.setBottomLevel(task.getTaskLength());
  //     } else {
  //       int maxBottomLevel = 0;
  //       for (DefaultWeightedEdge edge : graph.outgoingEdgesOf(task)) {
  //         IOTask child = graph.getEdgeTarget(edge);
  //         maxBottomLevel = Math.max(maxBottomLevel, child.getBottomLevel() + task.getTaskLength());
  //       }
  //       task.setBottomLevel(maxBottomLevel);
  //     }
  //   }
  // }

}
