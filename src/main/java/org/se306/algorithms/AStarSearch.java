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
import org.se306.helpers.IOTaskMap;
import org.se306.helpers.Preprocessing;

public class AStarSearch {

  // Method to find the optimal schedule using A* search
  public static void findSchedule(Graph<IOTask, DefaultWeightedEdge> graph, int numProcessors) {

    int totalComputeTime = Preprocessing.getTotalComputeTime(graph);
    Preprocessing.calculateBottomLevels(graph);
    IOTaskMap.initialise(graph);

    // Initialize the open set as a priority queue (A* search frontier)
    PriorityQueue<State> openQueue =
        new PriorityQueue<>(Comparator.comparingInt(s -> s.getfScore()));
    Map<State, Integer> closedMap = new HashMap<>();

    // Add s_init to kick off A*
    addInitialState(openQueue, numProcessors, graph);

    while (!openQueue.isEmpty()) {
      State currentState = openQueue.poll();

      // If all tasks are scheduled, update the graph with the schedule and return
      if (currentState.getUnscheduledTaskIds().isEmpty()) {
        updateTaskGraph(graph, currentState);
        return;
      }

      // If this state has already been explored with a lower makespan skip it
      if (stateAlreadyExploredWithLowerMakespan(
          currentState, currentState.getMakespan(), closedMap)) {
        continue;
      }

      // Expand state & move to closed set
      expandState(currentState, openQueue, closedMap, graph, totalComputeTime);
      closedMap.put(currentState, currentState.getMakespan());
    }

    // If no valid schedule is found exception
    throw new RuntimeException("No valid schedule found.");
  }

  /** Add the initial state to the open queue at the start of the A* search. */
  private static void addInitialState(
      PriorityQueue<State> openQueue,
      int numProcessors,
      Graph<IOTask, DefaultWeightedEdge> taskGraph) {

    State initialState = new State(numProcessors); // No tasks

    // Initialize unscheduled tasks with all task IDs from the graph
    // TODO: will be removed if/when we switch from tracking unscheduled tasks to
    // ready tasks
    for (IOTask task : taskGraph.vertexSet()) {
      initialState.getUnscheduledTaskIds().add(task.getId());
    }

    openQueue.add(initialState);
  }

  /**
   * Expand the current state by scheduling all tasks that are ready to be scheduled. Creates a new
   * state for each task and processor combination (|n|x|P| total) and adds them to the open queue.
   */
  private static void expandState(
      State currentState,
      PriorityQueue<State> openQueue,
      Map<State, Integer> closedMap,
      Graph<IOTask, DefaultWeightedEdge> taskGraph,
      int totalComputeTime) {

    // Get all tasks that are ready to be scheduled (all predecessors are scheduled)
    List<IOTask> readyTasks = currentState.getReadyTasks(taskGraph);
    int numProcessors = currentState.getNumProcessors();

    // Schedule each ready task on each processor -> |n|x|P| new states
    for (IOTask task : readyTasks) {
      for (int processor = 0; processor < currentState.getNumProcessors(); processor++) {
        State newState = currentState.scheduleTask(task, processor, taskGraph);

        // Calculate the fScore for the new state
        int makespan = newState.getMakespan(); // 'Distance' from s_init (start state)
        int heuristic =
            FFunction.heuristicEstimate(
                newState, taskGraph, numProcessors, totalComputeTime); // 'ETA' to goal
        int fScore = makespan + heuristic; // Estimated total 'distance' f(s) = g(s) + h(s)

        // If this state has already been explored with a lower makespan skip it
        if (stateAlreadyExploredWithLowerMakespan(newState, makespan, closedMap)) {
          continue;
        }

        // Record scores and add to OPEN
        newState.setMakespan(makespan);
        newState.setfScore(fScore);
        openQueue.add(newState);
      }
    }
  }

  /** Check if the state has already been explored with a lower makespan. If so, we can skip it. */
  private static boolean stateAlreadyExploredWithLowerMakespan(
      State newState, int makespan, Map<State, Integer> closedMap) {
    return closedMap.containsKey(newState) && makespan >= closedMap.get(newState);
  }

  /**
   * Update the task graph with the schedule from the final state of the A* search. Ran after A*
   * completes.
   */
  private static void updateTaskGraph(
      Graph<IOTask, DefaultWeightedEdge> taskGraph, State goalState) {
    for (IOTask ioTask : taskGraph.vertexSet()) {
      StateTask stateTask = goalState.getStateTaskFromIOTask(ioTask);
      ioTask.setStartTime(stateTask.getStartTime());
      ioTask.setProcessor(stateTask.getProcessor() + 1); // Output processors are 1-indexed
    }
  }
}
