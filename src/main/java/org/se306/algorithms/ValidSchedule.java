package org.se306.algorithms;

import java.util.HashMap;
import java.util.Map;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;
import org.se306.domain.Task;

public class ValidSchedule {
  private Graph<Task, DefaultEdge> findValidSchedule(Graph<Task, DefaultEdge> graph, int numProcessors) {
    TopologicalOrderIterator<Task, DefaultEdge> iterator = new TopologicalOrderIterator<Task, DefaultEdge>(graph);
    Map<Integer, Integer> processorNextStartTimes = new HashMap<Integer, Integer>();

    for (int i = 1; i <= numProcessors; i++) {
      processorNextStartTimes.put(i, 0);
    }

    while (iterator.hasNext()) {
      Task vertex = iterator.next();
      int bestStartTime = Integer.MAX_VALUE;
      int bestProcessor = -1;
      for (int processor = 1; processor <= numProcessors; processor++) {
        int startTime = processorNextStartTimes.get(processor); // Fix this calculation to include communication cost
        if (startTime < bestStartTime) {
          bestStartTime = startTime;
          bestProcessor = processor;
        }
      }

      vertex.setStartTime(bestStartTime);
      vertex.setProcessor(bestProcessor);
      processorNextStartTimes.put(bestProcessor, vertex.getFinishTime());
      // Modify the graph object as well
    }

    return graph;
  }
}
