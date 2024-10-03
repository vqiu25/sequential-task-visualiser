package org.se306.helpers;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.se306.domain.Task;

import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class AStarSearch {

    // Method to find the optimal schedule using A* search (ignore the name)
    public static void findValidSchedule(Graph<Task, DefaultWeightedEdge> graph, int numProcessors) {
        // Initialize the open set as a priority queue (A* search frontier)
        PriorityQueue<State> openSet = new PriorityQueue<>(Comparator.comparingDouble(s -> s.fScore));
        Map<String, Double> closedSet = new HashMap<>();

        // Initial state: no tasks scheduled yet
        State initialState = new State(numProcessors);

        // Initialize unscheduled tasks with all task IDs from the graph
        for (Task task : graph.vertexSet()) {
            initialState.unscheduledTasks.add(task.getId());
        }

        openSet.add(initialState);

        while (!openSet.isEmpty()) {
            State currentState = openSet.poll();

            // If all tasks are scheduled, update the graph with the schedule and return
            if (currentState.unscheduledTasks.isEmpty()) {
                // Update tasks in the graph with scheduled times and processors
                for (Task task : graph.vertexSet()) {
                    TaskInfo info = currentState.taskInfoMap.get(task.getId());
                    task.setStartTime((int) info.startTime);
                    task.setProcessor(info.processor + 1); // Processors are 1-indexed
                }
                return;
            }

            // Generate a unique key for the current state
            String stateKey = currentState.getStateKey();

            // Check if this state has already been explored with a lower gScore
            if (closedSet.containsKey(stateKey) && currentState.gScore >= closedSet.get(stateKey)) {
                continue;
            }

            // Add current state to closed set
            closedSet.put(stateKey, currentState.gScore);

            // Get all tasks that are ready to be scheduled (all predecessors are scheduled)
            List<Task> readyTasks = currentState.getReadyTasks(graph);

            for (Task task : readyTasks) {
                // For each processor, schedule the task and create a new state
                for (int processor = 0; processor < numProcessors; processor++) {
                    State newState = currentState.scheduleTask(task, processor, graph);

                    // Calculate gScore (makespan of the new state)
                    double tentativeGScore = newState.getMakespan();

                    // Calculate fScore
                    double tentativeFScore = tentativeGScore + heuristicEstimate(newState, graph);

                    // Generate a unique key for the new state (so we don't explore the same state)
                    String newStateKey = newState.getStateKey();

                    // If this state has already been explored with a lower gScore skip it
                    if (closedSet.containsKey(newStateKey) && tentativeGScore >= closedSet.get(newStateKey)) {
                        continue;
                    }

                    // Set the scores and add the new state to the open set
                    newState.gScore = tentativeGScore;
                    newState.fScore = tentativeFScore;
                    openSet.add(newState);
                }
            }
        }

        // If no valid schedule is found exception
        throw new RuntimeException("No valid schedule found.");
    }

    // State class with each state representing a partial schedule
    private static class State {
        Map<String, TaskInfo> taskInfoMap;
        Set<String> unscheduledTasks;
        double gScore;
        double fScore;
        int numProcessors;
        double[] processorAvailableTime;

        // Initial state constructor
        State(int numProcessors) {
            this.taskInfoMap = new HashMap<>();
            this.unscheduledTasks = new HashSet<>();
            this.gScore = 0.0;
            this.fScore = 0.0;
            this.numProcessors = numProcessors;
            this.processorAvailableTime = new double[numProcessors];
            Arrays.fill(this.processorAvailableTime, 0.0);
        }

        // Copy constructor
        State(State other) {
            this.taskInfoMap = new HashMap<>(other.taskInfoMap);
            this.unscheduledTasks = new HashSet<>(other.unscheduledTasks);
            this.gScore = other.gScore;
            this.fScore = other.fScore;
            this.numProcessors = other.numProcessors;
            this.processorAvailableTime = Arrays.copyOf(other.processorAvailableTime, other.numProcessors);
        }

        // Get a unique key representing the state
        String getStateKey() {
            // Key based on scheduled tasks and their assignments
            StringBuilder keyBuilder = new StringBuilder();
            taskInfoMap.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> keyBuilder.append(entry.getKey()).append(":")
                            .append(entry.getValue().processor).append(":")
                            .append(entry.getValue().startTime).append("|"));
            return keyBuilder.toString();
        }

        // Get tasks that are ready to be scheduled
        List<Task> getReadyTasks(Graph<Task, DefaultWeightedEdge> graph) {
            List<Task> readyTasks = new ArrayList<>();
            for (String taskId : unscheduledTasks) {
                Task task = getTaskById(taskId, graph);
                boolean allPredecessorsScheduled = true;
                for (DefaultWeightedEdge edge : graph.incomingEdgesOf(task)) {
                    Task predecessor = graph.getEdgeSource(edge);
                    if (!taskInfoMap.containsKey(predecessor.getId())) {
                        allPredecessorsScheduled = false;
                        break;
                    }
                }
                if (allPredecessorsScheduled) {
                    readyTasks.add(task);
                }
            }
            return readyTasks;
        }

        // Schedule a task and return a new state
        State scheduleTask(Task task, int processor, Graph<Task, DefaultWeightedEdge> graph) {
            State newState = new State(this);

            // Determine earliest start time
            double earliestStartTime = newState.processorAvailableTime[processor];

            // Consider dependencies
            for (DefaultWeightedEdge edge : graph.incomingEdgesOf(task)) {
                Task predecessor = graph.getEdgeSource(edge);
                TaskInfo predecessorInfo = newState.taskInfoMap.get(predecessor.getId());

                // Check if predecessorInfo is null (should not happen)
                if (predecessorInfo == null) {
                    throw new RuntimeException("Predecessor " + predecessor.getId() + " not scheduled yet.");
                }

                double finishTime = predecessorInfo.startTime + predecessorInfo.duration;
                double communicationDelay = graph.getEdgeWeight(edge);

                if (predecessorInfo.processor != processor) {
                    finishTime += communicationDelay;
                }

                earliestStartTime = Math.max(earliestStartTime, finishTime);
            }

            // Schedule the task
            double taskDuration = task.getTaskLength();
            newState.taskInfoMap.put(task.getId(), new TaskInfo(processor, earliestStartTime, taskDuration));
            newState.processorAvailableTime[processor] = earliestStartTime + taskDuration;
            newState.unscheduledTasks.remove(task.getId());

            // Update gScore (makespan)
            newState.gScore = newState.getMakespan();

            return newState;
        }

        // Get the makespan of the current schedule
        double getMakespan() {
            return Arrays.stream(processorAvailableTime).max().orElse(0.0);
        }

        // Calculate idle time for all processors (this is important for oliver's heurstiic)
        public double getIdleTime() {
            double makespan = getMakespan();
            double totalUsedTime = 0.0;
            for (double availableTime : processorAvailableTime) {
                totalUsedTime += availableTime;
            }
            // Idle time is the difference between makespan * number of processors and total used time
            return (makespan * numProcessors) - totalUsedTime;
        }

        // Helper method to get Task by ID
        private Task getTaskById(String id, Graph<Task, DefaultWeightedEdge> graph) {
            for (Task task : graph.vertexSet()) {
                if (task.getId().equals(id)) {
                    return task;
                }
            }
            return null;
        }
    }

    // Task scheduling information
    private static class TaskInfo {
        int processor;       // Processor index (0-based)
        double startTime;    // Start time of the task
        double duration;     // Duration of the task

        TaskInfo(int processor, double startTime, double duration) {
            this.processor = processor;
            this.startTime = startTime;
            this.duration = duration;
        }
    }

    // THIS HEURSITIC IS BASED ON OLIVER PAPER I DONT UNDERSTAND IT I JUST COPIED HIM
    private static double heuristicEstimate(State state, Graph<Task, DefaultWeightedEdge> graph) {
        double idleTimeEstimate = estimateIdleTime(state, graph);
        double bottomLevelEstimate = estimateBottomLevel(state, graph);
        double dataReadyTimeEstimate = estimateDataReadyTime(state, graph);

        // Return the maximum of the three components (as written in Oliver's paper)
        return Math.max(Math.max(idleTimeEstimate, bottomLevelEstimate), dataReadyTimeEstimate);
    }

    // Estimate the idle time based on the current state
    private static double estimateIdleTime(State state, Graph<Task, DefaultWeightedEdge> graph) {
        double totalComputationTime = 0.0;
        for (Task task : graph.vertexSet()) {
            totalComputationTime += task.getTaskLength();
        }

        // Return the idle time estimate based on the total computation time divided by the number of processors
        return (totalComputationTime + state.getIdleTime()) / state.numProcessors;
    }

    // Estimate the bottom level for tasks already scheduled
    private static double estimateBottomLevel(State state, Graph<Task, DefaultWeightedEdge> graph) {
        double maxBottomLevel = 0.0;
        for (TaskInfo taskInfo : state.taskInfoMap.values()) {
            Task task = state.getTaskById(taskInfo.processor + "", graph);
            double bottomLevel = calculateBottomLevel(task, state, graph);
            maxBottomLevel = Math.max(maxBottomLevel, taskInfo.startTime + bottomLevel);
        }
        return maxBottomLevel;
    }

    // Estimate the data ready time for unscheduled tasks
    private static double estimateDataReadyTime(State state, Graph<Task, DefaultWeightedEdge> graph) {
        double maxDRT = 0.0;
        for (String taskId : state.unscheduledTasks) {
            Task task = state.getTaskById(taskId, graph);
            double minDRT = Double.MAX_VALUE;
            for (int processor = 0; processor < state.numProcessors; processor++) {
                double drt = calculateDataReadyTime(task, processor, state, graph);
                minDRT = Math.min(minDRT, drt);
            }
            double bottomLevel = calculateBottomLevel(task, state, graph);
            maxDRT = Math.max(maxDRT, minDRT + bottomLevel);
        }
        return maxDRT;
    }

    // Helper method to calculate the bottom level (critical path) of a task
    private static double calculateBottomLevel(Task task, State state, Graph<Task, DefaultWeightedEdge> graph) {
        double longestPath = 0.0;
        for (DefaultWeightedEdge edge : graph.outgoingEdgesOf(task)) {
            Task successor = graph.getEdgeTarget(edge);
            double pathLength = task.getTaskLength() + calculateBottomLevel(successor, state, graph);
            longestPath = Math.max(longestPath, pathLength);
        }
        return longestPath;
    }

    // Helper method to calculate the data ready time for a task on a specific processor
    private static double calculateDataReadyTime(Task task, int processor, State state, Graph<Task, DefaultWeightedEdge> graph) {
        double maxReadyTime = 0.0;
        for (DefaultWeightedEdge edge : graph.incomingEdgesOf(task)) {
            Task predecessor = graph.getEdgeSource(edge);
            TaskInfo predecessorInfo = state.taskInfoMap.get(predecessor.getId());
            double finishTime = predecessorInfo != null ? predecessorInfo.startTime + predecessorInfo.duration : predecessor.getTaskLength();
            double communicationDelay = graph.getEdgeWeight(edge);
            if (predecessorInfo == null || predecessorInfo.processor != processor) {
                finishTime += communicationDelay;
            }
            maxReadyTime = Math.max(maxReadyTime, finishTime);
        }
        return maxReadyTime;
    }
}
