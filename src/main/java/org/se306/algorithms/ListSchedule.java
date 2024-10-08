package org.se306.algorithms;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;
import org.se306.domain.Task;

/**
 * This class performs a (non-optimal) list scheduling algorithm on the task
 * graph.
 * Currently unused, but may be useful for optimisation 3.4.
 */
public class ListSchedule{
  
  public static void findValidSchedule(Graph<Task, DefaultWeightedEdge> graph, int numProcessors) {
    // Perform topological sorting
    TopologicalOrderIterator<Task, DefaultWeightedEdge> iterator = new TopologicalOrderIterator<>(graph);

    // Tracks the time each processor is available to start a new task
    int[] processorAvailableTime = new int[numProcessors];

    // Process tasks in topological order
    while (iterator.hasNext()) {
      Task task = iterator.next();

      // Initialise variables for processor assignment
      int chosenProcessor = -1;
      int minStartTime = Integer.MAX_VALUE;

      // Try assigning the task to each processor and pick the best
      for (int currentProcessor = 1; currentProcessor <= numProcessors; currentProcessor++) {
        int earliestStartTime = processorAvailableTime[currentProcessor - 1]; // -1 because array is 0-indexed

        // Calculate earliest start time on processor currentProcessor, considering
        // dependencies
        for (DefaultWeightedEdge incomingEdge : graph.incomingEdgesOf(task)) {
          Task predecessor = graph.getEdgeSource(incomingEdge);
          int communicationDelay = (int) graph.getEdgeWeight(incomingEdge);
          int finishTime = predecessor.getStartTime() + predecessor.getTaskLength();

          if (predecessor.getProcessor() != currentProcessor) {
            finishTime += communicationDelay;
          }
          earliestStartTime = Math.max(earliestStartTime, finishTime);
        }

        // Update if this processor allows an earlier start time
        if (earliestStartTime < minStartTime) {
          minStartTime = earliestStartTime;
          chosenProcessor = currentProcessor;
        }
      }

      // Assign the task to the chosen processor and update times
      task.setStartTime(minStartTime);
      task.setProcessor(chosenProcessor);

      // Update processor availability time
      processorAvailableTime[chosenProcessor - 1] = minStartTime + task.getTaskLength(); // -1 because array is
                                                                                         // 0-indexed
    }
  }
}
