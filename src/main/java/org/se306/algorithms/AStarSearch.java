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

public class AStarSearch {

  // Method to find the optimal schedule using A* search (ignore the name)
  public static void findValidSchedule(Graph<IOTask, DefaultWeightedEdge> graph, int numProcessors) {

    // Initialize the open set as a priority queue (A* search frontier)
    PriorityQueue<State> openSet = new PriorityQueue<>(Comparator.comparingDouble(s -> s.getfScore()));
    Map<State, Integer> closedSet = new HashMap<>();

    // Initial state: no tasks scheduled yet
    State initialState = new State(numProcessors);

    // Initialize unscheduled tasks with all task IDs from the graph
    for (IOTask task : graph.vertexSet()) {
      initialState.getUnscheduledTasks().add(task.getId());
    }

    openSet.add(initialState);

    while (!openSet.isEmpty()) {
      State currentState = openSet.poll();

      // If all tasks are scheduled, update the graph with the schedule and return
      if (currentState.getUnscheduledTasks().isEmpty()) {
        // Update tasks in the graph with scheduled times and processors
        for (IOTask task : graph.vertexSet()) {
          StateTask info = currentState.getTaskInfoMap().get(task.getId());
          task.setStartTime(info.getStartTime());
          task.setProcessor(info.getProcessor() + 1); // Processors are 1-indexed
        }
        return;
      }

      // Check if this state has already been explored with a lower gScore
      if (closedSet.containsKey(currentState) && currentState.getgScore() >= closedSet.get(currentState)) {
        continue;
      }

      // Add current state to closed set
      closedSet.put(currentState, currentState.getgScore());

      // Get all tasks that are ready to be scheduled (all predecessors are scheduled)
      List<IOTask> readyTasks = currentState.getReadyTasks(graph);

      for (IOTask task : readyTasks) {
        // For each processor, schedule the task and create a new state
        for (int processor = 0; processor < numProcessors; processor++) {
          State newState = currentState.scheduleTask(task, processor, graph);

          // Calculate gScore (makespan of the new state)
          int tentativeGScore = newState.getMakespan();

          // Calculate fScore
          int tentativeFScore = tentativeGScore + heuristicEstimate(newState, graph, numProcessors);

          // If this state has already been explored with a lower gScore skip it
          if (closedSet.containsKey(newState) && tentativeGScore >= closedSet.get(newState)) {
            continue;
          }

          // Set the scores and add the new state to the open set
          newState.setgScore(tentativeGScore);
          newState.setfScore(tentativeFScore);
          openSet.add(newState);
        }
      }
    }

    // If no valid schedule is found exception
    throw new RuntimeException("No valid schedule found.");
  }

  // THIS HEURSITIC IS BASED ON OLIVER PAPER I DONT UNDERSTAND IT I JUST COPIED
  // HIM
  private static int heuristicEstimate(State state, Graph<IOTask, DefaultWeightedEdge> graph, int numProcessors) {
    int idleTimeEstimate = estimateIdleTime(state, graph, numProcessors);
    int bottomLevelEstimate = estimateBottomLevel(state, graph);
    int dataReadyTimeEstimate = estimateDataReadyTime(state, graph, numProcessors);

    // Return the maximum of the three components (as written in Oliver's paper)
    return Math.max(Math.max(idleTimeEstimate, bottomLevelEstimate), dataReadyTimeEstimate);
  }

  // Estimate the idle time based on the current state
  private static int estimateIdleTime(State state, Graph<IOTask, DefaultWeightedEdge> graph, int numProcessors) {
    int totalComputationTime = 0;
    for (IOTask task : graph.vertexSet()) {
      totalComputationTime += task.getTaskLength();
    }

    // Return the idle time estimate based on the total computation time divided by
    // the number of
    // processors
    return (totalComputationTime + state.getIdleTime()) / numProcessors;
  }

  // Estimate the bottom level for tasks already scheduled
  private static int estimateBottomLevel(State state, Graph<IOTask, DefaultWeightedEdge> graph) {
    int maxBottomLevel = 0;
    for (StateTask taskInfo : state.getTaskInfoMap().values()) {
      IOTask task = state.getTaskById(taskInfo.getProcessor() + "", graph);
      int bottomLevel = calculateBottomLevel(task, state, graph);
      maxBottomLevel = Math.max(maxBottomLevel, taskInfo.getStartTime() + bottomLevel);
    }
    return maxBottomLevel;
  }

  // Estimate the data ready time for unscheduled tasks
  private static int estimateDataReadyTime(State state, Graph<IOTask, DefaultWeightedEdge> graph, int numProcessors) {
    int maxDRT = 0;
    for (String taskId : state.getUnscheduledTasks()) {
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
      StateTask predecessorInfo = state.getTaskInfoMap().get(predecessor.getId());
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
