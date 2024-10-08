package org.se306.algorithms;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.se306.domain.IOTask;
import org.se306.domain.State;
import org.se306.domain.StateTask;
import org.se306.helpers.FFunction;

public class AStarSearch {

  // Method to find the optimal schedule using A* search (ignore the name)
  public static void findValidSchedule(Graph<IOTask, DefaultWeightedEdge> graph, int numProcessors) {

    // Initialize the open set as a priority queue (A* search frontier)
    PriorityQueue<State> openQueue = new PriorityQueue<>(Comparator.comparingDouble(s -> s.getfScore()));
    Map<State, Integer> closedMap = new HashMap<>();

    // Initial state: no tasks scheduled yet
    State initialState = new State(numProcessors);

    // Initialize unscheduled tasks with all task IDs from the graph
    // TODO: will be removed if/when we switch from tracking unscheduled tasks to ready tasks
    for (IOTask task : graph.vertexSet()) {
      initialState.getUnscheduledTaskIds().add(task.getId());
    }

    openQueue.add(initialState);

    while (!openQueue.isEmpty()) {
      State currentState = openQueue.poll();

      // If all tasks are scheduled, update the graph with the schedule and return
      if (currentState.getUnscheduledTaskIds().isEmpty()) {
        updateTaskGraph(graph, currentState);
        return;
      }

      // Check if this state has already been explored with a lower gScore
      if (closedMap.containsKey(currentState) && currentState.getMakespan() >= closedMap.get(currentState)) {
        continue;
      }

      // Add current state to closed set
      closedMap.put(currentState, currentState.getMakespan());

      // Get all tasks that are ready to be scheduled (all predecessors are scheduled)
      List<IOTask> readyTasks = currentState.getReadyTasks(graph);

      for (IOTask task : readyTasks) {
        // For each processor, schedule the task and create a new state
        for (int processor = 0; processor < numProcessors; processor++) {
          State newState = currentState.scheduleTask(task, processor, graph);

          // Calculate gScore (makespan of the new state)
          int tentativeGScore = newState.getMakespan();

          // Calculate fScore
          int tentativeFScore = tentativeGScore + FFunction.heuristicEstimate(newState, graph, numProcessors);

          // If this state has already been explored with a lower gScore skip it
          if (closedMap.containsKey(newState) && tentativeGScore >= closedMap.get(newState)) {
            continue;
          }

          // Set the scores and add the new state to the open set
          newState.setMakespan(tentativeGScore);
          newState.setfScore(tentativeFScore);
          openQueue.add(newState);
        }
      }
    }

    // If no valid schedule is found exception
    throw new RuntimeException("No valid schedule found.");
  }

  /**
   * Update the task graph with the schedule from the final state of the A*
   * search. Ran after A* completes.
   */
  private static void updateTaskGraph(Graph<IOTask, DefaultWeightedEdge> taskGraph, State goalState) {
    for (IOTask ioTask : taskGraph.vertexSet()) {
      StateTask stateTask = goalState.getStateTaskFromIOTask(ioTask);
      ioTask.setStartTime(stateTask.getStartTime());
      ioTask.setProcessor(stateTask.getProcessor() + 1); // Output processors are 1-indexed
    }
  }
}
