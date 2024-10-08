package org.se306.algorithms;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.se306.domain.State;
import org.se306.domain.Task;
import org.se306.domain.TaskInfo;

public class AStarSearch {

  // Method to find the optimal schedule using A* search (ignore the name)
  public static void findValidSchedule(Graph<Task, DefaultWeightedEdge> graph, int numProcessors) {

    // Initialize the open set as a priority queue (A* search frontier)
    PriorityQueue<State> openSet = new PriorityQueue<>(Comparator.comparingDouble(s -> s.getfScore()));
    Map<String, Integer> closedSet = new HashMap<>();

    // Initial state: no tasks scheduled yet
    State initialState = new State(numProcessors);

    // Initialize unscheduled tasks with all task IDs from the graph
    for (Task task : graph.vertexSet()) {
      initialState.getUnscheduledTasks().add(task.getId());
    }

    openSet.add(initialState);

    while (!openSet.isEmpty()) {
      State currentState = openSet.poll();

      // If all tasks are scheduled, update the graph with the schedule and return
      if (currentState.getUnscheduledTasks().isEmpty()) {
        // Update tasks in the graph with scheduled times and processors
        for (Task task : graph.vertexSet()) {
          TaskInfo info = currentState.getTaskInfoMap().get(task.getId());
          task.setStartTime((int) info.startTime);
          task.setProcessor(info.processor + 1); // Processors are 1-indexed
        }
        return;
      }

      // Generate a unique key for the current state
      String stateKey = currentState.getStateKey();

      // Check if this state has already been explored with a lower gScore
      if (closedSet.containsKey(stateKey) && currentState.getgScore() >= closedSet.get(stateKey)) {
        continue;
      }

      // Add current state to closed set
      closedSet.put(stateKey, currentState.getgScore());

      // Get all tasks that are ready to be scheduled (all predecessors are scheduled)
      List<Task> readyTasks = currentState.getReadyTasks(graph);

      for (Task task : readyTasks) {
        // For each processor, schedule the task and create a new state
        for (int processor = 0; processor < numProcessors; processor++) {
          State newState = currentState.scheduleTask(task, processor, graph);

          // Calculate gScore (makespan of the new state)
          int tentativeGScore = newState.getMakespan();

          // Calculate fScore
          int tentativeFScore = tentativeGScore + heuristicEstimate(newState, graph, numProcessors);

          // Generate a unique key for the new state (so we don't explore the same state)
          String newStateKey = newState.getStateKey();

          // If this state has already been explored with a lower gScore skip it
          if (closedSet.containsKey(newStateKey) && tentativeGScore >= closedSet.get(newStateKey)) {
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
  private static int heuristicEstimate(State state, Graph<Task, DefaultWeightedEdge> graph, int numProcessors) {
    int idleTimeEstimate = estimateIdleTime(state, graph, numProcessors);
    int bottomLevelEstimate = estimateBottomLevel(state, graph);
    int dataReadyTimeEstimate = estimateDataReadyTime(state, graph, numProcessors);

    // Return the maximum of the three components (as written in Oliver's paper)
    return Math.max(Math.max(idleTimeEstimate, bottomLevelEstimate), dataReadyTimeEstimate);
  }

  // Estimate the idle time based on the current state
  private static int estimateIdleTime(State state, Graph<Task, DefaultWeightedEdge> graph, int numProcessors) {
    int totalComputationTime = 0;
    for (Task task : graph.vertexSet()) {
      totalComputationTime += task.getTaskLength();
    }

    // Return the idle time estimate based on the total computation time divided by
    // the number of
    // processors
    return (totalComputationTime + state.getIdleTime()) / numProcessors;
  }

  // Estimate the bottom level for tasks already scheduled
  private static int estimateBottomLevel(State state, Graph<Task, DefaultWeightedEdge> graph) {
    int maxBottomLevel = 0;
    for (TaskInfo taskInfo : state.getTaskInfoMap().values()) {
      Task task = state.getTaskById(taskInfo.processor + "", graph);
      int bottomLevel = calculateBottomLevel(task, state, graph);
      maxBottomLevel = Math.max(maxBottomLevel, taskInfo.startTime + bottomLevel);
    }
    return maxBottomLevel;
  }

  // Estimate the data ready time for unscheduled tasks
  private static int estimateDataReadyTime(State state, Graph<Task, DefaultWeightedEdge> graph, int numProcessors) {
    int maxDRT = 0;
    for (String taskId : state.getUnscheduledTasks()) {
      Task task = state.getTaskById(taskId, graph);
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
      Task task, State state, Graph<Task, DefaultWeightedEdge> graph) {
    int longestPath = 0;
    for (DefaultWeightedEdge edge : graph.outgoingEdgesOf(task)) {
      Task successor = graph.getEdgeTarget(edge);
      int pathLength = task.getTaskLength() + calculateBottomLevel(successor, state, graph);
      longestPath = Math.max(longestPath, pathLength);
    }
    return longestPath;
  }

  // Helper method to calculate the data ready time for a task on a specific
  // processor
  private static int calculateDataReadyTime(
      Task task, int processor, State state, Graph<Task, DefaultWeightedEdge> graph) {
    int maxReadyTime = 0;
    for (DefaultWeightedEdge edge : graph.incomingEdgesOf(task)) {
      Task predecessor = graph.getEdgeSource(edge);
      TaskInfo predecessorInfo = state.getTaskInfoMap().get(predecessor.getId());
      int finishTime = predecessorInfo != null
          ? predecessorInfo.startTime + predecessorInfo.duration
          : predecessor.getTaskLength();
      int communicationDelay = (int) graph.getEdgeWeight(edge);
      if (predecessorInfo == null || predecessorInfo.processor != processor) {
        finishTime += communicationDelay;
      }
      maxReadyTime = Math.max(maxReadyTime, finishTime);
    }
    return maxReadyTime;
  }
}
