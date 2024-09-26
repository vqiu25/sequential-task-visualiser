package org.se306.algorithms;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;
import org.se306.domain.Task;

public class ValidSchedule {
  public static void findValidSchedule(Graph<Task, DefaultWeightedEdge> graph, int numProcessors) {
    // Perform topological sorting
    TopologicalOrderIterator<Task, DefaultWeightedEdge> iterator =
        new TopologicalOrderIterator<>(graph);

    // Tracks the time each processor is available to start a new task
    int[] processorAvailableTime = new int[numProcessors];

    // Process tasks in topological order
    while (iterator.hasNext()) {
      Task task = iterator.next();

      // Initialise variables for processor assignment
      int chosenProcessor = -1;
      int minStartTime = Integer.MAX_VALUE;

      // Try assigning the task to each processor and pick the best
      for (int i = 0; i < numProcessors; i++) {
        int earliestStartTime = processorAvailableTime[i];

        // Calculate earliest start time on processor i, considering dependencies
        for (DefaultWeightedEdge incomingEdge : graph.incomingEdgesOf(task)) {
          Task predecessor = graph.getEdgeSource(incomingEdge);
          int communicationDelay = (int) graph.getEdgeWeight(incomingEdge);
          int finishTime = predecessor.getStartTime() + predecessor.getTaskLength();

          if (predecessor.getProcessor() != i) {
            finishTime += communicationDelay;
          }
          earliestStartTime = Math.max(earliestStartTime, finishTime);
        }

        // Update if this processor allows an earlier start time
        if (earliestStartTime < minStartTime) {
          minStartTime = earliestStartTime;
          chosenProcessor = i;
        }
      }

      // Assign the task to the chosen processor and update times
      task.setStartTime(minStartTime);
      task.setProcessor(chosenProcessor);

      // Update processor availability time
      processorAvailableTime[chosenProcessor] = minStartTime + task.getTaskLength();
    }
  }
}
